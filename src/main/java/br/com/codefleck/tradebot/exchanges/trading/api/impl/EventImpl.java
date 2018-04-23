package br.com.codefleck.tradebot.exchanges.trading.api.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.codefleck.tradebot.tradingapi.Event;

@Entity
public final class EventImpl implements Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String date;
    private String dateInMilis;
    private String type;
    private String backgroundColor;
    private String backgroundAlpha;
    private String graph;
    private String text;
    private String description;

    public EventImpl(){

    }

    public EventImpl(String date, String type, String backgroundColor, String backgroundAlpha, String graph, String text, String description) {
        this.date = date;
        this.type = type;
        this.backgroundColor = backgroundColor;
        this.backgroundAlpha = backgroundAlpha;
        this.graph = graph;
        this.text = text;
        this.description = description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String getBackgroundAlpha() {
        return backgroundAlpha;
    }

    public void setBackgroundAlpha(String backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
    }

    @Override
    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateInMilis() {
        return dateInMilis;
    }

    public void setDateInMilis(String dateInMilis) {
        this.dateInMilis = dateInMilis;
    }

    @Override
    public String toString() {
        return "EventImpl{" + "id=" + id + ", date='" + date + '\'' + ", dateInMilis='" + dateInMilis + '\'' + ", type='" + type + '\'' + ", backgroundColor='" + backgroundColor + '\'' + ", backgroundAlpha='" + backgroundAlpha + '\'' + ", graph='" + graph + '\'' + ", text='" + text + '\'' + ", description='" + description + '\'' + '}';
    }
}
