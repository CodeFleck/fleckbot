package br.com.codefleck.tradebot.models.exchange;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.tradingInterfaces.OrderType;

@Entity
public class OrderState {

    /**
     * Id - default to null.
     */
    @Id
    public String id = null;

    /**
     * Type: buy/sell. We default to null which means no order has been placed yet, i.e. we've just started!
     */
    public OrderType type = null;

    /**
     * Price to buy/sell at - default to zero.
     */
    public BigDecimal price = BigDecimal.ZERO;

    /**
     * Number of units to buy/sell - default to zero.
     */
    public BigDecimal amount = BigDecimal.ZERO;

    public OrderState() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

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