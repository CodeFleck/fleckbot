<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="chartdivCandle"></div>
<!-- Chart code -->
<script>
  var chartData = [];

  <c:forEach items='${listaDeBarras}' var='bar'>

  var newDate = new Date( ${bar.customEndTimeForGraph} );
  newDate.setDate( newDate.getDate() );
  var hour = newDate.getHours();
  var minute = newDate.getMinutes();
  newDate.setHours( hour, minute );

  chartData.push( {
    "date": newDate,
    "open": "${bar.openPrice.toString()}",
    "high": "${bar.maxPrice.toString()}",
    "low": "${bar.minPrice.toString()}",
    "close": "${bar.closePrice.toString()}"
  } );
  </c:forEach>



  var chartCandle = AmCharts.makeChart( "chartdivCandle", {
    "type": "serial",
    "theme": "light",
    "dataDateFormat":"YYYY-MM-DD",
    "valueAxes": [ {
      "position": "left"
    } ],
    "graphs": [ {
      "id": "g2",
      "balloonText": "Open:<b>[[open]]</b><br>Low:<b>[[low]]</b><br>High:<b>[[high]]</b><br>Close:<b>[[close]]</b><br>",
      "closeField": "close",
      "fillColors": "#7f8da9",
      "highField": "high",
      "lineColor": "#7f8da9",
      "lineAlpha": 1,
      "lowField": "low",
      "fillAlphas": 0.9,
      "negativeFillColors": "#db4c3c",
      "negativeLineColor": "#db4c3c",
      "openField": "open",
      "title": "Price:",
      "type": "candlestick",
      "valueField": "close"
    } ],
    "chartScrollbar": {
      "graph": "g2",
      "graphType": "line",
      "scrollbarHeight": 30
    },
    "chartCursor": {
      "valueLineEnabled": true,
      "valueLineBalloonEnabled": true
    },
    "categoryField": "date",
    "categoryAxis": {
      "parseDates": true
    },
    "dataProvider": chartData,

    "export": {
      "enabled": true,
      "position": "bottom-right"
    }
  } );

  chartCandle.addListener( 'rendered', zoomChart );
  zoomChart();

  // this method is called when chart is first inited as we listen for "dataUpdated" event
  function zoomChart() {
    // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
    chartCandle.zoomToIndexes( 0, chartCandle.dataProvider.length - 1 );
  }
</script>


