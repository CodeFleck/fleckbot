function populateGraph(data){



  var bars = [];

  if (!Date.now) {
    Date.now = function() { return new Date().getTime(); }
   var DATE = Math.floor(Date.now() / 1000)
  }

  var LAST_PRICE = data[7];
  var HIGH = data[9];
  var LOW = data[10];

  for (i = 0; i < 60; i++) {

//tratar como vai ser o primeiro candle e o resto... tvz uma flag
    var bar = { date: DATE, open: LAST_PRICE, high: HIGH, low: LOW, close: "0"};

    if (bar.date >= bar.date + 60){
      bars.push(bar)
    }

  }

  var OPEN_TIME = function formatDate() {
    var d = new Date(),
      month = '' + (d.getMonth() + 1),
      day = '' + d.getDate(),
      year = '' + d.getFullYear()

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }



  console.log("*** Entrei no populateGraph ***", data)

  var chart = AmCharts.makeChart( "chartdiv", {
    "type": "serial",
    "theme": "none",
    "dataDateFormat":"YYYY-MM-DD",
    "valueAxes": [ {
      "position": "left"
    } ],
    "graphs": [ {
      "id": "g1",
      "proCandlesticks": true,
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
      "graph": "g1",
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
    "dataProvider": [ {
      "date": OPEN_TIME,
      "open": LAST_PRICE,
      "high": HIGH,
      "low": LOW,
      "close": "8900.49"
    } ],
    "export": {
      "enabled": true,
      "position": "bottom-right"
    }
  } );

  chart.addListener( "rendered", zoomChart );
  zoomChart();

// this method is called when chart is first inited as we listen for "dataUpdated" event
  function zoomChart() {
    // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
    chart.zoomToIndexes( chart.dataProvider.length - 50, chart.dataProvider.length - 1 );
  }
}