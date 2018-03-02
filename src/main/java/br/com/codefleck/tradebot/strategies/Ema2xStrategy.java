package br.com.codefleck.tradebot.strategies;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ta4j.core.*;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.strategyapi.StrategyConfig;
import br.com.codefleck.tradebot.strategyapi.StrategyException;
import br.com.codefleck.tradebot.strategyapi.TradingStrategy;
import br.com.codefleck.tradebot.tradingapi.*;

@SuppressWarnings("ALL")
public class Ema2xStrategy implements TradingStrategy {

    private static final Logger LOG = LogManager.getLogger();

    /**
     * Reference to the main Trading API.
     */
    private TradingApi tradingApi;

    /**
     * The market this strategy is trading on.
     */
    private Market market;

    /**
     * The state of the order.
     */
    private OrderState lastOrder;

    /**
     * The counter currency amount to use when placing the buy order.
     * This was loaded from the strategy entry in the {project-root}/config/strategies.xml config file.
     */
    private BigDecimal counterCurrencyBuyOrderAmount;

    /**
     * The minimum % gain was to achieve before placing a SELL oder.
     * This was loaded from the strategy entry in the {project-root}/config/strategies.xml config file.
     */
    private BigDecimal minimumPercentageGain;


    /**
     * Initialises the Trading Strategy.
     * Called once by the Trading Engine when the bot starts up; it's a bit like a servlet init() method.
     *
     * @param tradingApi the Trading API. Use this to make trades and stuff.
     * @param market     the market for this strategy. This is the market the strategy is currently running on - you wire
     *                   this up in the markets.xml and strategies.xml files.
     * @param config     configuration for the strategy. Contains any (optional) config you set up in the
     *                   strategies.xml file.
     */
    @Override
    public void init(TradingApi tradingApi, Market market, StrategyConfig config) {

        LOG.info(() -> "Loading Trading Strategy...");

        this.tradingApi = tradingApi;
        this.market = market;
        getConfigForStrategy(config);

        LOG.info(() -> "Trading Strategy loaded successfully!");
    }

    /**
     * <p>
     * This is the main execution method of the Trading Strategy. It is where your algorithm lives.
     * </p>
     * <p>
     * It is called by the Trading Engine during each trade cycle, e.g. every 60s. The trade cycle is configured in
     * the {project-root}/config/engine.xml file.
     * </p>
     *
     * @throws StrategyException if something unexpected occurs. This tells the Trading Engine to shutdown the bot
     *                           immediately to help prevent unexpected losses.
     */
    @Override
    public void execute() throws StrategyException {

        LOG.info(() -> market.getName() + " Checking order status...");

        try {
            // Grab the latest order book for the market.
            final MarketOrderBook orderBook = tradingApi.getMarketOrders(market.getId());

            final List<MarketOrder> buyOrders = orderBook.getBuyOrders();
            if (buyOrders.size() == 0) {
                LOG.warn("Exchange returned empty Buy Orders. Ignoring this trade window. OrderBook: " + orderBook);
                return;
            }

            final List<MarketOrder> sellOrders = orderBook.getSellOrders();
            if (sellOrders.size() == 0) {
                LOG.warn("Exchange returned empty Sell Orders. Ignoring this trade window. OrderBook: " + orderBook);
                return;
            }

            // Get the current BID and ASK spot prices.
            final BigDecimal currentBidPrice = buyOrders.get(0).getPrice();
            final BigDecimal currentAskPrice = sellOrders.get(0).getPrice();

            LOG.info(() -> market.getName() + " Current BID price=" +
                new DecimalFormat("#.########").format(currentBidPrice));
            LOG.info(() -> market.getName() + " Current ASK price=" +
                new DecimalFormat("#.########").format(currentAskPrice));

            /*
             * Is this the first time the Strategy has been called? If yes, we initialise the OrderState so we can keep
             * track of orders during later trace cycles.
             */
            if (lastOrder == null) {
                LOG.info(() -> market.getName() +
                    " First time Strategy has been called - creating new OrderState object.");
                lastOrder = new OrderState();
            }

            // Always handy to log what the last order was during each trace cycle.
            LOG.info(() -> market.getName() + " Last Order was: " + lastOrder);

            /*
             * Execute the appropriate algorithm based on the last order type.
             */
            if (lastOrder.type == OrderType.BUY) {
                executeAlgoForWhenLastOrderWasBuy();

            } else if (lastOrder.type == OrderType.SELL) {
                executeAlgoForWhenLastOrderWasSell(currentBidPrice, currentAskPrice);

            } else if (lastOrder.type == null) {
                executeAlgoForWhenLastOrderWasNone(currentBidPrice);
            }

        } catch (ExchangeNetworkException e) {
            // Your timeout handling code could go here.
            // We are just going to log it and swallow it, and wait for next trade cycle.
            LOG.error(market.getName() + " Failed to get market orders because Exchange threw network exception. " +
                "Waiting until next trade cycle.", e);

        } catch (TradingApiException e) {
            // Your error handling code could go here...
            // We are just going to re-throw as StrategyException for engine to deal with - it will shutdown the bot.
            LOG.error(market.getName() + " Failed to get market orders because Exchange threw TradingApi exception. " +
                " Telling Trading Engine to shutdown bot!", e);
            throw new StrategyException(e);
        }
    }

