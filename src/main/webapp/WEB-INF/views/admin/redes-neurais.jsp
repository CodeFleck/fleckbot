<%@ page contentType="text/html" pageEncoding="ISO-8859-1" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="java.util.*" %>
<template:admin>
    <jsp:attribute name="extraScripts" />
    <jsp:body>
        <script src="https://canvasjs.com/assets/script/canvasjs.min.js" type="text/javascript"></script>

        <div class="container-fluid"><br>
            <h2 class="basic-title">Treinar Redes Neurais</h2><br>
            <div class="col-md-6 text-left">
                <form:form method="POST" action="/admin/redes-neurais/treinar-redes">
                    <table class="table table-condensed table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <td>Ativo</td>
                            <td>Categoria a Prever</td>
                            <td>Épocas</td>
                            <td>De</td>
                            <td>Até</td>
                            <td>Período</td>
                            <td>Nome do Conjunto</td>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <select class="form-control selectpicker" name="simbolo">
                                    <option>BTC</option>
                                </select>
                            </td>
                            <td>
                                <select class="form-control selectpicker" name="categoria">
                                    <option>Fechamento</option>
                                    <option>Abertura</option>
                                    <option>Maior</option>
                                    <option>Menor</option>
                                    <option>Volume</option>
                                </select>
                            </td>
                            <td><input type="number" class="form-control" name="epocas" min="0" step="1" value="5"></td>
                            <td> <input type="date" class="form-control" name="beginDate" min="2015-01-01" max="2018-01-08" value="2015-01-01"></td>
                            <td><input type="date" class="form-control" name="endDate" min="2015-01-01" max="2018-01-08" value="2018-01-01"></td>
                            <td><select class="form-control selectpicker" name="period">
                                <option>1 minuto</option>
                                <option>5 minutos</option>
                                <option>10 minutos</option>
                                <option>15 minutos</option>
                                <option>30 minutos</option>
                                <option>1 hora</option>
                                <option>2 horas</option>
                                <option>4 horas</option>
                                <option>1 dia</option>
                                <option>1 semana</option>
                                <option>1 mes</option>
                            </select>
                            </td>
                            <td><input type="text" class="form-control" name="nomeDoConjunto" value="1sem-5ep-LR0.001-jan15jan18"></td>
                        </tr>
                        </tbody>
                    </table>
                    <button type="submit" class="btn btn-primary">Treinar Redes Neurais</button>
                </form:form>
            </div><br>
            <div>
                <p>Nome do conjunto: ${nomeConjunto}</p>
                <c:if test="${categoria} != null%">
                    <p>Categoria: ${categoria}</p></c:if>
                <c:if test="${errorPercentageAvg} != null%">
                <p>Porcentagem média de erro: ${errorPercentageAvg}%</p></c:if>
                <c:if test="${errorPercentageLastDay} != null%">
                    <p>Porcentagem de erro no último dia: ${errorPercentageLastDay}%</p></c:if>
                <c:if test="${epocas} != null%">
                    <p>Número de épocas: ${epocas}</p></c:if>
                <p>Data de inicio: ${beginDate}</p>
                <p>Data final: ${endDate}</p>
                <c:if test="${period} != null%">
                    <p>Período: ${period}</p></c:if>
                <c:if test="${majorError} != null%">
                    <p>Maior erro: ${majorError}%</p></c:if>
                <c:if test="${minorError} != null%">
                    <p>Menor erro: ${minorError}%</p></c:if>
            </div>
            <div id='chartContainer'></div><br>
        </div>

        <script type="text/javascript">

          $(function () {
            var chart = new CanvasJS.Chart("chartContainer", {
                backgroundColor: null,
              title: {
                text: "Preço Bitcoin",
                  fontFamily: "verdana",
                  fontSize: 25
              },
              animationEnabled: true,
              axisY: {
                titleFontFamily: "verdana",
                titleFontSize: 25,
                includeZero: false
              },
              toolTip: {
                shared: true
              },
              axisX: {
                  labelAngle: -45
              },
              data: [
                {
                  type: "spline",
                  name: "Predição",
                  showInLegend: true,
                  dataPoints: ${predictsDataPoints}
                },
                {
                  type: "spline",
                  name: "Preço Real",
                  showInLegend: true,
                  dataPoints: ${actualsDataPoints}
                }
              ],
              legend: {
                cursor: "pointer",
                  fontSize: 25,
                  fontFamily: "verdana",
                  fontWeight: "lighter",
                itemclick: function (e) {
                  if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
                    e.dataSeries.visible = false;
                  }
                  else {
                    e.dataSeries.visible = true;
                  }
                  chart.render();
                }
              }
            });

            chart.render();
          });
        </script>






        <%--<br><br>--%>
        <%--<div>--%>
        <%--<h6>Bitcoin USD: </h6>--%>
        <%--<div id="btc"></div>--%>
        <%--</div>--%>
        <%--<div>--%>
        <%--<h6>Livro de Ofertas: </h6>--%>
        <%--<h6>Compra</h6>--%>
        <%--<div id="orderBookBid"></div>--%>
        <%--<h6>Venda</h6>--%>
        <%--<div id="orderBookAsk"></div>--%>
        <%--</div>--%>

        <%--<script src="/assets/js/connectBitfinex.js"></script>--%>
        <%--<script src="/assets/js/bitfinexDataProvider.js"></script>--%>
    </jsp:body>
</template:admin>
