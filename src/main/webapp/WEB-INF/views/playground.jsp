<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>
        <div>
            <div class ="container container"><br>
                <h2 class="basic-title">Playground para testes</h2><br>
                <div class="well">
                    <form:form method="POST" action="/playground/load" modelAttribute="dataSet">
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
                            <td><input type="radio" name="strategyName" id="movingMomentumStrategy"></td>
                            <td>MovingMomentumStrategy</td>
                        </tr>
                        </tbody>
                    </table>
                        <button type="submit" class="btn btn-primary">Carregar Dados</button>
                    </form:form>
                </div>
            </div>
        </div>
    </jsp:body>
</template:admin>