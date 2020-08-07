<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <sec:csrfMetaTags/>

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

            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <div class="d-flex flex-end mb-3">
                    <div>
                        <a href="<c:url value="/admin/orders"/>">
                            <sec:authentication property="name"/>
                        </a>
                    </div>

                    <form method="post" action="<c:url value="/logout"/>" id="logout-form">
                        <sec:csrfInput/>
                        <button type="submit" class="btn btn-outline-dark ml-3 pt-0 pb-1">Logout</button>
                    </form>
                </div>
            </sec:authorize>

            <sec:authorize access="isAnonymous()">
                <div class="d-flex flex-column align-items-end">
                    <a href="<c:url value="/login"/>" class="mb-3">Login</a>
                    <a href="<c:url value="/cart"/>" class="btn btn-outline-primary">
                        My cart:
                        <span id="cart-items-quantity"></span>
                        <span id="total-price"></span>
                    </a>
                </div>
            </sec:authorize>
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