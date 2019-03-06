<%@ page contentType="text/html" pageEncoding="ISO-8859-1" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>
        <meta http-equiv="refresh" content="60; URL=http://localhost:8090/analise">

        <div class ="container-fluid"><br>
            <h2 class="basic-title">Análise preditiva</h2><br>
            <p>Confira aqui as previsões de preço:</p>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Moeda</th>
                    <th>Preço Atual</th>
                    <th>1 min</th>
                    <th>5 min</th>
                    <th>10 min</th>
                    <th>15 min</th>
                    <th>30 min</th>
                    <th>1 hora</th>
                    <th>2 horas</th>
                    <th>4 horas</th>
                    <th>1 dia</th>
                    <th>1 semana</th>
                    <th>1 mês</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Bitcoin</td>
                    <td><div id="btc"></div></td>
                    <td>$${oneMinute}</td>
                    <td>$${fiveMinutes}</td>
                    <td>$${tenMinutes}</td>
                    <td>$${fifteenMinutes}</td>
                    <td>$${thirtyMinutes}</td>
                    <td>$${oneHour}</td>
                    <td>$${twoHours}</td>
                    <td>$${fourHours}</td>
                    <td>$${twentyFourHours}</td>
                    <td>$${oneWeek}</td>
                    <td>$${oneMonth}</td>
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
