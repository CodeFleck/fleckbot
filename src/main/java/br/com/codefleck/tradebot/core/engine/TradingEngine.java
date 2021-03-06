package br.com.codefleck.tradebot.core.engine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.codefleck.tradebot.core.config.exchange.AuthenticationConfigImpl;
import br.com.codefleck.tradebot.core.config.exchange.ExchangeConfigImpl;
import br.com.codefleck.tradebot.core.config.exchange.NetworkConfigImpl;
import br.com.codefleck.tradebot.core.config.exchange.OptionalConfigImpl;
import br.com.codefleck.tradebot.core.config.market.MarketImpl;
import br.com.codefleck.tradebot.core.config.strategy.StrategyConfigItems;
import br.com.codefleck.tradebot.core.mail.EmailAlerter;
import br.com.codefleck.tradebot.core.util.ConfigurableComponentFactory;
import br.com.codefleck.tradebot.models.engine.EngineConfig;
import br.com.codefleck.tradebot.models.exchange.AuthenticationConfig;
import br.com.codefleck.tradebot.models.exchange.ExchangeConfig;
import br.com.codefleck.tradebot.models.exchange.NetworkConfig;
import br.com.codefleck.tradebot.models.exchange.OptionalConfig;
import br.com.codefleck.tradebot.models.market.MarketConfig;
import br.com.codefleck.tradebot.models.strategy.StrategyConfig;
import br.com.codefleck.tradebot.exchanges.exchangeInterfaces.ExchangeAdapter;
import br.com.codefleck.tradebot.exchanges.trading.LogEntryImpl;
import br.com.codefleck.tradebot.services.EngineConfigService;
import br.com.codefleck.tradebot.services.ExchangeConfigService;
import br.com.codefleck.tradebot.services.MarketConfigService;
import br.com.codefleck.tradebot.services.StrategyConfigService;
import br.com.codefleck.tradebot.strategyapi.TradingStrategy;
import br.com.codefleck.tradebot.tradingInterfaces.BalanceInfo;
import br.com.codefleck.tradebot.tradingInterfaces.ExchangeNetworkException;
import br.com.codefleck.tradebot.tradingInterfaces.Market;
import br.com.codefleck.tradebot.tradingInterfaces.TradingApiException;

/**
 * The main Trading Engine.
 * <p>
 * The engine has been coded to fail *hard and fast* whenever something unexpected happens. If Email
 * Alerts are enabled, a message will be sent with details of the problem before the bot is shutdown.
 * <p>
 * The only time the bot does not fail hard and fast is for network issues connecting to the exchange - it logs the error
 * and retries at next trade cycle.
 * <p>
 * To keep things simple:
 * - The engine is single threaded.
 * - The engine only supports trading on 1 exchange per instance of the bot, i.e. 1 Exchange Adapter per process.
 * - The engine only supports 1 Trading Strategy per Market.
 *
 */
@Component
public class TradingEngine {

    private static final Logger LOG = LogManager.getLogger();

    // Email Alert error message stuff
    private static final String CRITICAL_EMAIL_ALERT_SUBJECT = "CRITICAL Alert message from FleckBot";
    private static final String DETAILS_ERROR_MSG_LABEL = " Details: ";
    private static final String CAUSE_ERROR_MSG_LABEL = " Cause: ";
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String HORIZONTAL_RULE = "--------------------------------------------------" + NEWLINE;

    /*
     * Keep list of log entries
     */
    public List<LogEntryImpl> logEntryList;

    /*
     * Trade execution interval in secs. The time we wait/sleep in between trade cycles.
     */
    private static int tradeExecutionInterval;

    /*
     * Control flag decides if the Trading Engine lives or dies.
     */
    private volatile boolean keepAlive = true;

    /*
     * Is Trading Engine already running? Used to prevent multiple 'starts' of the engine.
     */
    private boolean isRunning = false;

    /*
     * Monitor to use when checking if Trading Engine is running.
     */
    private static final Object IS_RUNNING_MONITOR = new Object();

    /*
     * The thread the Trading Engine is running in.
     */
    private Thread engineThread;

    /*
     * Map of Trading Strategy descriptions from config.
     */
    private final Map<String, StrategyConfig> strategyDescriptions = new HashMap<>();

    /*
     * List of cached Trading Strategy implementations for the Trade Engine to execute.
     */
    private final List<TradingStrategy> tradingStrategiesToExecute = new ArrayList<>();

