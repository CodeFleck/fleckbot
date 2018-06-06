package br.com.codefleck.tradebot.tradingInterfaces;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Defines the different order types for sending to the exchange.
 */
public enum OrderType {

    /**
     * Buy order.
     */
    BUY("Buy"),

    /**
     * Sell order.
     */
    SELL("Sell");

    private final String orderType;

    OrderType(String orderType) {
        this.orderType = orderType;
    }

    @Enumerated(EnumType.STRING)
    public String getStringValue() {
        return orderType;
    }
}