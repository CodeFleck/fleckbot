<%@ page contentType="text/html" pageEncoding="ISO-8859-1" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
        "show_popup_button": true,
        "popup_width": "1000",
        "popup_height": "650",
        "locale": "en"
        }
        );
        </script>
        </div>
        <!-- TradingView Widget END -->

        <%--<div class="container">--%>
            <%--<div class="col-md-6 text-left">--%>
                <%--<form:form method="POST" action="/analise/treinar-redes">--%>
                    <%--<table class="table table-condensed table-bordered table-striped table-hover">--%>
                        <%--<thead>--%>
                        <%--<tr>--%>
                            <%--<td>Símbolo</td>--%>
                            <%--<td>Épocas</td>--%>
                            <%--<td>Tamanho Lote</td>--%>
                        <%--</tr>--%>
                        <%--</thead>--%>
                        <%--<tbody>--%>
                        <%--<tr>--%>
                            <%--<td><select class="form-control selectpicker" name="simbolo">--%>
                                <%--<option>BTC</option>--%>
                            <%--</select>--%>
                            <%--<td><input type="number" class="form-control" name="epocas" min="0" step="1" value="100"></td>--%>
                            <%--<td><input type="number" class="form-control" name="tamanhoLote" min="0" step="1" value="64"></td>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        <%--</tbody>--%>
                    <%--</table>--%>
                    <%--<button type="submit" class="btn btn-primary btntreinar">Treinar Redes Neurais</button>--%>
                <%--</form:form>--%>
            <%--</div>--%>
        <%--</div>--%>







        <%--<p>*************</p>--%>
        <%--<div>--%>
            <%--<h6>Bitcoin USD: </h6>--%>
            <%--<div id="btc"></div>--%>
        <%--</div>--%>
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