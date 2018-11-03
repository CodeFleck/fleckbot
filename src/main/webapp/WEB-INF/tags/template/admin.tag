<%@attribute name="extraScripts" fragment="true"%>
<%@attribute name="extraStyles" fragment="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>FleckBot</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <!-- bootstrap -->

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <!-- style -->
    <link rel="stylesheet" href="<c:url value='/assets/css/index.css'/>">
    <link rel="stylesheet" href="<c:url value='/assets/css/forms.css'/>">
    <jsp:invoke fragment="extraStyles"/>
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

</head>

<body>

<!-- INICIO NAV -->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">FleckBot</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <%--<li class="nav-item">--%>
                <%--<a class="nav-link" href="<c:url value='/noticias'/>">Not&iacute;cias</a>--%>
            <%--</li>--%>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value='/analise'/>">An&aacute;lise</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value='/carteiras'/>">Carteiras</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value='/playground'/>">Playground</a>
            </li>
            <li class= "nav-item">
            <c:if test="${pageContext.request.isUserInRole('ADMIN')}">
                <a class='nav-link' href="<c:url value='/admin/redes-neurais'/>"><c:out value="Redes Neurais" /></a>
            </c:if>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value='/trading'/>">Logs</a>
            </li>
        </ul>
        <form class="form-inline my-5 my-lg-0">
            <div id="botStatus">
                <c:choose>
                    <c:when test="${botStatus}">
                        <p class="">Status: Ligado
                            <a href="/trading/desligar" class="btn btn-danger btn-sm" id="desligar">Desligar</a>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p class="">Status: Desligado
                            <a href="/trading/ligar" class="btn btn-success btn-sm" id="ligar">Ligar</a>
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
        <c:url var="logoutUrl" value="/logout"/>
        <form class="form-inline" action="${logoutUrl}" method="post">
            <input type="submit" class="btn btn-light btn-sm" id="btnlogout" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
</nav>

<!-- FINAL NAV -->
<jsp:doBody />

<jsp:invoke fragment="extraScripts"/>

</body>
</html>