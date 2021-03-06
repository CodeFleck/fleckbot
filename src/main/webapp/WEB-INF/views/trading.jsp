<%@ page contentType="text/html" pageEncoding="ISO-8859-1" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<template:admin>
<jsp:attribute name="extraStyles">
<link rel="stylesheet" href="<c:url value='/assets/css/pagination/jqpagination.css'/>" />
</jsp:attribute>
<jsp:attribute name="extraScripts">
<script src="<c:url value='/assets/js/vendors/jquery.jqpagination.js'/>"></script>
</jsp:attribute>
    <jsp:body>
        <div>
            <div class ="container min-container">
                <h5 class="basic-title">Logs: </h5>
                <div class="well">
                    <table class="table table-condensed table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <td>id</td>
                            <td>data e hora</td>
                            <td>descricao</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items='${paginatedList.currentList}' var='object'>
                            <tr>
                                <td>${object.id}</td>
                                <td>${object.created}</td>
                                <td>${object.description}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <template:paginationComponent paginatedList="${paginatedList}" page="${param.page}" action="/trading"/>
                </div>
            </div>
        </div>



        <%--<script src="https://www.amcharts.com/lib/3/amcharts.js"></script>--%>
        <%--<script src="https://www.amcharts.com/lib/3/serial.js"></script>--%>
        <%--<script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>--%>
        <%--<link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />--%>
        <%--<script src="https://www.amcharts.com/lib/3/themes/light.js"></script>--%>
        <%--<div id="chartdiv"></div>--%>

        <%--<script>--%>

        <%--var dataPoints = [];--%>

        <%--var populatedDataPoint = [];--%>

        <%--for (i = 0; i < ${series.barCount}; i++) {--%>

        <%--var str = "${series.getBar(0).getBeginTime()}";--%>
        <%--var date1 = str.substring(0, 9);--%>

        <%--populatedDataPoint.push({--%>
        <%--date :  "date1",--%>
        <%--open : "${series.getBar(0).getOpenPrice()}",--%>
        <%--high : "${series.getBar(0).getMaxPrice()}",--%>
        <%--low : "${series.getBar(0).getMinPrice()}",--%>
        <%--close : "${series.getBar(0).getClosePrice()}"--%>
        <%--});--%>
        <%--}--%>

        <%--console.log('populatedDataPoint: ', populatedDataPoint);--%>

        <%--var chart = AmCharts.makeChart( "chartdiv", {--%>
        <%--"type": "serial",--%>
        <%--"theme": "light",--%>
        <%--"dataDateFormat":"YYYY-MM-DD",--%>
        <%--"valueAxes": [ {--%>
        <%--"position": "left"--%>
        <%--} ],--%>
        <%--"graphs": [ {--%>
        <%--"id": "g1",--%>
        <%--"balloonText": "Open:<b>[[open]]</b><br>Low:<b>[[low]]</b><br>High:<b>[[high]]</b><br>Close:<b>[[close]]</b><br>",--%>
        <%--"closeField": "close",--%>
        <%--"fillColors": "#7f8da9",--%>
        <%--"highField": "high",--%>
        <%--"lineColor": "#7f8da9",--%>
        <%--"lineAlpha": 1,--%>
        <%--"lowField": "low",--%>
        <%--"fillAlphas": 0.9,--%>
        <%--"negativeFillColors": "#db4c3c",--%>
        <%--"negativeLineColor": "#db4c3c",--%>
        <%--"openField": "open",--%>
        <%--"title": "Price:",--%>
        <%--"type": "candlestick",--%>
        <%--"valueField": "close"--%>
        <%--} ],--%>
        <%--"chartScrollbar": {--%>
        <%--"graph": "g1",--%>
        <%--"graphType": "line",--%>
        <%--"scrollbarHeight": 30--%>
        <%--},--%>
        <%--"chartCursor": {--%>
        <%--"valueLineEnabled": true,--%>
        <%--"valueLineBalloonEnabled": true--%>
        <%--},--%>
        <%--"categoryField": "date",--%>
        <%--"categoryAxis": {--%>
        <%--"parseDates": true--%>
        <%--},--%>
        <%--"dataProvider": populatedDataPoint,--%>
        <%--"export": {--%>
        <%--"enabled": true,--%>
        <%--"position": "bottom-right"--%>
        <%--}--%>
        <%--} );--%>

        <%--chart.addListener( "rendered", zoomChart );--%>
        <%--zoomChart();--%>

        <%--// this method is called when chart is first inited as we listen for "dataUpdated" event--%>
        <%--function zoomChart() {--%>
        <%--// different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues--%>
        <%--chart.zoomToIndexes( chart.dataProvider.length - 10, chart.dataProvider.length - 1 );--%>
        <%--}--%>
        <%--</script>--%>

    </jsp:body>
</template:admin>