    /**
     * Algo for executing when the Trading Strategy is invoked for the first time.
     * We start off with a buy order at current BID price.
     *
     * @param currentBidPrice the current market BID price.
     * @throws StrategyException if an unexpected exception is received from the Exchange Adapter.
     *                           Throwing this exception indicates we want the Trading Engine to shutdown the bot.
     */
    private void executeAlgoForWhenLastOrderWasNone(BigDecimal currentBidPrice) throws StrategyException {

        LOG.info(() -> market.getName() + " OrderType is NONE - placing new BUY order at ["
            + new DecimalFormat("#.########").format(currentBidPrice) + "]");

        try {

            // Calculate the amount of base currency (BTC) to buy for given amount of counter currency (USD).
            final BigDecimal amountOfBaseCurrencyToBuy =
                getAmountOfBaseCurrencyToBuyForGivenCounterCurrencyAmount(counterCurrencyBuyOrderAmount);

            // Send the order to the exchange
            LOG.info(() -> market.getName() + " Sending initial BUY order to exchange --->");

            lastOrder.id = tradingApi.createOrder(market.getId(), OrderType.BUY, amountOfBaseCurrencyToBuy, currentBidPrice);

            LOG.info(() -> market.getName() + " Initial BUY Order sent successfully. ID: " + lastOrder.id);

            // update last order details
            lastOrder.price = currentBidPrice;
            lastOrder.type = OrderType.BUY;
            lastOrder.amount = amountOfBaseCurrencyToBuy;

        } catch (ExchangeNetworkException e) {
            // Your timeout handling code could go here, e.g. you might want to check if the order actually
            // made it to the exchange? And if not, resend it...
            // We are just going to log it and swallow it, and wait for next trade cycle.
            LOG.error(market.getName() + " Initial order to BUY base currency failed because Exchange threw network exception. " +
                "Waiting until next trade cycle.", e);

        } catch (TradingApiException e) {
            // Your error handling code could go here...
            // We are just going to re-throw as StrategyException for engine to deal with - it will shutdown the bot.
            LOG.error(market.getName() + " Initial order to BUY base currency failed because Exchange threw TradingApi exception. " +
                " Telling Trading Engine to shutdown bot!", e);
            throw new StrategyException(e);
        }
    }

