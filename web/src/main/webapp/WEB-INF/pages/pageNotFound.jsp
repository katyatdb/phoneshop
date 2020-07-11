<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="404 Error">
    <div class="container mt-4">
        <h1>Page not found</h1>
        <a href="<c:url value="/productList"/>" class="btn btn-outline-primary mt-3">
            Back to product list
        </a>
    </div>
</tags:master>
