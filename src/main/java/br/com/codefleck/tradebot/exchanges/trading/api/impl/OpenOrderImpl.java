package br.com.codefleck.tradebot.exchanges.trading.api.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import br.com.codefleck.tradebot.tradingapi.OpenOrder;
import br.com.codefleck.tradebot.tradingapi.OrderType;

/**
 * A Open Order implementation that can be used by Exchange Adapters.
 *
 */
@Entity
public final class OpenOrderImpl implements OpenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Date creationDate;
    private String marketId;
    private OrderType type;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal originalQuantity;
    private BigDecimal total;


    public OpenOrderImpl(String id, Date creationDate, String marketId, OrderType type,
                         BigDecimal price, BigDecimal quantity, BigDecimal originalQuantity,
                         BigDecimal total) {
        this.id = id;
        if (creationDate != null) {
            this.creationDate = new Date(creationDate.getTime());
        }
        this.marketId = marketId;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.originalQuantity = originalQuantity;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        if (creationDate != null) {
            return new Date(creationDate.getTime());
        }
        return null;
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate != null) {
            this.creationDate = new Date(creationDate.getTime());
        }
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(BigDecimal originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenOrderImpl openOrder = (OpenOrderImpl) o;
        return Objects.equal(id, openOrder.id) &&
            Objects.equal(marketId, openOrder.marketId) &&
            type == openOrder.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, marketId, type);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("creationDate", creationDate)
            .add("marketId", marketId)
            .add("type", type)
            .add("price", price)
            .add("quantity", quantity)
            .add("originalQuantity", originalQuantity)
            .add("total", total)
            .toString();
    }
}
