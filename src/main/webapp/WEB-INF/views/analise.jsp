<%@ page contentType="text/html" pageEncoding="ISO-8859-1" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>
        <meta http-equiv="refresh" content="60; URL=http://localhost:8090/analise">
        <!-- TradingView Widget BEGIN -->
        <%--<div class="tradingview-widget-container">--%>
        <%--<div id="technical-analysis"></div>--%>
        <%--<div class="tradingview-widget-copyright"><a href="https://tradingview.com/symbols/AAPL/" rel="noopener" target="_blank"><span class="blue-text">AAPL</span> by TradingView</a></div>--%>
        <%--<script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>--%>
        <%--<script type="text/javascript">--%>
        <%--new TradingView.widget(--%>
        <%--{--%>
        <%--"container_id": "technical-analysis",--%>
        <%--"width": 1270,--%>
        <%--"height": 610,--%>
        <%--"symbol": "BITFINEX:BTCUSD",--%>
        <%--"interval": "D",--%>
        <%--"timezone": "exchange",--%>
        <%--"theme": "Light",--%>
        <%--"style": "1",--%>
        <%--"toolbar_bg": "#f1f3f6",--%>
        <%--"withdateranges": true,--%>
        <%--"hide_side_toolbar": false,--%>
        <%--"allow_symbol_change": true,--%>
        <%--"save_image": false,--%>
        <%--"hideideas": true,--%>
        <%--"show_popup_button": true,--%>
        <%--"popup_width": "1000",--%>
        <%--"popup_height": "650",--%>
        <%--"locale": "en"--%>
        <%--}--%>
        <%--);--%>
        <%--</script>--%>
        <%--</div>--%>
        <!-- TradingView Widget END -->

        <div class ="container-fluid"><br>
            <h2 class="basic-title">Análise preditiva</h2><br>
            <p>Confira aqui as previsões de preço:</p>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Moeda</th>
                    <th>Preço Atual</th>
                    <th>1 min</th>
                    <th>15 min</th>
                    <th>30 min</th>
                    <th>1 hora</th>
                    <th>2 horas</th>
                    <th>4 horas</th>
                    <th>1 dia</th>
                    <th>1 semana</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Bitcoin</td>
                    <td><div id="btc"></div></td>
                    <td>$${oneMinute}</td>
                    <td>$${fifteenMinutes}</td>
                    <td>$${thirtyMinutes}</td>
                    <td>$${oneHour}</td>
                    <td>$${twoHours}</td>
                    <td>$${fourHours}</td>
                    <td>$${twentyFourHours}</td>
                    <td>$${oneWeek}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <br><br>
        <p>Categoria: ${categoria}</p>
        <p>Porcentagem média de erro: ${errorPercentageAvg}%</p>

        <%--<p>*************</p>--%>
        <%--<div>--%>
        <%--<h6>Livro de Ofertas: </h6>--%>
        <%--<h6>Compra</h6>--%>
        <%--<div id="orderBookBid"></div>--%>
        <%--<h6>Venda</h6>--%>
        <%--<div id="orderBookAsk"></div>--%>
        <%--</div>--%>


        <script src="/assets/js/connectBitfinex.js"></script>
        <script src="/assets/js/bitfinexDataProvider.js"></script>
    </jsp:body>
</template:admin>
