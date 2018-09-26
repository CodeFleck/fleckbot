<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="ISO-8859-1" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Daniel Fleck">

    <title>Signin</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="<c:url value='/assets/css/signin.css'/>">
</head>
<body>
<div class="container">
    <div class="text-center">
        <c:url value="/login" var="loginUrl"/>
        <form class="form-signin" action="${loginUrl}" method="post">
            <div class="welcome">
                <div class="imglogo"><img class="mb-4" src="<c:url value='/assets/imagens/logo.png'/>" alt="logoFleckBot" width="172" height="172"></div>
                <h1 class="h3 mb-3 font-weight-normal">Fleckbot</h1>
            </div>
            <c:if test="${param.error != null}">
                <p style='color:red'>
                    Usuário e/ou senha inválidos.
                </p>
            </c:if>
            <c:if test="${param.logout != null}">
                <p style='color:blue'>
                    Você se deslogou.
                </p>
            </c:if>
            <div class="loginform">
            <div class="form-group row">
                <label for="username" class="col-sm-2 col-form-label">Usuário</label>
                <div class="col-sm-10">
                    <input type="text" id="username" name="username" class="form-control" placeholder="Digite seu usuário" required autofocus/>
                </div>
            </div>
            <div class="form-group row">
                <label for="password" class="col-sm-2 col-form-label">Senha</label>
                <div class="col-sm-10">
                    <input type="password" id="password" name="password" class="form-control" placeholder="Senha" required/>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-lg btn-primary btn-block btnlogin">Log in</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
