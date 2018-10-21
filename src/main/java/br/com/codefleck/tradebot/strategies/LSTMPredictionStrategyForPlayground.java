package br.com.codefleck.tradebot.strategies;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ta4j.core.*;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import br.com.codefleck.tradebot.models.exchange.OrderState;
import br.com.codefleck.tradebot.strategyapi.StrategyException;
import br.com.codefleck.tradebot.tradingInterfaces.*;

/**
 * @author Daniel Fleck
 */
public class LSTMPredictionStrategyForPlayground {

    final Logger LOG = LogManager.getLogger();

//    public static Strategy buildStrategy(TimeSeries series) {
//
//        final MarketOrderBook orderBook = tradingApi.getMarketOrders(market.getId());
//
//        return new BaseStrategy(entryRule, exitRule);
//
//    }
//
//    public void execute() throws StrategyException {
//
//
//
//        try {
//            // Grab the latest order book for the market.
//            final MarketOrderBook orderBook = tradingApi.getMarketOrders(market.getId());
//
//            final List<MarketOrder> buyOrders = orderBook.getBuyOrders();
//            if (buyOrders.size() == 0) {
//                LOG.warn("Exchange returned empty Buy Orders. Ignoring this trade window. OrderBook: " + orderBook);
//                return;
//            }
//
//            final List<MarketOrder> sellOrders = orderBook.getSellOrders();
//            if (sellOrders.size() == 0) {
//                LOG.warn("Exchange returned empty Sell Orders. Ignoring this trade window. OrderBook: " + orderBook);
//                return;
//            }
//
//            // Get the current BID and ASK spot prices.
//            final BigDecimal currentBidPrice = buyOrders.get(0).getPrice();
//            final BigDecimal currentAskPrice = sellOrders.get(0).getPrice();
//
//            LOG.info(() -> market.getName() + " Current BID price=" + new DecimalFormat("#.########").format(currentBidPrice));
//            LOG.info(() -> market.getName() + " Current ASK price=" + new DecimalFormat("#.########").format(currentAskPrice));
//
//            /*
//             * Is this the first time the Strategy has been called? If yes, we initialise the OrderState so we can keep
//             * track of orders during later trace cycles.
//             */
//            if (lastOrder == null) {
//                LOG.info(() -> market.getName() + " First time Strategy has been called - creating new OrderState object.");
//                lastOrder = new OrderState();
//            }
//
//            // Always handy to log what the last order was during each trace cycle.
//            LOG.info(() -> market.getName() + " Last Order was: " + lastOrder);
//
//            /*
//             * Execute the appropriate algorithm based on the last order type.
//             */
//            if (lastOrder.type == OrderType.BUY) {
//                executeAlgoForWhenLastOrderWasBuy();
//
//            } else if (lastOrder.type == OrderType.SELL) {
//                executeAlgoForWhenLastOrderWasSell(currentBidPrice, currentAskPrice);
//
//            } else if (lastOrder.type == null) {
//                executeAlgoForWhenLastOrderWasNone(currentBidPrice);
//            }
//
//        } catch (ExchangeNetworkException e) {
//            // Your timeout handling code could go here.
//            // We are just going to log it and swallow it, and wait for next trade cycle.
//            LOG.error(market.getName() + " Failed to get market orders because Exchange threw network exception. " + "Waiting until next trade cycle.", e);
//
//        } catch (TradingApiException e) {
//            // Your error handling code could go here...
//            // We are just going to re-throw as StrategyException for engine to deal with - it will shutdown the bot.
//            LOG.error(market.getName() + " Failed to get market orders because Exchange threw TradingApi exception. " + " Telling Trading Engine to shutdown bot!", e);
//            throw new StrategyException(e);
//        }
//    }
}
