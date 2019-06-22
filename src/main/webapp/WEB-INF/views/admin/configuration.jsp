<%@ page contentType="text/html" pageEncoding="ISO-8859-1" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<template:admin>
    <jsp:attribute name="extraScripts" />

    <jsp:body>
        <div class="container-fluid"><br>
            <h2 class="basic-title">Configurações</h2><br><br>

            <h5 class="float-left">Transportar dados:</h5><br><br>
            <form:form method="POST" action="/admin/configuration/transport-data" class="form-inline">
                <div class="form-group mb-2 col-md-3">
                    <label for="field" class="sr-only">nao sei</label>
                    <input type="text" readonly class="form-control-plaintext" id="field" value="Transportar informação do arquivo csv para o banco de dados:">
                </div>
                <button type="submit" class="btn btn-primary mb-2">Transportar</button>
            </form:form>

            <br>
            <h5 class="float-left">Trainar modelos:</h5><br><br>
            <div class="col-md-6 float-left">
                <form:form method="POST" action="/admin/configuration/generate-model">
                    <table class="table table-condensed table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <td>Ativo</td>
                            <td>Categoria</td>
                            <td>Período</td>
                            <td></td>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <select class="form-control selectpicker" name="coin">
                                    <option>BTC</option>
                                </select>
                            </td>
                            <td>
                                <select class="form-control selectpicker" name="category">
                                    <option>Fechamento</option>
                                    <option>Abertura</option>
                                    <option>Maior</option>
                                    <option>Menor</option>
                                    <option>Volume</option>
                                </select>
                            </td>
                            <td><select class="form-control selectpicker" name="period">
                                <option>1 minuto</option>
                                <option>5 minutos</option>
                                <option>10 minutos</option>
                                <option>15 minutos</option>
                                <option>30 minutos</option>
                                <option>1 hora</option>
                                <option>2 horas</option>
                                <option>4 horas</option>
                                <option>1 dia</option>
                                <option>1 semana</option>
                                <option>1 mes</option>
                            </select>
                            </td>
                            <td><button type="submit" class="btn btn-primary">Gerar modelo</button></td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
    </jsp:body>
</template:admin>