    /*
     * The emergency stop currency value is used to prevent a catastrophic loss on the exchange.
     * It is set to the currency short code, e.g. BTC, USD.
     * This is normally the currency you intend to hold a long position in.
     */
    private String emergencyStopCurrency;

    /*
     * The Emergency Stop balance.
     * It is used to prevent a catastrophic loss on the exchange.
     * the exchange drops below this value, the Trading Engine will stop trading on all markets.
     * Manual intervention is then required to restart the bot.
     */
    private BigDecimal emergencyStopBalance;

    private String botId;
    private String botName;

    private final EmailAlerter emailAlerter;
    private ExchangeAdapter exchangeAdapter;


    // Services
    private final ExchangeConfigService exchangeConfigService;
    private final EngineConfigService engineConfigService;
    private final StrategyConfigService strategyConfigService;
    private final MarketConfigService marketConfigService;

    @Autowired
    public TradingEngine(ExchangeConfigService exchangeConfigService, EngineConfigService engineConfigService,
                         StrategyConfigService strategyConfigService, MarketConfigService marketConfigService,
                         EmailAlerter emailAlerter) {

        LOG.info(() -> "Creating instance of Trading Engine...");
        this.logEntryList = new ArrayList<LogEntryImpl>();
        this.exchangeConfigService = exchangeConfigService;
        this.engineConfigService = engineConfigService;
        this.strategyConfigService = strategyConfigService;
        this.marketConfigService = marketConfigService;
        this.emailAlerter = emailAlerter;
    }

    public void start() throws IllegalStateException {

        synchronized (IS_RUNNING_MONITOR) {
            if (isRunning) {
                final String errorMsg = "Cannot start Trading Engine because it is already running!";
                LOG.error(errorMsg);
                logEntryList.add(new LogEntryImpl(errorMsg));
                throw new IllegalStateException(errorMsg);
            }

            // first time to start
            isRunning = true;
        }

        // store this so we can shutdown the engine later
        engineThread = Thread.currentThread();

        new Thread(this::runMainControlLoop).start();
    }

    public void initConfig() {

        LOG.info(() -> "Initialising FleckBot config...");
        logEntryList.add(new LogEntryImpl("Initialising FleckBot config..."));

        // the sequence order of these methods is significant - don't change it.
        loadExchangeAdapterConfig();
        loadEngineConfig();
        loadTradingStrategyConfig();
        loadMarketConfigAndInitialiseTradingStrategies();
    }

