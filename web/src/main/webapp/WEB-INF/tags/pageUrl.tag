<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="page" required="true" %>

<c:url value="/productList">
    <c:param name="page" value="${page}"/>

    <c:if test="${not empty param.query}">
        <c:param name="query" value="${param.query}"/>
    </c:if>
    <c:if test="${not empty param.sortBy}">
        <c:param name="sortBy" value="${param.sortBy}"/>
    </c:if>
    <c:if test="${not empty param.orderBy}">
        <c:param name="orderBy" value="${param.orderBy}"/>
    </c:if>
</c:url>