    /**
     * <p>
     * Algo for executing when last order we placed on the exchanges was a BUY.
     * </p>
     * <p>
     * If last buy order filled, we try and sell at a profit.
     * </p>
     *
     * @throws StrategyException if an unexpected exception is received from the Exchange Adapter.
     *                           Throwing this exception indicates we want the Trading Engine to shutdown the bot.
     */
    private void executeAlgoForWhenLastOrderWasBuy() throws StrategyException {

        try {

            // Fetch our current open orders and see if the buy order is still outstanding/open on the exchange
            final List<OpenOrder> myOrders = tradingApi.getYourOpenOrders(market.getId());
            boolean lastOrderFound = false;
            for (final OpenOrder myOrder : myOrders) {
                if (myOrder.getId().equals(lastOrder.id)) {
                    lastOrderFound = true;
                    break;
                }
            }

            // If the order is not there, it must have all filled.
            if (!lastOrderFound) {

                LOG.info(() -> market.getName() +
                    "Yesss! Last BUY Order Id [" + lastOrder.id + "] filled at [" + lastOrder.price + "]");

                /*
                 * The last buy order was filled, so lets see if we can send a new sell order.
                 *
                 * IMPORTANT - new sell order ASK price must be > (last order price + exchange fees) because:
                 *
                 * 1. If we put sell amount in as same amount as previous buy, the exchange barfs because we don't have
                 *    enough units to cover the transaction fee.
                 * 2. We could end up selling at a loss.
                 *
                 * For this example strategy, we're just going to add 2% (taken from the 'minimum-percentage-gain'
                 * config item in the {project-root}/config/strategies.xml config file) on top of previous bid price
                 * to make a little profit and cover the exchange fees.
                 *
                 * Your algo will have other ideas on how much profit to make and when to apply the exchange fees - you
                 * could try calling the TradingApi#getPercentageOfBuyOrderTakenForExchangeFee() and
                 * TradingApi#getPercentageOfSellOrderTakenForExchangeFee() when calculating the order to send to the
                 * exchange...
                 */
                LOG.info(() -> market.getName() + " Percentage profit (in decimal) to make for the sell order is: "
                    + minimumPercentageGain);

                final BigDecimal amountToAdd = lastOrder.price.multiply(minimumPercentageGain);
                LOG.info(() -> market.getName() + " Amount to add to last buy order fill price: " + amountToAdd);

                /*
                 * Most exchanges (if not all) use 8 decimal places.
                 * It's usually best to round up the ASK price in your calculations to maximise gains.
                 */
                final BigDecimal newAskPrice = lastOrder.price.add(amountToAdd).setScale(8, RoundingMode.HALF_UP);
                LOG.info(() -> market.getName() + " Placing new SELL order at ask price [" +
                    new DecimalFormat("#.########").format(newAskPrice) + "]");

                LOG.info(() -> market.getName() + " Sending new SELL order to exchange --->");

                // Build the new sell order
                lastOrder.id = tradingApi.createOrder(market.getId(), OrderType.SELL, lastOrder.amount, newAskPrice);
                LOG.info(() -> market.getName() + " New SELL Order sent successfully. ID: " + lastOrder.id);

                // update last order state
                lastOrder.price = newAskPrice;
                lastOrder.type = OrderType.SELL;
            } else {

                /*
                 * BUY order has not filled yet.
                 * Could be nobody has jumped on it yet... or the order is only part filled... or market has gone up and
                 * we've been outbid and have a stuck buy order. In which case, we have to wait for the market to
                 * fall for the order to fill... or you could tweak this code to cancel the current order and raise your bid -
                 * remember to deal with any part-filled orders!
                 */
                LOG.info(() -> market.getName() + " !!! Still have BUY Order " + lastOrder.id
                    + " waiting to fill at [" + lastOrder.price + "] - holding last BUY order...");
            }

        } catch (ExchangeNetworkException e) {
            // Your timeout handling code could go here, e.g. you might want to check if the order actually
            // made it to the exchange? And if not, resend it...
            // We are just going to log it and swallow it, and wait for next trade cycle.
            LOG.error(market.getName() + " New Order to SELL base currency failed because Exchange threw network exception. " +
                "Waiting until next trade cycle. Last Order: " + lastOrder, e);

        } catch (TradingApiException e) {
            // Your error handling code could go here...
            // We are just going to re-throw as StrategyException for engine to deal with - it will shutdown the bot.
            LOG.error(market.getName() + " New order to SELL base currency failed because Exchange threw TradingApi exception. " +
                " Telling Trading Engine to shutdown bot! Last Order: " + lastOrder, e);
            throw new StrategyException(e);
        }
    }

