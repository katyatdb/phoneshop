<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="isAdminPage" required="false" %>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
          integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.13.0/css/all.css"
          integrity="sha384-Bfad6CLCknfcloXFOyFnlgtENryhrpZCe29RTifKEixXQZ38WheV+i/6YWSzkz3V" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" crossorigin="anonymous"
            integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0="></script>
</head>
<body class="product-list mt-2">
<header>
    <div class="container">
        <div class="d-flex justify-content-between align-items-end">
            <div>
                <h1>
                    <a href="<c:url value="/productList"/>" id="logo">Phonify</a>
                </h1>
            </div>
            <c:choose>
                <c:when test="${isAdminPage eq true}">
                    <div class="d-flex flex-end mb-3">
                        <div>admin</div>
                        <a href="#" class="ml-3">Logout</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="d-flex flex-column align-items-end">
                        <a href="#" class="mb-3">Login</a>
                        <a href="<c:url value="/cart"/>" class="btn btn-outline-primary">
                            My cart:
                            <span id="cart-items-quantity"></span>
                            <span id="total-price"></span>
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <hr>
    </div>
</header>
<main>
    <jsp:doBody/>
</main>
<script>
    const ajaxCartUrl = "<c:url value="/ajaxCart"/>";
    const cartUrl = "<c:url value="/cart"/>";
</script>
<script src="<c:url value="/resources/js/cartCrud.js"/>"></script>
</body>
</html>