    /*
     * The main control loop.
     * We loop infinitely unless an unexpected exception occurs.
     * The code fails hard and fast if an unexpected occurs. Network exceptions *should* recover.
     */
    private void runMainControlLoop() {

        LOG.info(() -> "Starting Trading Engine for " + botId + " ...");
        logEntryList.add(new LogEntryImpl("Starting Trading Engine for " + botId));

        while (keepAlive) {

            try {

                LOG.info(() -> "*** Starting next trade cycle... ***");
                logEntryList.add(new LogEntryImpl("*** Starting next trade cycle... ***"));

                // Emergency Stop Check MUST run at start of every trade cycle.
                if (isEmergencyStopLimitBreached()) {
                    break;
                }

                // Execute the Trading Strategies
                for (final TradingStrategy tradingStrategy : tradingStrategiesToExecute) {
                    LOG.info(() -> "Executing Trading Strategy ---> " + tradingStrategy.getClass().getSimpleName());
                    logEntryList.add(new LogEntryImpl("Executing Trading Strategy ---> " + tradingStrategy.getClass().getSimpleName()));
//                    tradingStrategy.execute();
                }

                LOG.info(() -> "*** Sleeping " + tradeExecutionInterval + "s til next trade cycle... ***");
                logEntryList.add(new LogEntryImpl("*** Sleeping " + tradeExecutionInterval + "s til next trade cycle... ***"));

                attemptToReconnectToExchange();

            } catch (ExchangeNetworkException e) {

                /*
                 * We have a network connection issue reported by Exchange Adapter when called directly from
                 * Trading Engine. Current policy is to log it and sleep until next trade cycle.
                 */
                final String WARNING_MSG = "A network error has occurred in Exchange Adapter! " +
                    "FleckBot will attempt next trade in " + tradeExecutionInterval + "s...";
                LOG.error(WARNING_MSG, e);
                logEntryList.add(new LogEntryImpl(WARNING_MSG));
                attemptToReconnectToExchange();

            } catch (TradingApiException e) {

                /*
                 * A serious issue has occurred in the Exchange Adapter.
                 * Current policy is to log it, send email alert if required, and shutdown bot.
                 */
                final String FATAL_ERROR_MSG = "A FATAL error has occurred in Exchange Adapter!";
                LOG.fatal(FATAL_ERROR_MSG, e);
                logEntryList.add(new LogEntryImpl(FATAL_ERROR_MSG));
                emailAlerter.sendMessage(CRITICAL_EMAIL_ALERT_SUBJECT,
                    buildCriticalEmailAlertMsgContent(FATAL_ERROR_MSG +
                        DETAILS_ERROR_MSG_LABEL + e.getMessage() +
                        CAUSE_ERROR_MSG_LABEL + e.getCause(), e));
                keepAlive = false;
//                      } catch (StrategyException e) {
//
//                        /*
//                         * A serious issue has occurred in the Trading Strategy.
//                         * Current policy is to log it, send email alert if required, and shutdown bot.
//                         */
//                        final String FATAL_ERROR_MSG = "A FATAL error has occurred in Trading Strategy!";
//                        LOG.fatal(FATAL_ERROR_MSG, e);
//                        emailAlerter.sendMessage(CRITICAL_EMAIL_ALERT_SUBJECT,
//                            buildCriticalEmailAlertMsgContent(FATAL_ERROR_MSG +
//                                DETAILS_ERROR_MSG_LABEL + e.getMessage() +
//                                CAUSE_ERROR_MSG_LABEL + e.getCause(), e));
//                        keepAlive = false;
            } catch (Exception e) {

                /*
                 * A serious and *unexpected* issue has occurred in the Exchange Adapter or Trading Strategy.
                 * Current policy is to log it, send email alert if required, and shutdown bot.
                 */
                final String FATAL_ERROR_MSG = "An unexpected FATAL error has occurred in Exchange Adapter or Trading Strategy!";
                LOG.fatal(FATAL_ERROR_MSG, e);
                logEntryList.add(new LogEntryImpl(FATAL_ERROR_MSG));
                emailAlerter.sendMessage(CRITICAL_EMAIL_ALERT_SUBJECT,
                    buildCriticalEmailAlertMsgContent(FATAL_ERROR_MSG +
                        DETAILS_ERROR_MSG_LABEL + e.getMessage() +
                        CAUSE_ERROR_MSG_LABEL + e.getCause(), e));
                keepAlive = false;
            }
        }

        LOG.fatal( botId + " is saying good bye!");
        logEntryList.add(new LogEntryImpl(botId + " is saying good bye!"));
        synchronized (IS_RUNNING_MONITOR) {
            isRunning = false;
        }
    }

    private void attemptToReconnectToExchange() {
        try {
            Thread.sleep(tradeExecutionInterval * 1000);
        } catch (InterruptedException e1) {
            LOG.warn("Control Loop thread interrupted when sleeping before next trade cycle");
            logEntryList.add(new LogEntryImpl("Control Loop thread interrupted when sleeping before next trade cycle"));
            Thread.currentThread().interrupt();
        }
    }

    /*
     * Shutdown the Trading Engine.
     * Might be called from a different thread.
     */
    public void shutdown() {

        LOG.info(() -> "Shutting down...");
        logEntryList.add(new LogEntryImpl("Shutting down..."));
        LOG.info(() -> "Engine originally started in thread: " + engineThread);
        logEntryList.add(new LogEntryImpl("Engine originally started in thread: " + engineThread));

        keepAlive = false;
        isRunning = false;
        engineThread.interrupt(); // poke it in case bot is sleeping
    }

    //  public synchronized boolean isRunning() {
    public boolean isRunning() {
        LOG.info(() -> "isRunning: " + isRunning);
        return isRunning;
    }

