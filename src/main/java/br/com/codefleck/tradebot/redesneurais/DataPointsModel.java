package br.com.codefleck.tradebot.redesneurais;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DataPointsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    double x;
    double y;
    String nomeConjunto;

    public DataPointsModel(){}

    public DataPointsModel(double x, double y, String nomeConjunto) {
        this.x = x;
        this.y = y;
        this.nomeConjunto = nomeConjunto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "DataPointsModel{" + "id=" + id + ", x=" + x + ", y=" + y + ", nomeConjunto='" + nomeConjunto + '\'' + '}';
    }
}

