<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>

            <!-- TradingView Widget BEGIN -->
            <div class="tradingview-widget-container">
                <div id="technical-analysis"></div>
                <div class="tradingview-widget-copyright"><a href="https://tradingview.com/symbols/AAPL/" rel="noopener" target="_blank"><span class="blue-text">AAPL</span> by TradingView</a></div>
                <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
                <script type="text/javascript">
                  new TradingView.widget(
                    {
                      "container_id": "technical-analysis",
                      "width": 1270,
                      "height": 610,
                      "symbol": "BITFINEX:BTCUSD",
                      "interval": "D",
                      "timezone": "exchange",
                      "theme": "Light",
                      "style": "1",
                      "toolbar_bg": "#f1f3f6",
                      "withdateranges": true,
                      "hide_side_toolbar": false,
                      "allow_symbol_change": true,
                      "save_image": false,
                      "hideideas": true,
                      "studies": [
                        "DoubleEMA@tv-basicstudies"
                      ],
                      "show_popup_button": true,
                      "popup_width": "1000",
                      "popup_height": "650",
                      "locale": "en"
                    }
                  );
                </script>
            </div>
            <!-- TradingView Widget END -->
        <p>*************</p>
        <div>
            <h6>Bitcoin USD: </h6>
            <div id="btc"></div>
        </div>
        <p>*************</p>
        <div>
            <h6>Livro de Ofertas: </h6>
            <h6>Compra</h6>
            <div id="orderBookBid"></div>
            <h6>Venda</h6>
            <div id="orderBookAsk"></div>
        </div>


        <script src="/assets/js/connectBitfinex.js"></script>
        <script src="/assets/js/bitfinexDataProvider.js"></script>
    </jsp:body>
</template:admin>