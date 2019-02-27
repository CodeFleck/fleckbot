package br.com.codefleck.tradebot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
public class StockData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private ZonedDateTime date; // date
    private String symbol; // stock name

    private double open; // open price
    private double close; // close price
    private double low; // low price
    private double high; // high price
    private double volume; // volume

    public StockData() {
    }

    public StockData (ZonedDateTime date, String symbol, double open, double close, double low, double high, double volume) {
        this.date = date;
        this.symbol = symbol;
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
        this.volume = volume;
    }

    public ZonedDateTime getDate() { return date; }
    public void setDate(ZonedDateTime date) { this.date = date; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getOpen() { return open; }
    public void setOpen(double open) { this.open = open; }

    public double getClose() { return close; }
    public void setClose(double close) { this.close = close; }

    public double getLow() { return low; }
    public void setLow(double low) { this.low = low; }

    public double getHigh() { return high; }
    public void setHigh(double high) { this.high = high; }

    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "StockData{" + "id=" + id + ", date='" + date + '\'' + ", symbol='" + symbol + '\'' + ", open=" + open + ", close=" + close + ", low=" + low + ", high=" + high + ", volume=" + volume + '}';
    }
}
