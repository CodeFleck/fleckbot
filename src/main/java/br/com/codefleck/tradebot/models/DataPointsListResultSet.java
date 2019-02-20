package br.com.codefleck.tradebot.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DataPointsListResultSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String nomeConjunto;

    @OneToMany(mappedBy = "dataPointModelID")
    private List<DataPointsModel> predictDataPointsModelList = new ArrayList<>();

    @OneToMany(mappedBy = "dataPointModelID")
    private List<DataPointsModel> actualDataPointsModelList = new ArrayList<>();

    public DataPointsListResultSet() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeConjunto() {
        return nomeConjunto;
    }

    public void setNomeConjunto(String nomeConjunto) {
        this.nomeConjunto = nomeConjunto;
    }

    public List<DataPointsModel> getPredictDataPointsModelList() {
        return predictDataPointsModelList;
    }

    public void setPredictDataPointsModelList(List<DataPointsModel> predictDataPointsModelList) {
        this.predictDataPointsModelList = predictDataPointsModelList;
    }

    public List<DataPointsModel> getActualDataPointsModelList() {
        return actualDataPointsModelList;
    }

    public void setActualDataPointsModelList(List<DataPointsModel> actualDataPointsModelList) {
        this.actualDataPointsModelList = actualDataPointsModelList;
    }

    @Override
    public String toString() {
        return "DataPointsListResultSet{" +
                "id=" + id +
                ", nomeConjunto='" + nomeConjunto + '\'' +
                ", predictDataPointsModelList=" + predictDataPointsModelList +
                ", actualDataPointsModelList=" + actualDataPointsModelList +
                '}';
    }
}
