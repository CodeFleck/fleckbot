package br.com.codefleck.tradebot.redesneurais;

import java.util.List;
import java.util.Map;

public class DataPointsListModel {

    public int id;
    String name;
    List<Map<Object, Object>> predictsDataPoints;
    List<Map<Object, Object>> actualsDataPoints;

    public DataPointsListModel(){}

    public DataPointsListModel(int id, String name, List<Map<Object, Object>> predictsDataPoints, List<Map<Object, Object>> actualsDataPoints ) {
        this.id = id;
        this.name = name;
        this.predictsDataPoints = predictsDataPoints;
        this.actualsDataPoints = actualsDataPoints;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<Object, Object>> getPredictsDataPoints() {
        return predictsDataPoints;
    }

    public void setPredictsDataPoints(List<Map<Object, Object>> predictsDataPoints) {
        this.predictsDataPoints = predictsDataPoints;
    }

    public List<Map<Object, Object>> getActualsDataPoints() {
        return actualsDataPoints;
    }

    public void setActualsDataPoints(List<Map<Object, Object>> actualsDataPoints) {
        this.actualsDataPoints = actualsDataPoints;
    }

    @Override
    public String toString() {
        return "DataPointsListModel{" + "id=" + id + ", name='" + name + '\'' + ", predictsDataPoints=" + predictsDataPoints + ", actualsDataPoints=" + actualsDataPoints + '}';
    }
}
