package br.com.codefleck.tradebot.exchanges.trading;

import java.util.List;

import javax.persistence.*;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.tradingInterfaces.MarketOrder;
import br.com.codefleck.tradebot.tradingInterfaces.MarketOrderBook;

/**
 * A Market Order Book implementation that can be used by Exchange Adapters.
 */
@Entity
public final class MarketOrderBookImpl implements MarketOrderBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String marketId;

    @OneToMany(targetEntity= MarketOrderImpl.class)
    @JoinTable(name="marketorderimpl")
    @MapKeyColumn(name="id")
    private List<MarketOrder> sellOrders;

    @OneToMany(targetEntity= MarketOrderImpl.class)
    @JoinTable(name="marketorderimpl")
    @MapKeyColumn(name="id")
    private List<MarketOrder> buyOrders;


    public MarketOrderBookImpl(String marketId, List<MarketOrder> sellOrders, List<MarketOrder> buyOrders) {
        this.marketId = marketId;
        this.sellOrders = sellOrders;
        this.buyOrders = buyOrders;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public List<MarketOrder> getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(List<MarketOrder> sellOrders) {
        this.sellOrders = sellOrders;
    }

    public List<MarketOrder> getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(List<MarketOrder> buyOrders) {
        this.buyOrders = buyOrders;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("marketId", marketId)
            .add("sellOrders", sellOrders)
            .add("buyOrders", buyOrders)
            .toString();
    }
}