    /*
     * Checks if the Emergency Stop Currency (e.g. USD, BTC) wallet balance on exchange has gone *below* configured limit.
     * If the balance cannot be obtained or has dropped below the configured limit, we notify the main control loop to
     * immediately shutdown the bot.
     *
     * This check is here to help protect runaway losses due to:
     * - 'buggy' Trading Strategies
     * - Unforeseen bugs in the Trading Engine and Exchange Adapter
     * - the exchange sending corrupt order book data and the Trading Strategy being misled... this has happened.
     */
    private boolean isEmergencyStopLimitBreached() throws TradingApiException, ExchangeNetworkException {

        boolean isEmergencyStopLimitBreached = true;

        if (emergencyStopBalance.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }

        LOG.info(() -> "Performing Emergency Stop check...");
        logEntryList.add(new LogEntryImpl("Performing Emergency Stop check..."));

        BalanceInfo balanceInfo;
        try {
            balanceInfo = exchangeAdapter.getBalanceInfo();
        } catch (TradingApiException e) {
            final String errorMsg = "Failed to get Balance info from exchange to perform Emergency Stop check - letting"
                + " Trade Engine error policy decide what to do next...";
            LOG.error(errorMsg, e);
            logEntryList.add(new LogEntryImpl(errorMsg));
            // re-throw to main loop - might only be connection issue and it will retry...
            throw e;
        }

        final Map<String, BigDecimal> balancesAvailable = balanceInfo.getBalancesAvailable();
        final BigDecimal currentBalance = balancesAvailable.get(emergencyStopCurrency);
        if (currentBalance == null) {
            final String errorMsg =
                "Emergency stop check: Failed to get current Emergency Stop Currency balance as '"
                    + emergencyStopCurrency + "' key into Balances map "
                    + "returned null. Balances returned: " + balancesAvailable;
            LOG.error(errorMsg);
            logEntryList.add(new LogEntryImpl(errorMsg));
            throw new IllegalStateException(errorMsg);
        } else {

            LOG.info(() -> "Emergency Stop Currency balance available on exchange is ["
                + new DecimalFormat("#.########").format(currentBalance) + "] "
                + emergencyStopCurrency);
            logEntryList.add(new LogEntryImpl("Emergency Stop Currency balance available on exchange is ["
                + new DecimalFormat("#.########").format(currentBalance) + "] "
                + emergencyStopCurrency));

            LOG.info(() -> "Balance that will stop ALL trading across ALL markets is ["
                + new DecimalFormat("#.########").format(emergencyStopBalance) + "] " + emergencyStopCurrency);
            logEntryList.add(new LogEntryImpl("Balance that will stop ALL trading across ALL markets is ["
                + new DecimalFormat("#.########").format(emergencyStopBalance) + "] " + emergencyStopCurrency));


            if (currentBalance.compareTo(emergencyStopBalance) < 0) {
                final String balanceBlownErrorMsg =
                    "EMERGENCY STOP triggered! - Current Emergency Stop Currency [" + emergencyStopCurrency + "] wallet balance ["
                        + new DecimalFormat("#.########").format(currentBalance) + "] on exchange "
                        + "is lower than configured Emergency Stop balance ["
                        + new DecimalFormat("#.########").format(emergencyStopBalance) + "] " + emergencyStopCurrency;

                LOG.fatal(balanceBlownErrorMsg);
                logEntryList.add(new LogEntryImpl(balanceBlownErrorMsg));
                emailAlerter.sendMessage(CRITICAL_EMAIL_ALERT_SUBJECT,
                    buildCriticalEmailAlertMsgContent(balanceBlownErrorMsg, null));
            } else {

                isEmergencyStopLimitBreached = false;
                LOG.info(() -> "Emergency Stop check PASSED!");
                logEntryList.add(new LogEntryImpl("Emergency Stop check PASSED!"));
            }
        }
        return isEmergencyStopLimitBreached;
    }

