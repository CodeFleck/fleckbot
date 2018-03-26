<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
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
                    <form:form method="POST" action="/playground/load">
                    <table class="table table-condensed table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <td>Corretora</td>
                            <td>Moeda Base</td>
                            <td>Ativo</td>
                            <td>De</td>
                            <td>At&eacute;</td>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Coinbase</td>
                                <td>USD</td>
                                <td>BTC</td>
                                <td> <input type="date" name="beginDate" min="2014-01-01" max="2018-01-08"></td>
                                <td><input type="date" name="endDate" min="2014-01-01" max="2018-01-08"></td>
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
                            <td>Minha estrategia</td>
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