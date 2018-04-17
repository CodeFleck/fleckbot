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

                <!-- Chart code -->
                <script>
                  var chartData = [];
                  var tradeData = [];

                  <c:forEach items='${tradeListForGraph}' var='tradeEvents'>

                  var eventDate = new Date(${tradeEvents.substring(9,22)});
                  eventDate.setDate( eventDate.getDate() );
                  var hour = eventDate.getHours();
                  var minute = eventDate.getMinutes();
                  eventDate.setHours( hour, minute );

                  var type = ${tradeEvents.substring(31,37)};
                  var backgroundColor = ${tradeEvents.substring(56,65)};
                  var text = ${tradeEvents.substring(110,113)};
                  var description = ${tradeEvents.substring(128,172)};

                  console.log("type: "+ type) ;
                  console.log(" backgroundColor: " + backgroundColor);
                  console.log(" text: " + text);
                  console.log(" description: " + description);

                          tradeData.push({
                            "date": eventDate,
                            "type": type,
                            "backgroundColor": backgroundColor,
                            "graph": "g1",
                            "text": text,
                            "description": description
                          });
                  </c:forEach>

                  <c:forEach items='${listaDeBarras}' var='bar'>

                  var newDate = new Date( ${bar.customEndTimeForGraph} );
                  newDate.setDate( newDate.getDate() );
                  var hour = newDate.getHours();
                  var minute = newDate.getMinutes();
                  newDate.setHours( hour, minute );

                  chartData.push( {
                    "date": newDate,
                    "value": "${bar.closePrice.toString()}",
                    "volume": "${bar.volume.toString()}"
                  } );
                  </c:forEach>

                  var chart = AmCharts.makeChart( "chartdiv", {
                    "type": "stock",
                    "theme": "light",
                    "dataSets": [ {
                      "color": "#b0de09",
                      "fieldMappings": [ {
                        "fromField": "value",
                        "toField": "value"
                      }, {
                        "fromField": "volume",
                        "toField": "volume"
                      } ],
                      "dataProvider": chartData,
                      "categoryField": "date",
                      // EVENTS
                      "stockEvents": tradeData,
                    } ],

                    "panels": [ {
                      "title": "Value",
                      "stockGraphs": [ {
                        "id": "g1",
                        "valueField": "value"
                      } ],
                      "stockLegend": {
                        "valueTextRegular": " ",
                        "markerType": "none"
                      }
                    } ],

                    "chartScrollbarSettings": {
                      "graph": "g1"
                    },

                    "chartCursorSettings": {
                      "valueBalloonsEnabled": true,
                      "graphBulletSize": 1,
                      "valueLineBalloonEnabled": true,
                      "valueLineEnabled": true,
                      "valueLineAlpha": 0.5
                    },

                    "periodSelector": {
                      "periods": [ {
                        "period": "DD",
                        "count": 10,
                        "label": "10 days"
                      }, {
                        "period": "MM",
                        "count": 1,
                        "label": "1 month"
                      }, {
                        "period": "YYYY",
                        "count": 1,
                        "label": "1 year"
                      }, {
                        "period": "YTD",
                        "label": "YTD"
                      }, {
                        "period": "MAX",
                        "label": "MAX"
                      } ]
                    },

                    "panelsSettings": {
                      "usePrefixes": true
                    },
                    "export": {
                      "enabled": true
                    }
                  } );
                </script>

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
                                    <td> <input type="date" name="beginDate" min="2014-01-01" max="2018-01-08"></td>
                                    <td><input type="date" name="endDate" min="2014-01-01" max="2018-01-08"></td>
                                    <td><select class="selectpicker" name="period">
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
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>Minha estratégia</td>
                                </tr>
                                </tbody>
                            </table>
                            <button type="submit" class="btn btn-primary">Carregar Dados</button>
                        </form:form>
                    </div>
                    <br>
                    <h2 class="basic-title">Resultados</h2><br>
                    <div id="chartdiv"></div><br>
                    <div class="well">
                        <p>Tempo de execução dos testes: ${stopTime}</p>
                        <p>Quantidade de barras percorridas: ${numberOfBars}</p>
                        <p>Número total de ordens efetuadas: ${numberOfTrades}</p>

                        <br><p>***   ORDENS   ***</p>
                        <c:forEach items='${tradeList}' var='object'>
                            <tr>
                                <td><b>Compra</b></td><br>
                                <td><b>Data compra:</b> ${series.getBar(object.entry.index).beginTime}</td><br>
                                <td><b>Preço: </b>$${object.entry.price}</td><br>
                                <td><b>Quantia:</b> USD$${object.entry.amount} (FAZER ESTE VALOR DINAMICO)</td><br>
                            </tr>
                            <tr>
                                <td><b>Venda</b></td><br>
                                <td><b>Data venda: </b>${series.getBar(object.exit.index).endTime}</td><br>
                                <td><b>Preço:</b> $${object.exit.price}</td>
                                <td><b>Quantia:</b> USD$${object.exit.amount}(FAZER ESTE VALOR DINAMICO)</td><br>
                                <c:set var = "lucroBruto" scope = "session" value = "${object.exit.price - object.entry.price}"/>
                                <td><b>Lucro Bruto:</b> $<c:out value = "${lucroBruto}"/></td>
                                <c:set var = "taxa" scope = "session" value = "${lucroBruto - (lucroBruto*0.999)}"/>
                                <td> | <b>Taxa (0,1%):</b> $ <c:out value = "${taxa}"/></td>
                                <c:set var = "lucroLiquido" scope = "session" value = "${lucroBruto*0.999}"/>
                                <td> | <b>Lucro Líquido:</b> $<c:out value = "${lucroLiquido}"/></td>
                            </tr><br><br>
                        </c:forEach>
                        <br>
                        <p><b>Lucro bruto Total:</b> $${totalGrossProfit}</p>
                        <p><b>Total pago em taxas:</b> $${totalFees}</p>
                        <p><b>Lucro líquido Total:</b> $${totalNetProfit}</p>

                        <p><b>Diferença trade X HODL: </b>
                            HODL: $${buyAndHoldResult} |
                            Trade: $${buyAndHoldResult + totalNetProfit}</p>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</template:admin>