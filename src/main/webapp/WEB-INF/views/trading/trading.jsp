<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<template:admin>
    <jsp:attribute name="extraScripts">
    </jsp:attribute>
    <jsp:body>

        <div id="chartdiv"></div>






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


        <script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
        <script src="https://www.amcharts.com/lib/3/serial.js"></script>
        <script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
        <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
        <script src="https://www.amcharts.com/lib/3/themes/light.js"></script>
        <script src="/assets/js/tradingGraphCaller.js"></script>
        <script src="/assets/js/connectBitfinex.js"></script>
        <script src="/assets/js/bitfinexDataProvider.js"></script>
    </jsp:body>
</template:admin>
