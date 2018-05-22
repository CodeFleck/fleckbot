package br.com.codefleck.tradebot.core.util;

import java.util.Objects;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Order.OrderType;

public class CustomTrade {

    /** The entry order */
    private Order entry;

    /** The exit order */
    private Order exit;

    /** The type of the entry order */
    private OrderType startingType;

    /** The profit achieved in the trade in USD */
    private Decimal tradeProfit;

    /** The percentage of profit achieved in the trade */
    private Decimal tradeProfitPercentage;

    /**
     * Constructor.
     */
    public CustomTrade() {
        this(OrderType.BUY);
    }

    /**
     * Constructor.
     * @param startingType the starting {@link OrderType order type} of the trade (i.e. type of the entry order)
     */
    public CustomTrade(OrderType startingType) {
        if (startingType == null) {
            throw new IllegalArgumentException("Starting type must not be null");
        }
        this.startingType = startingType;
    }

    /**
     * Constructor.
     * @param entry the entry {@link Order order}
     * @param exit the exit {@link Order order}
     */
    public CustomTrade(Order entry, Order exit) {
        if (entry.getType().equals(exit.getType())) {
            throw new IllegalArgumentException("Both orders must have different types");
        }
        this.startingType = entry.getType();
        this.entry = entry;
        this.exit = exit;
    }

    /**
     * @return the entry {@link Order order} of the trade
     */
    public Order getEntry() {
        return entry;
    }

    /**
     * @return the exit {@link Order order} of the trade
     */
    public Order getExit() {
        return exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entry, exit);
    }

    /**
     * @return true if the trade is closed, false otherwise
     */
    public boolean isClosed() {
        return (entry != null) && (exit != null);
    }

    /**
     * @return true if the trade is opened, false otherwise
     */
    public boolean isOpened() {
        return (entry != null) && (exit == null);
    }

    /**
     * @return true if the trade is new, false otherwise
     */
    public boolean isNew() {
        return (entry == null) && (exit == null);
    }

    public void setEntry(Order entry) {
        this.entry = entry;
    }

    public void setExit(Order exit) {
        this.exit = exit;
    }

    public OrderType getStartingType() {
        return startingType;
    }

    public void setStartingType(OrderType startingType) {
        this.startingType = startingType;
    }

    public Decimal getTradeProfit() {
        return tradeProfit;
    }

    public void setTradeProfit(Decimal tradeProfit) {
        this.tradeProfit = tradeProfit;
    }

    public Decimal getTradeProfitPercentage() { return tradeProfitPercentage; }

    public void setTradeProfitPercentage(Decimal tradeProfitPercentage) { this.tradeProfitPercentage = tradeProfitPercentage; }

    @Override
    public String toString() {
        return "CustomTrade{" + "entry=" + entry + ", exit=" + exit + ", startingType=" + startingType + ", tradeProfit=" + tradeProfit + ", tradeProfitPercentage=" + tradeProfitPercentage + '}';
    }
}

