<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<template:admin>
    <jsp:attribute name="extraScripts" />
    <jsp:body>
        <div>
        <div class ="container-fluid"><br>
            <h2 class="basic-title">Playground para testes</h2><br>

            <!-- Resources -->
            <script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
            <script src="https://www.amcharts.com/lib/3/serial.js"></script>
            <script src="https://www.amcharts.com/lib/3/amstock.js"></script>
            <script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
            <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
            <script src="https://www.amcharts.com/lib/3/themes/light.js"></script>

            <!-- HTML -->
            <div>
                <div class="well">
                    <form:form method="POST" action="/playground/load">
                        <table class="table table-condensed table-bordered table-striped table-hover">
                            <thead>
                            <tr>
                                <td>Corretora</td>
                                <td>Moeda Base</td>
                                <td>Ativo</td>
                                <td>De</td>
                                <td>Até</td>
                                <td>Período</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>Coinbase</td>
                                <td>USD</td>
                                <td>BTC</td>
                                <td> <input type="date" class="form-control" name="beginDate" min="2014-01-01" max="2018-01-08"></td>
                                <td><input type="date" class="form-control" name="endDate" min="2014-01-01" max="2018-01-08"></td>
                                <td><select class="form-control selectpicker" name="period">
                                    <option>1 minuto</option>
                                    <option>5 minutos</option>
                                    <option>10 minutos</option>
                                    <option>15 minutos</option>
                                    <option>30 minutos</option>
                                    <option>1 hora</option>
                                    <option>2 horas</option>
                                    <option>3 horas</option>
                                    <option>4 horas</option>
                                    <option>1 dia</option>
                                    <option>1 semana</option>
                                    <option>1 mês</option>
                                </select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <br>
                        <table class="table table-condensed table-bordered table-striped table-hover">
                            <thead>
                            <tr>
                                <td>Estratégia</td>
                                <td>Montante para compra/venda USD$</td>
                                <td>Saldo USD</td>
                                <td>Saldo BTC</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    <select class="form-control selectpicker" name="strategy">
                                        <%--<option>LSTM Prediction Strategy</option>--%>
                                        <option>SMA Duplo</option>
                                        <option>LSTM Prediction</option>
                                    </select>
                                </td>
                                <td><input type="number" class="form-control" name="montante" min="0" step="0.01" value="1000.00"></td>
                                <td><input type="number" class="form-control" name="saldo" min="0" step="0.01" value="50000.00"></td>
                                <td><input class="form-control" type="text" placeholder="0,00" readonly></td>
                            </tr>
                            </tbody>
                        </table>
                        <button type="submit" class="btn btn-primary btnInitTeste">Iniciar Teste</button>
                    </form:form>

                    <br>
                    <h2 class="basic-title">Resultados</h2>
                    <br>

                        <%--Chart de velas--%>
                        <%--<template:candleChart /><br>--%>
                        <%--Chart de Eventos--%>
                    <template:eventChart />

                    <div class="well">
                        <h6>***   Resumo   ***</h6>
                        <p>Período: ${chosenPeriod}</p>
                        <p>Número total de ordens executadas: ${numberOfTrades}<br>
                            Lucro de HODL: $${buyAndHoldPercentage}% | Lucro de trade: $${tradePercentage}%<br>
                            Lucro Total em USD: $${totalProfitUSD}   |   Lucro Total em BTC: ${bitcoinAmount}<br>
                            Total pago em taxas: $${totalFees}<br>
                            Saldo final: USD ${finalBalance}<br>
                            Saldo final bitcoins: BTC ${bitcoinAmount}<br>

                            <br><h6>***   Ordens   ***</h6>

                        <c:forEach items='${tradeList}' var='object'>
                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-md-6 float-left">
                                        <tr>
                                            <td><b>Compra: </b> ${series.getBar(object.entry.index).endTime}</td><br>
                                            <td><b>Quantia:</b> USD$${object.entry.amount}</td>
                                            <td><b>Preço: </b>$${object.entry.price}</td><br>
                                            <td><b>Lucro:</b> $${object.tradeProfit} (${object.tradeProfitPercentage}%)</td>
                                        </tr>
                                    </div>
                                    <div class="col-md-6">
                                        <tr>
                                            <td><b>Venda: </b>${series.getBar(object.exit.index).endTime}</td><br>
                                            <td><b>Quantia:</b> $${object.exit.amount}</td>
                                            <td><b>Preço:</b> $${object.exit.price}</td><br>
                                        </tr>
                                    </div>
                                </div><hr>
                            </div>
                        </c:forEach>
                        <br>

                        <p>Quantidade de barras percorridas: ${numberOfBars}</p>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</template:admin>