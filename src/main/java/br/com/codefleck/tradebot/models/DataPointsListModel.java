package br.com.codefleck.tradebot.models;

import java.util.List;

public class DataPointsListModel {

    private Integer id;
    String name;
    List<DataPointsModel> predictDataPointsModelList;
    List<DataPointsModel> actualDataPointsModelList;

    public DataPointsListModel(){}

    public DataPointsListModel(String name, List<DataPointsModel> predictDataPointsModelList, List<DataPointsModel> actualDataPointsModelList) {
        this.name = name;
        this.predictDataPointsModelList = predictDataPointsModelList;
        this.actualDataPointsModelList = actualDataPointsModelList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "DataPointsListModel{" + "id=" + id + ", name='" + name + '\'' + ", predictDataPointsModelList=" + predictDataPointsModelList + ", actualDataPointsModelList=" + actualDataPointsModelList + '}';
    }
}