    /**
     * <p>
     * Algo for executing when last order we placed on the exchange was a SELL.
     * </p>
     * <p>
     * If last sell order filled, we send a new buy order to the exchange.
     * </p>
     *
     * @param currentBidPrice the current market BID price.
     * @param currentAskPrice the current market ASK price.
     * @throws StrategyException if an unexpected exception is received from the Exchange Adapter.
     *                           Throwing this exception indicates we want the Trading Engine to shutdown the bot.
     */
    private void executeAlgoForWhenLastOrderWasSell(BigDecimal currentBidPrice, BigDecimal currentAskPrice)
        throws StrategyException {

        try {

            // Fetch our current open orders and see if the sell order is still outstanding/unfilled on the exchange
            final List<OpenOrder> myOrders = tradingApi.getYourOpenOrders(market.getId());
            boolean lastOrderFound = false;
            for (final OpenOrder myOrder : myOrders) {
                if (myOrder.getId().equals(lastOrder.id)) {
                    lastOrderFound = true;
                    break;
                }
            }

            // if the order is not there, it must have all filled.
            if (!lastOrderFound) {

                LOG.info(() -> market.getName() +
                    " Yesss! Last SELL Order Id [" + lastOrder.id + "] filled at [" + lastOrder.price + "]");

                // Get amount of base currency (BTC) we can buy for given counter currency (USD) amount.
                final BigDecimal amountOfBaseCurrencyToBuy =
                    getAmountOfBaseCurrencyToBuyForGivenCounterCurrencyAmount(counterCurrencyBuyOrderAmount);

                LOG.info(() -> market.getName() + " Placing new BUY order at bid price [" +
                    new DecimalFormat("#.########").format(currentBidPrice) + "]");

                LOG.info(() -> market.getName() + " Sending new BUY order to exchange --->");

                // Send the buy order to the exchange.
                lastOrder.id = tradingApi.createOrder(market.getId(), OrderType.BUY, amountOfBaseCurrencyToBuy, currentBidPrice);
                LOG.info(() -> market.getName() + " New BUY Order sent successfully. ID: " + lastOrder.id);

                // update last order details
                lastOrder.price = currentBidPrice;
                lastOrder.type = OrderType.BUY;
                lastOrder.amount = amountOfBaseCurrencyToBuy;
            } else {

                /*
                 * SELL order not filled yet.
                 * Could be nobody has jumped on it yet... or the order is only part filled... or market has gone down
                 * and we've been undercut and have a stuck sell order. In which case, we have to wait for market to
                 * recover for the order to fill... or you could tweak this code to cancel the current order and lower your ask
                 * - remember to deal with any part-filled orders!
                 */
                if (currentAskPrice.compareTo(lastOrder.price) < 0) {
                    LOG.info(() -> market.getName() + " <<< Current ask price [" + currentAskPrice
                        + "] is LOWER then last order price ["
                        + lastOrder.price + "] - holding last SELL order...");

                } else if (currentAskPrice.compareTo(lastOrder.price) > 0) {
                    LOG.error(market.getName() + " >>> Current ask price [" + currentAskPrice
                        + "] is HIGHER than last order price ["
                        + lastOrder.price + "] - IMPOSSIBLE! FleckBot must have sold?????");

                } else if (currentAskPrice.compareTo(lastOrder.price) == 0) {
                    LOG.info(() -> market.getName() + " === Current ask price [" + currentAskPrice
                        + "] is EQUAL to last order price ["
                        + lastOrder.price + "] - holding last SELL order...");
                }
            }
        } catch (ExchangeNetworkException e) {
            // Your timeout handling code could go here, e.g. you might want to check if the order actually
            // made it to the exchange? And if not, resend it...
            // We are just going to log it and swallow it, and wait for next trade cycle.
            LOG.error(market.getName() + " New Order to BUY base currency failed because Exchange threw network exception. " +
                "Waiting until next trade cycle. Last Order: " + lastOrder, e);

        } catch (TradingApiException e) {
            // Your error handling code could go here...
            // We are just going to re-throw as StrategyException for engine to deal with - it will shutdown the bot.
            LOG.error(market.getName() + " New order to BUY base currency failed because Exchange threw TradingApi exception. " +
                " Telling Trading Engine to shutdown bot! Last Order: " + lastOrder, e);
            throw new StrategyException(e);
        }
    }

