package br.com.codefleck.tradebot.exchanges.trading.api.impl;

import java.math.BigDecimal;

import javax.persistence.*;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.tradingapi.MarketOrder;
import br.com.codefleck.tradebot.tradingapi.OrderType;

/**
 * A Market Order implementation that can be used by Exchange Adapters.
 */
@Entity
public final class MarketOrderImpl implements MarketOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private OrderType type;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal total;

    public MarketOrderImpl(OrderType type, BigDecimal price, BigDecimal quantity, BigDecimal total) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    @Enumerated(EnumType.ORDINAL)
    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("type", type)
            .add("price", price)
            .add("quantity", quantity)
            .add("total", total)
            .toString();
    }
}