    private String buildCriticalEmailAlertMsgContent(String errorDetails, Throwable exception) {

        final StringBuilder msgContent = new StringBuilder("A CRITICAL error event has occurred on FleckBot.");
        msgContent.append(NEWLINE).append(NEWLINE);

        msgContent.append(HORIZONTAL_RULE);
        msgContent.append("Bot Id / Name:");
        msgContent.append(NEWLINE).append(NEWLINE);
        msgContent.append(botId);
        msgContent.append(" / ");
        msgContent.append(botName);
        msgContent.append(NEWLINE).append(NEWLINE);

        msgContent.append(HORIZONTAL_RULE);
        msgContent.append("Exchange Adapter:");
        msgContent.append(NEWLINE).append(NEWLINE);
        msgContent.append(exchangeAdapter.getClass().getName());
        msgContent.append(NEWLINE).append(NEWLINE);

        msgContent.append(HORIZONTAL_RULE);
        msgContent.append("Event Time:");
        msgContent.append(NEWLINE).append(NEWLINE);
        msgContent.append(new Date());
        msgContent.append(NEWLINE).append(NEWLINE);

        msgContent.append(HORIZONTAL_RULE);
        msgContent.append("Event Details:");
        msgContent.append(NEWLINE).append(NEWLINE);
        msgContent.append(errorDetails);
        msgContent.append(NEWLINE).append(NEWLINE);

        msgContent.append(HORIZONTAL_RULE);
        msgContent.append("Action Taken:");
        msgContent.append(NEWLINE).append(NEWLINE);
        msgContent.append("The bot will shut down NOW! Check the bot logs for more information.");
        msgContent.append(NEWLINE).append(NEWLINE);

        if (exception != null) {
            msgContent.append(HORIZONTAL_RULE);
            msgContent.append("Stacktrace:");
            msgContent.append(NEWLINE).append(NEWLINE);
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);
            exception.printStackTrace(printWriter);
            msgContent.append(stringWriter.toString());
        }

