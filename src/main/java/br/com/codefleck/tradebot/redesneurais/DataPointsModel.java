package br.com.codefleck.tradebot.redesneurais;

public class DataPointsModel {

    double x;
    double y;

    DataPointsListModel dataPointsListModel;

    public DataPointsModel(){}

    public DataPointsModel(double x, double y, DataPointsListModel dataPointsListModel) {
        this.x = x;
        this.y = y;
        this.dataPointsListModel = dataPointsListModel;
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

    public DataPointsListModel getDataPointsListModel() {
        return dataPointsListModel;
    }

    public void setDataPointsListModel(DataPointsListModel dataPointsListModel) {
        this.dataPointsListModel = dataPointsListModel;
    }

    @Override
    public String toString() {
        return "DataPointsModel{" + ", x=" + x + ", y=" + y + ", dataPointsListModel=" + dataPointsListModel + '}';
    }
}
