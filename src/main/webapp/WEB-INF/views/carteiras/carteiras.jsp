<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">
          google.charts.load('current', {'packages':['corechart']});
          google.charts.setOnLoadCallback(drawChart);

          function drawChart() {

            var data = google.visualization.arrayToDataTable([
              ['Task', 'Hours per Day'],
              ['USD',     ${carteiras.balancesAvailable.get("USD")}],
              ['Bitcoin',     ${carteiras.balancesAvailable.get("BTC")}],
              ['Bitcoin Cash',  ${carteiras.balancesAvailable.get("BCH")}],
              ['Dash', ${carteiras.balancesAvailable.get("DSH")}],
              ['Iota', ${carteiras.balancesAvailable.get("IOT")}],
              ['Litecoin', ${carteiras.balancesAvailable.get("LTC")}],
              ['Quantum', ${carteiras.balancesAvailable.get("QTM")}],
              ['Santiment', ${carteiras.balancesAvailable.get("SAN")}],
              ['Monero', ${carteiras.balancesAvailable.get("XMR")}],
              ['Ripple', ${carteiras.balancesAvailable.get("XRP")}]
            ]);

            var options = {
              title: '',
              backgroundColor: '#eee'
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart'));

            chart.draw(data, options);
          }
        </script>


        <div class="container-fluid">
            <div class="col-md-5 float-left">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col">Carteiras</th>
                        <th scope="col">Disponível</th>
                        <th scope="col">Em uso</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row"><img src="/assets/imagens/coins/usd.png" alt="usd" width="32" height="32"/></th>
                        <td>US Dolar</td>
                        <td>${carteiras.balancesAvailable.get("USD")}</td>
                        <td>${carteiras.balancesOnHold.get("USD")}</td>
                    </tr>
                    <tr>
                        <th scope="row"><img src="/assets/imagens/coins/btc.png" alt="Bitcoin" width="32" height="32"/></th>
                        <td>Bitcoin</td>
                        <td>${carteiras.balancesAvailable.get("BTC")}</td>
                        <td>${carteiras.balancesOnHold.get("BTC")}</td>
                    </tr>
                    <tr>
                        <th scope="row"><img src="/assets/imagens/coins/bch.png" alt="Bitcoin Cash" width="32" height="32"/></th>
                        <td>Bitcoin Cash</td>
                        <td>${carteiras.balancesAvailable.get("BCH")}</td>
                        <td>${carteiras.balancesOnHold.get("BCH")}</td>
                    </tr>
                    <tr>
                        <th scope="row"><img src="/assets/imagens/coins/dash.png" alt="Dash" width="32" height="32"/></th>
                        <td>Dash</td>
                        <td>${carteiras.balancesAvailable.get("DSH")}</td>
                        <td>${carteiras.balancesOnHold.get("DSH")}</td>
                    </tr>
                    <tr>
                        <th scope="row"><img src="/assets/imagens/coins/miota.png" alt="Iota" width="32" height="32"/></th>
                        <td>Iota</td>
                        <td>${carteiras.balancesAvailable.get("IOT")}</td>
                        <td>${carteiras.balancesOnHold.get("IOT")}</td>
                    </tr>
                    <tr>
                        <th
                                scope="row"><img src="/assets/imagens/coins/ltc.png" alt="Litecoin" width="32"
                                                 height="32"/></th>
                        <td>Litecoin</td>
                        <td>${carteiras.balancesAvailable.get("LTC")}</td>
                        <td>${carteiras.balancesOnHold.get("LTC")}</td>
                    </tr>
                    <tr>
                        <th
                                scope="row"><img src="/assets/imagens/coins/qtum.png" alt="Quantum" width="32"
                                                 height="32"/></th>
                        <td>Quantum</td>
                        <td>${carteiras.balancesAvailable.get("QTM")}</td>
                        <td>${carteiras.balancesOnHold.get("QTM")}</td>
                    </tr>
                    <tr>
                        <th
                                scope="row"><img src="/assets/imagens/coins/san.png" alt="Santiment" width="32"
                                                 height="32"/></th>
                        <td>Santiment</td>
                        <td>${carteiras.balancesAvailable.get("SAN")}</td>
                        <td>${carteiras.balancesOnHold.get("SAN")}</td>
                    </tr>
                    <tr>
                        <th
                                scope="row"><img src="/assets/imagens/coins/xmr.png" alt="Monero" width="32" height="32"/></th>
                        <td>Monero</td>
                        <td>${carteiras.balancesAvailable.get("XMR")}</td>
                        <td>${carteiras.balancesOnHold.get("XMR")}</td>
                    </tr>
                    <tr>
                        <th
                                scope="row"><img src="/assets/imagens/coins/xrp.png" alt="Ripple" width="32" height="32"/></th>
                        <td>Ripple</td>
                        <td>${carteiras.balancesAvailable.get("XRP")}</td>
                        <td>${carteiras.balancesOnHold.get("XRP")}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="col-md-7 float-right">
                <div id="piechart" style="width: 900px; height: 500px; background: #f8f9fa;"></div>
            </div>
        </div>
    </jsp:body>
</template:admin>
