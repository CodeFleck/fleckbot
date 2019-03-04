package br.com.codefleck.tradebot.models;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
public class DataPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer dataPointsid;

    private double x;
    private double y;
    private String nomeConjunto;
    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    DataPointsListType dataPointsListType;

    public DataPoints(){
    }

    public DataPoints(double x, double y, String nomeConjunto, LocalDateTime localDateTime, DataPointsListType dataPointModelistType) {
        this.x = x;
        this.y = y;
        this.nomeConjunto = nomeConjunto;
        this.localDateTime = localDateTime;
        this.dataPointsListType = dataPointModelistType;
    }

    public Integer getDataPointsid() {
        return dataPointsid;
    }

    public void setDataPointsid(Integer dataPointsid) {
        this.dataPointsid = dataPointsid;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getNomeConjunto() {
        return nomeConjunto;
    }

    public void setNomeConjunto(String nomeConjunto) {
        this.nomeConjunto = nomeConjunto;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }


    public DataPointsListType getDataPointsListType() {
        return dataPointsListType;
    }

    public void setDataPointsListType(DataPointsListType dataPointsListType) {
        this.dataPointsListType = dataPointsListType;
    }

    @Override
    public String toString() {
        return "DataPoints{" +
                "dataPointsid=" + dataPointsid +
                ", x=" + x +
                ", y=" + y +
                ", nomeConjunto='" + nomeConjunto + '\'' +
                ", localDateTime=" + localDateTime +
                ", dataPointsListType=" + dataPointsListType +
                '}';
    }
}

