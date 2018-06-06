package br.com.codefleck.tradebot.tradingInterfaces;

public interface Event {

    Integer getId();

    String getDate();

    String getType();

    String getBackgroundColor();

    String getBackgroundAlpha();

    String getGraph();

    String getText();

    String getDescription();

}
