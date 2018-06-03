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
                <form:form method="POST" action="/redes-neurais/treinar-redes">
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
                            <td><input type="number" class="form-control" name="epocas" min="0" step="1" value="100"></td>
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
                            <td><input type="text" class="form-control" name="nomeDoConjunto"></td>
                        </tr>
                        </tbody>
                    </table>
                    <button type="submit" class="btn btn-primary">Treinar Redes Neurais</button>
                </form:form>
            </div><br><br>
            <p>Categoria: ${categoria}</p>
            <p>Porcentagem média de erro: ${errorPercentageAvg}%</p>
            <p>Porcentagem de erro no último dia: ${errorPercentageLastDay}%</p>
            <div id='chartContainer'></div><br>
        </div>

        <script type="text/javascript">

          $(function () {
            var chart = new CanvasJS.Chart("chartContainer", {
              title: {
                text: "Preço Bitcoin"
              },
              subtitles: [
                {
                  text: "Margem de erro "
                }
              ],
              animationEnabled: true,
              axisY: {
                titleFontFamily: "arial",
                titleFontSize: 12,
                includeZero: false
              },
              toolTip: {
                shared: true
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
