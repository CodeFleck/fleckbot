package br.com.codefleck.tradebot.exchanges.trading.api.impl;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.MoreObjects;

import br.com.codefleck.tradebot.tradingapi.Ticker;

/**
 * A Ticker implementation that can be used by Exchange Adapters.
 */
@Entity
public final class TickerImpl implements Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal last;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal open;
    private BigDecimal volume;
    private BigDecimal vwap;
    private Long timestamp;


    public TickerImpl(BigDecimal last, BigDecimal bid, BigDecimal ask, BigDecimal low, BigDecimal high,
                      BigDecimal open, BigDecimal volume, BigDecimal vwap, Long timestamp) {
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.low = low;
        this.high = high;
        this.open = open;
        this.volume = volume;
        this.vwap = vwap;
        this.timestamp = timestamp;
    }

    @Override
    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    @Override
    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    @Override
    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    @Override
    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    @Override
    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    @Override
    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    @Override
    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public BigDecimal getVwap() {
        return vwap;
    }

    public void setVwap(BigDecimal vwap) {
        this.vwap = vwap;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("last", last)
            .add("bid", bid)
            .add("ask", ask)
            .add("low", low)
            .add("high", high)
            .add("open", open)
            .add("volume", volume)
            .add("vwap", vwap)
            .add("timestamp", timestamp)
            .toString();
    }
}