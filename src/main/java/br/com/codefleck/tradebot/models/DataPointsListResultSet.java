package br.com.codefleck.tradebot.models;

import java.util.ArrayList;
import java.util.List;

public class DataPointsListResultSet {

    private Integer resultsetid;

    String nomeConjunto;

    private List<DataPoints> predictDataPointsList = new ArrayList<>();

    private List<DataPoints> actualDataPointsList = new ArrayList<>();

    public DataPointsListResultSet() {
    }

    public Integer getResultsetid() {
        return resultsetid;
    }

    public void setResultsetid(Integer resultsetid) {
        this.resultsetid = resultsetid;
    }

    public String getNomeConjunto() {
        return nomeConjunto;
    }

    public void setNomeConjunto(String nomeConjunto) {
        this.nomeConjunto = nomeConjunto;
    }

    public List<DataPoints> getPredictDataPointsList() {
        return predictDataPointsList;
    }

    public void setPredictDataPointsList(List<DataPoints> predictDataPointsList) {
        this.predictDataPointsList = predictDataPointsList;
    }

    public List<DataPoints> getActualDataPointsList() {
        return actualDataPointsList;
    }

    public void setActualDataPointsList(List<DataPoints> actualDataPointsList) {
        this.actualDataPointsList = actualDataPointsList;
    }

    @Override
    public String toString() {
        return "DataPointsListResultSet{" +
                "Resultsetid=" + resultsetid +
                ", nomeConjunto='" + nomeConjunto + '\'' +
                ", predictDataPointsList=" + predictDataPointsList +
                ", actualDataPointsList=" + actualDataPointsList +
                '}';
    }
}
