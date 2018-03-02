<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>

        <div class="container-fluid">
        <div style="height: 610px;">
            <!-- TradingView Widget BEGIN -->
            <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
            <script type="text/javascript">
              new TradingView.widget({
                "container_id": 'analytics-platform',
                "autosize": true,
                "symbol": "BITFINEX:BTCUSD",
                "interval": "240",
                "timezone": "America/Sao_Paulo",
                "theme": "Light",
                "style": "1",
                "locale": "br",
                "toolbar_bg": "#f1f3f6",
                "enable_publishing": false,
                "withdateranges": true,
                "hide_side_toolbar": false,
                "allow_symbol_change": true,
                "save_image": false,
                "details": true,
                "news": [ "headlines" ],
                "hideideas": true,
                "show_popup_button": true,
                "popup_width": "1000",
                "popup_height": "650"
              });
            </script>
            <!-- TradingView Widget END -->

        </div>

        <%--noticias--%>
        <div>
            <div class="btcwdgt-news" bw-entries="7" bw-theme="light"></div>
            <script>
              (function(b,i,t,C,O,I,N) {
                window.addEventListener('load',function() {
                  if(b.getElementById(C))return;
                  I=b.createElement(i),N=b.getElementsByTagName(i)[0];
                  I.src=t;I.id=C;N.parentNode.insertBefore(I, N);
                },false)
              })(document,'script','https://widgets.bitcoin.com/widget.js','btcwdgt');
            </script>
        </div>
        <div class="col-md-4 float-right">
            <a class="twitter-timeline" data-dnt="true" href="https://twitter.com/search?q=%23bitcoin%20%23btc" data-widget-id="961382940021227520">Tweets about #bitcoin #btc</a>
            <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
        </div>

        <%--reddit--%>
        <div class="col-md-8" style="margin-bottom: 3px;">
            <script src="https://www.reddit.com/r/CryptoCurrency/hot/.embed?limit=7&t=week" type="text/javascript" style="width: 100%;" />
        </div>

    </jsp:body>
</template:admin>