    /**
     * Returns amount of base currency (BTC) to buy for a given amount of counter currency (USD) based on last
     * market trade price.
     *
     * @param amountOfCounterCurrencyToTrade the amount of counter currency (USD) we have to trade (buy) with.
     * @return the amount of base currency (BTC) we can buy for the given counter currency (USD) amount.
     * @throws TradingApiException      if an unexpected error occurred contacting the exchange.
     * @throws ExchangeNetworkException if a request to the exchange has timed out.
     */
    private BigDecimal getAmountOfBaseCurrencyToBuyForGivenCounterCurrencyAmount(BigDecimal amountOfCounterCurrencyToTrade) throws
        TradingApiException, ExchangeNetworkException {

        LOG.info(() -> market.getName() + " Calculating amount of base currency (BTC) to buy for amount of counter currency " +
            new DecimalFormat("#.########").format(amountOfCounterCurrencyToTrade) + " " + market.getCounterCurrency());

        // Fetch the last trade price
        final BigDecimal lastTradePriceInUsdForOneBtc = tradingApi.getLatestMarketPrice(market.getId());
        LOG.info(() -> market.getName() + " Last trade price for 1 " + market.getBaseCurrency() + " was: " +
            new DecimalFormat("#.########").format(lastTradePriceInUsdForOneBtc) + " " + market.getCounterCurrency());

        /*
         * Most exchanges (if not all) use 8 decimal places and typically round in favour of the exchange.
         * It's usually safest to round down the order quantity in your calculations.
         */
        final BigDecimal amountOfBaseCurrencyToBuy = amountOfCounterCurrencyToTrade.divide(
            lastTradePriceInUsdForOneBtc, 8, RoundingMode.HALF_DOWN);

        LOG.info(() -> market.getName() + " Amount of base currency (" + market.getBaseCurrency() + ") to BUY for "
            + new DecimalFormat("#.########").format(amountOfCounterCurrencyToTrade) +
            " " + market.getCounterCurrency() + " based on last market trade price: " + amountOfBaseCurrencyToBuy);

        return amountOfBaseCurrencyToBuy;
    }

    /**
     * Loads the config for the strategy. We expect the 'counter-currency-buy-order-amount' and 'minimum-percentage-gain'
     * config items to be present in the {project-root}/config/strategies.xml config file.
     *
     * @param config the config for the Trading Strategy.
     */
    private void getConfigForStrategy(StrategyConfig config) {

        // --------------------------------------------------------------------
        // counter currency buy amount
        // --------------------------------------------------------------------
        final String counterCurrencyBuyOrderAmountFromConfigAsString = config.getConfigItem("counter-currency-buy-order-amount");
        if (counterCurrencyBuyOrderAmountFromConfigAsString == null) {
            // game over - kill it off now.
            throw new IllegalArgumentException("Mandatory counter-currency-buy-order-amount value missing in strategy.xml config.");
        }
        LOG.info(() -> "<counter-currency-buy-order-amount> from config is: " + counterCurrencyBuyOrderAmountFromConfigAsString);

        // will fail fast if value is not a number!
        counterCurrencyBuyOrderAmount = new BigDecimal(counterCurrencyBuyOrderAmountFromConfigAsString);
        LOG.info(() -> "counterCurrencyBuyOrderAmount: " + counterCurrencyBuyOrderAmount);

        // --------------------------------------------------------------------
        // min % gain
        // --------------------------------------------------------------------
        final String minimumPercentageGainFromConfigAsString = config.getConfigItem("minimum-percentage-gain");
        if (minimumPercentageGainFromConfigAsString == null) {
            // game over - kill it off now.
            throw new IllegalArgumentException("Mandatory minimum-percentage-gain value missing in strategy.xml config.");
        }
        LOG.info(() -> "<minimum-percentage-gain> from config is: " + minimumPercentageGainFromConfigAsString);

        // will fail fast if value is not a number!
        final BigDecimal minimumPercentageGainFromConfig = new BigDecimal(minimumPercentageGainFromConfigAsString);
        minimumPercentageGain = minimumPercentageGainFromConfig.divide(new BigDecimal(100), 8, RoundingMode.HALF_UP);
        LOG.info(() -> "minimumPercentageGain in decimal is: " + minimumPercentageGain);
    }

    /**
     * <p>
     * Models the state of an Order we have placed on the exchange.
     * </p>
     * <p>
     * Typically, you would maintain order state in a database or use some other persistent datasource to recover from
     * restarts and for audit purposes. In this example, we are storing the state in memory to keep it simple.
     * </p>
     */
    private static class OrderState {

        /**
         * Id - default to null.
         */
        private String id = null;

        /**
         * Type: buy/sell. We default to null which means no order has been placed yet, i.e. we've just started!
         */
        private OrderType type = null;

        /**
         * Price to buy/sell at - default to zero.
         */
        private BigDecimal price = BigDecimal.ZERO;

        /**
         * Number of units to buy/sell - default to zero.
         */
        private BigDecimal amount = BigDecimal.ZERO;

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("price", price)
                .add("amount", amount)
                .toString();
        }
    }
}

