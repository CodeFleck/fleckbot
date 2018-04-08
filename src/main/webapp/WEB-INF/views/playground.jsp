<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<template:admin>
    <jsp:attribute name="extraScripts" />
    <jsp:body>
        <div>
            <div class ="container container"><br>
                <h2 class="basic-title">Playground para testes</h2><br>
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
                                <td><select class="selectpicker" id="duration">
                                    <option>1 minuto</option>
                                    <option>5 minutos</option>
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
            </div>
            <br>
            <div class ="container container"><br>
                <h2 class="basic-title">Resultados</h2><br>
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

            <br><br>

            <!-- HTML -->
            <div id="chartdiv" class="col-md-12"></div>

            <!-- Chart code -->
            <script>
              var chartData = [];
              generateChartData();

              function generateChartData() {
                var firstDate = new Date( 2012, 0, 1 );
                firstDate.setDate( firstDate.getDate() - 500 );
                firstDate.setHours( 0, 0, 0, 0 );

                for ( var i = 0; i < 500; i++ ) {
                  var newDate = new Date( firstDate );
                  newDate.setDate( newDate.getDate() + i );

                  var a = Math.round( Math.random() * ( 40 + i ) ) + 100 + i;
                  var b = Math.round( Math.random() * 100000000 );

                  chartData.push( {
                    "date": newDate,
                    "value": a,
                    "volume": b
                  } );
                }
              }

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
                  "stockEvents": [ {
                    "date": new Date( 2010, 8, 19 ),
                    "type": "sign",
                    "backgroundColor": "#85CDE6",
                    "graph": "g1",
                    "text": "S",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2010, 10, 19 ),
                    "type": "flag",
                    "backgroundColor": "#FFFFFF",
                    "backgroundAlpha": 0.5,
                    "graph": "g1",
                    "text": "F",
                    "description": "Some longer\ntext can also\n be added"
                  }, {
                    "date": new Date( 2010, 11, 10 ),
                    "showOnAxis": true,
                    "backgroundColor": "#85CDE6",
                    "type": "pin",
                    "text": "X",
                    "graph": "g1",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2010, 11, 26 ),
                    "showOnAxis": true,
                    "backgroundColor": "#85CDE6",
                    "type": "pin",
                    "text": "Z",
                    "graph": "g1",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2011, 0, 3 ),
                    "type": "sign",
                    "backgroundColor": "#85CDE6",
                    "graph": "g1",
                    "text": "U",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2011, 1, 6 ),
                    "type": "sign",
                    "graph": "g1",
                    "text": "D",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2011, 3, 5 ),
                    "type": "sign",
                    "graph": "g1",
                    "text": "L",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2011, 3, 5 ),
                    "type": "sign",
                    "graph": "g1",
                    "text": "R",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2011, 5, 15 ),
                    "type": "arrowUp",
                    "backgroundColor": "#00CC00",
                    "graph": "g1",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2011, 6, 25 ),
                    "type": "arrowDown",
                    "backgroundColor": "#CC0000",
                    "graph": "g1",
                    "description": "This is description of an event"
                  }, {
                    "date": new Date( 2011, 8, 1 ),
                    "type": "text",
                    "graph": "g1",
                    "text": "Longer text can\nalso be displayed",
                    "description": "This is description of an event"
                  } ]
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

            <!-- Resources -->
            <script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
            <script src="https://www.amcharts.com/lib/3/serial.js"></script>
            <script src="https://www.amcharts.com/lib/3/amstock.js"></script>
            <script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
            <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
            <script src="https://www.amcharts.com/lib/3/themes/light.js"></script>
        </div>
    </jsp:body>
</template:admin>