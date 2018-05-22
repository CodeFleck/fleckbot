package br.com.codefleck.tradebot.core.util;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class SMA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    Double valor;
    String period;
    Integer timeFrame;
    String data;

    public SMA(Double valor, String period, Integer timeFrame, String data) {
        this.valor = valor;
        this.period = period;
        this.timeFrame = timeFrame;
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(Integer timeFrame) {
        this.timeFrame = timeFrame;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SMA{" + "id=" + id + ", valor=" + valor + ", period='" + period + '\'' + ", timeFrame=" + timeFrame + ", data='" + data + '\'' + '}';
    }
}
