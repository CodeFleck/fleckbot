;(function () {

  'use strict';

  // In a new Javascript file create a variable for new websocket
  var ws = new WebSocket('wss://api.bitfinex.com/ws');

  window.Bitfinex = ws;

})();