        return msgContent.toString();
    }

    // ------------------------------------------------------------------------
    // Config loading methods
    // ------------------------------------------------------------------------

    private void loadExchangeAdapterConfig() {

        final ExchangeConfig domainExchangeConfig = exchangeConfigService.getExchangeConfig();
        LOG.info(() -> "Fetched Exchange config from repository: " + domainExchangeConfig);

        exchangeAdapter = ConfigurableComponentFactory.createComponent(domainExchangeConfig.getExchangeAdapter());
        LOG.info(() -> "Trading Engine will use Exchange Adapter for: " + exchangeAdapter.getImplName());

        final ExchangeConfigImpl adapterExchangeConfig = new ExchangeConfigImpl();

        // Fetch optional network config
        final NetworkConfig networkConfig = domainExchangeConfig.getNetworkConfig();
        if (networkConfig != null) {

            final NetworkConfigImpl adapterNetworkConfig = new NetworkConfigImpl();
            adapterNetworkConfig.setConnectionTimeout(networkConfig.getConnectionTimeout());

            // Grab optional non-fatal error codes
            final List<Integer> nonFatalErrorCodes = networkConfig.getNonFatalErrorCodes();
            if (nonFatalErrorCodes != null) {
                adapterNetworkConfig.setNonFatalErrorCodes(nonFatalErrorCodes);
            } else {
                LOG.info(() ->
                    "No (optional) NetworkConfiguration NonFatalErrorCodes have been set for Exchange Adapter: "
                        + exchangeAdapter.getImplName());
            }

            // Grab optional non-fatal error messages
            final List<String> nonFatalErrorMessages = networkConfig.getNonFatalErrorMessages();
            if (nonFatalErrorMessages != null) {
                adapterNetworkConfig.setNonFatalErrorMessages(nonFatalErrorMessages);
            } else {
                LOG.info(() ->
                    "No (optional) NetworkConfiguration NonFatalErrorMessages have been set for Exchange Adapter: "
                        + exchangeAdapter.getImplName());
            }

            adapterExchangeConfig.setNetworkConfig(adapterNetworkConfig);
            LOG.info(() -> "NetworkConfiguration has been set: " + adapterNetworkConfig);

        } else {
            LOG.info(() -> "No (optional) NetworkConfiguration has been set for Exchange Adapter: " + exchangeAdapter.getImplName());
        }

        // Fetch optional authentication config
        final AuthenticationConfig authenticationConfig = domainExchangeConfig.getAuthenticationConfig();
        if (authenticationConfig != null) {

            final AuthenticationConfigImpl adapterAuthenticationConfig = new AuthenticationConfigImpl();
            adapterAuthenticationConfig.setItems(authenticationConfig.getItems());
            adapterExchangeConfig.setAuthenticationConfig(adapterAuthenticationConfig);

            // WARNING - careful when you log this
            LOG.info(() -> "AuthenticationConfiguration has been set: " + adapterAuthenticationConfig);

        } else {
            LOG.info(() -> "No (optional) AuthenticationConfiguration has been set for Exchange Adapter: " + exchangeAdapter.getImplName());
        }

        // Fetch optional config
        final OptionalConfig optionalConfig = domainExchangeConfig.getOptionalConfig();
        if (optionalConfig != null) {

            final OptionalConfigImpl adapterOptionalConfig = new OptionalConfigImpl();
            adapterOptionalConfig.setItems(optionalConfig.getItems());
            adapterExchangeConfig.setOptionalConfig(adapterOptionalConfig);
            LOG.info(() -> "Optional Exchange Adapter config has been set: " + adapterOptionalConfig);

        } else {
            LOG.info(() -> "No Optional config has been set for Exchange Adapter: " + exchangeAdapter.getImplName());
        }

        exchangeAdapter.init(adapterExchangeConfig);
    }

    public ExchangeAdapter getExchangeAdapter(){
        return this.exchangeAdapter;
    }

    private void loadEngineConfig() {

        final EngineConfig engineConfig = engineConfigService.getEngineConfig();
        LOG.info(() -> "Fetched Engine config from repository: " + engineConfig);

        botId = engineConfig.getBotId();
        botName = engineConfig.getBotName();

        tradeExecutionInterval = engineConfig.getTradeCycleInterval();
        emergencyStopCurrency = engineConfig.getEmergencyStopCurrency();
        emergencyStopBalance = engineConfig.getEmergencyStopBalance();
    }

    private void loadTradingStrategyConfig() {

        final List<StrategyConfig> strategies = strategyConfigService.getAllStrategyConfig();
        LOG.debug(() -> "Fetched Strategy config from repository: " + strategies);

        for (final StrategyConfig strategy : strategies) {
            strategyDescriptions.put(strategy.getId(), strategy);
            LOG.info(() -> "Registered Trading Strategy with Trading Engine - ID: " + strategy.getId());
        }
    }

    private void loadMarketConfigAndInitialiseTradingStrategies() {

        final List<MarketConfig> markets = marketConfigService.getAllMarketConfig();
        LOG.info(() -> "Fetched Markets config from repository: " + markets);

        // used only as crude mechanism for checking for duplicate Markets
        final Set<Market> loadedMarkets = new HashSet<>();

        // Load em up and create the Strategies
        for (final MarketConfig market : markets) {

            final String marketName = market.getName();
            if (!market.isEnabled()) {
                LOG.info(() -> marketName + " market is NOT enabled for trading - skipping to next market...");
                continue;
            }

            final Market tradingMarket = new MarketImpl(marketName, market.getId(), market.getBaseCurrency(), market.getCounterCurrency());
            final boolean wasAdded = loadedMarkets.add(tradingMarket);
            if (!wasAdded) {
                final String errorMsg = "Found duplicate Market! Market details: " + market;
                LOG.fatal(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            // Get the strategy to use for this Market
            final String strategyToUse = market.getTradingStrategyId();
            LOG.info(() -> "Market Trading Strategy Id: " + strategyToUse);

            if (strategyDescriptions.containsKey(strategyToUse)) {
                final StrategyConfig tradingStrategy = strategyDescriptions.get(strategyToUse);
                final String tradingStrategyClassname = tradingStrategy.getClassName();

                // Grab optional config for the Trading Strategy
                final StrategyConfigItems tradingStrategyConfig = new StrategyConfigItems();
                final Map<String, String> configItems = tradingStrategy.getConfigItems();
                if (configItems != null) {
                    tradingStrategyConfig.setItems(configItems);
                } else {
                    LOG.info(() -> "No (optional) configuration has been set for Trading Strategy: " + strategyToUse);
                }

                LOG.info(() -> "StrategyConfigImpl (optional): " + tradingStrategyConfig);

                /*
                 * Load the Trading Strategy impl, instantiate it, set its config, and store in the cached
                 * Trading Strategy execution list.
                 */
                final TradingStrategy strategyImpl = ConfigurableComponentFactory.createComponent(tradingStrategyClassname);
                strategyImpl.init(exchangeAdapter, tradingMarket, tradingStrategyConfig);

                LOG.info(() -> "Finished loading trading strategy successfully. Name: [" + tradingStrategy.getName()
                    + "] Class: " + tradingStrategy.getClassName());

                tradingStrategiesToExecute.add(strategyImpl);
            } else {

                // Game over. Config integrity blown - we can't find strat.
                final String errorMsg = "Failed to find matching Strategy for Market " + market
                    + " - The Strategy " + "[" + strategyToUse + "] cannot be found in the "
                    + " Strategy Descriptions map: " + strategyDescriptions;
                LOG.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        }

        LOG.info(() -> "Loaded and set Market configuration successfully!");
    }
}