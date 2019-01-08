<%@ page language="java" contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <style>
        .paypal-button button.large {
            min-width: 150px;
        }

        .paypal-label {
            margin-left: 30px;
        }

        .paypal-input {
            margin: 0 20px !important;
            width: 100px !important;
            height: 25px !important;
            border-radius: 5px !important;
            text-align: center !important;
        }

        .paypal-button {
            min-height: 40px;
        }

        .textPay {
            text-align: center;
            margin: 10px 50px;
            font-size: 13px;
            display: block;
        }
        #usd-button form ,
        #brl-button form {
            margin: 0;
        }

        #usd-button img,
        #brl-button img {
            margin: 20px auto 0 auto;
            display: block;
        }
        .videoContainer {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            bottom: 0;
            right: 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        iframe {
            /* optional */
            width: 100%;
            height: 100%;
        }


    </style><title>Home page</title>
</head>
<body>
<div class="videoContainer">
    <iframe width="560" height="315" src="https://www.youtube.com/embed/dqcotlroLuI?rel=0&autoplay=1;controls=0&amp;showinfo=0" frameborder="0"
            allow="autoplay; encrypted-media" allowfullscreen></iframe>
</div>
</body>
</html>