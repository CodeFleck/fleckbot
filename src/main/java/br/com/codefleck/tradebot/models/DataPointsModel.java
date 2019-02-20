package br.com.codefleck.tradebot.models;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
public class DataPointsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datapointmodelid", nullable=false, updatable=false)
    private Integer dataPointModelID;

    double x;
    double y;
    String nomeConjunto;
    LocalDateTime timeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataPointModelID")
    DataPointsListResultSet dataPointsListResultSet;

    @Enumerated(EnumType.STRING)
    DataPointModelistType dataPointModelistType;

    public DataPointsModel(){
    }

    public DataPointsModel(double x, double y, String nomeConjunto, LocalDateTime timeStamp, DataPointsListResultSet dataPointsListResultSet, DataPointModelistType dataPointModelistType) {
        this.x = x;
        this.y = y;
        this.nomeConjunto = nomeConjunto;
        this.timeStamp = timeStamp;
        this.dataPointsListResultSet = dataPointsListResultSet;
        this.dataPointModelistType = dataPointModelistType;
    }

    public Integer getDataPointModelID() {
        return dataPointModelID;
    }

    public void setDataPointModelID(Integer dataPointModelID) {
        this.dataPointModelID = dataPointModelID;
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

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public DataPointsListResultSet getDataPointsListResultSet() {
        return dataPointsListResultSet;
    }

    public void setDataPointsListResultSet(DataPointsListResultSet dataPointsListResultSet) {
        this.dataPointsListResultSet = dataPointsListResultSet;
    }

    public DataPointModelistType getDataPointModelistType() {
        return dataPointModelistType;
    }

    public void setDataPointModelistType(DataPointModelistType dataPointModelistType) {
        this.dataPointModelistType = dataPointModelistType;
    }

    @Override
    public String toString() {
        return "DataPointsModel{" +
                "dataPointModelID=" + dataPointModelID +
                ", x=" + x +
                ", y=" + y +
                ", nomeConjunto='" + nomeConjunto + '\'' +
                ", timeStamp=" + timeStamp +
                ", dataPointsListResultSet=" + dataPointsListResultSet +
                ", dataPointModelistType=" + dataPointModelistType +
                '}';
    }
}

