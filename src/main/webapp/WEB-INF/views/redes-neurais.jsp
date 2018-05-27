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
                            <td>Símbolo</td>
                            <td>Categoria</td>
                            <td>Épocas</td>
                            <td>Tamanho Lote</td>
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
                                    <option>Todos</option>
                                    <option>Fechamento</option>
                                    <option>Abertura</option>
                                    <option>Maior</option>
                                    <option>Menor</option>
                                    <option>Volume</option>
                                </select>
                            </td>
                            <td><input type="number" class="form-control" name="epocas" min="0" step="1" value="100"></td>
                            <td><input type="number" class="form-control" name="tamanhoLote" min="0" step="1" value="64"></td>
                        </tr>
                        </tbody>
                    </table>
                    <button type="submit" class="btn btn-primary">Treinar Redes Neurais</button>
                </form:form>
            </div>
            <button type="submit" class="btn btn-success" onclick="callPredictionChart()">Carregar Últimos Resultados</button><br>
            <div id='chartContainer'></div>
        </div>

        <script type="text/javascript">

          $(function () {
            var chart = new CanvasJS.Chart("chartContainer", {
              title: {
                text: "Preço Bitcoin"
              },
              subtitles: [
                {
                  text: "Visualização da margem de erro"
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
