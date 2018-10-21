;(function () {

  'use strict';

  var bookId;
  var tickerId;

  // Create function to send on open
  Bitfinex.onopen = function() {
    Bitfinex.send(JSON.stringify({"event":"subscribe", "channel":"ticker", "pair":"BTCUSD"}));
    Bitfinex.send(JSON.stringify({"event":"subscribe", "channel":"book", "pair":"BTCUSD", "prec":"R0"}));
  };

  // Tell function what to do when message is received and log messages to "btc" div
  Bitfinex.onmessage = function(msg) {
    // create a variable for response and parse the json data
    var response = JSON.parse(msg.data);

//    console.log('response', response);
    // save hb variable from bitfinex
    var hb = response[1];
    if(hb != "hb") {
      if (response.channel) {
        switch (response.channel) {
          case 'book':
            bookId = response.chanId;
            break;
          case 'ticker':
            tickerId = response.chanId;
            break;
        }
        return;
      }

      var channelId = response[0];
      switch (channelId) {
        case bookId:
          responseOrder(response);
          break;
        case tickerId:
          responseTicker(response);
          break;
      }
    } else {
      // console.error(response);
    }
  };

  function responseOrder(data) {
//    console.log('order', data);

    var ORD_ID = data[1];
    var ORD_PRICE = data[2];
    var AMOUNT = data[3];
    var LENGTH = data[4]; //number of price points

    if (AMOUNT <= 0) {
      document.getElementById("orderBookAsk").innerHTML = "ID: " + ORD_ID + " | QTD: " + AMOUNT + " | pre&ccedil;o: " + ORD_PRICE;
    } else{
      document.getElementById("orderBookBid").innerHTML = "ID: " + ORD_ID + " | QTD: " + AMOUNT + " | pre&ccedil;o: " + ORD_PRICE;
    }
  }

  function responseTicker(data) {

    var LAST_PRICE = data[7];
    var LAST_PRICE_FORMATTED = LAST_PRICE.toFixed(2);
    var ASK = data[3];
    var BID = data[1];
    var VOLUME = data[8];
    var HIGH = data[9];
    var LOW = data[10];

    // document.getElementById("btc").innerHTML = "preco: " + LAST_PRICE + "<br> compra: " + ASK + " | venda: " + BID
    //   + "<br>volume: " + VOLUME + "<br> alta: " + HIGH + "  |  baixa: " + LOW;
    document.getElementById("btc").innerHTML = LAST_PRICE_FORMATTED;
  }

})();
