<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>
        <div>
            <div class ="container container"><br>
                <h2 class="basic-title">Playground para testes</h2><br>
                <div class="well">
                    <table class="table table-condensed table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <td></td>
                            <td>Corretora</td>
                            <td>Moeda Base</td>
                            <td>Ativo</td>
                            <td>De</td>
                            <td>At&eacute;</td>
                            <td>Dura&ccedil;&atilde;o</td>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><input type="radio" name="dataSizeName" id="coinbaseLong"></td>
                                <td>Coinbase</td>
                                <td>USD</td>
                                <td>BTC</td>
                                <td>01/12/2014</td>
                                <td>08/01/2018</td>
                                <td>3 anos, 1 m&ecirc;s e 8 dias</td>
                            </tr>
                            <tr>
                                <td><input type="radio" name="dataSizeName" id="coinbaseMini"></td>
                                <td>Coinbase</td>
                                <td>USD</td>
                                <td>BTC</td>
                                <td>0X/XX/20XX</td>
                                <td>08/01/2018</td>
                                <td>X anos, X m&ecirc;s e X dias</td>
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
                            <td><input type="radio" name="StrategyName" id="movingMomentumStrategy"></td>
                            <td>MovingMomentumStrategy</td>
                        </tr>
                        </tbody>
                    </table>
                    <a href="<c:url value='/playground/load'/>" class="btn btn-success"><span class="glyphicon glyphicon-plus-sign"></span> Carregas
                        dados</a>
                </div>
            </div>
        </div>
    </jsp:body>
</template:admin>