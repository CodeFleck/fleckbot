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
                                <td>At&eacute;</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>Coinbase</td>
                                <td>USD</td>
                                <td>BTC</td>
                                <td> <input type="date" name="beginDate" min="2014-01-01" max="2018-01-08"></td>
                                <td><input type="date" name="endDate" min="2014-01-01" max="2018-01-08"></td>
                            </tr>
                            </tbody>
                        </table>
                        <br>
                        <table class="table table-condensed table-bordered table-striped table-hover">
                            <thead>
                            <tr>
                                <td></td>
                                <td>Estrat&eacute;gia</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><input type="radio" name="strategyName" id="myStrategy"></td>
                                <td>Minha estrategia</td>
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
                    <p>Quantidade de barras percorridos: ${numberOfBars}</p>
                    <p>Número total de ordens efetuadas: ${numberOfTrades}</p>
                    <p>Período escolhido: ${periodoEscolhido}</p>
                    <br><p>***   ORDENS   ***</p>
                    <c:forEach items='${tradeList}' var='object'>
                        <tr>
                            <td>Compra: $${object.entry.price}</td>
                            <td> | Venda: $${object.exit.price}</td>
                            <td> | Lucro Bruto: $${object.exit.price - object.entry.price}</td>
                            <td> | Lucro Bruto: $${object.exit.price - object.entry.price}</td>
                            <br>
                        </tr>
                    </c:forEach>
                    <br>
                    <p>Lucro bruto: $${lucroBruto} (sobre 1 btc)</p>
                    <p>relação de lucratividade de ordens: ${profitableTradesRatio}</p>
                    <p>Diferença entre trade e HODL: ${profitVsBuyAndHold}</p>
                    <p>Máximo de retração: ${maximumDrawdown} (Medida de declínio depois de atingir um ponto máximo)</p>
                    <p>Total de custo de taxas: ${totalTransactionCosts} (valor suspeito de estar errado)</p>
                </div>
            </div>

            <br><br>

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

            <!-- HTML -->
            <div id="chartdiv" class="col-md-12"></div>


        </div>
    </jsp:body>
</template:admin>