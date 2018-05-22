<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Chart code -->
<div id="chartdiv"></div><br>
<script>
  // AmCharts.clear();
  var chartData = [];
  var tradeData = [];

  <c:forEach items='${tradeListForGraph}' var='tradeEvents'>
  var obj = JSON.parse(' ${tradeEvents} ');
  var eventDate = new Date(Number(obj.dateInMilis));
  eventDate.setDate( eventDate.getDate() );
  var hour = eventDate.getHours();
  var minute = eventDate.getMinutes();
  eventDate.setHours( hour, minute );

  tradeData.push({
    "date": eventDate,
    "type": obj.type,
    "backgroundColor": obj.backgroundColor,
    "graph": "g1",
    "text": obj.text,
    "description": obj.description
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
  chart = AmCharts.makeChart( "chartdiv", {
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
      "stockEvents": tradeData
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
