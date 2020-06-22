<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="sortBy" required="true" %>
<%@attribute name="orderBy" required="true" %>

<c:url var="url" value="/productList">
    <c:if test="${not empty param.page}">
        <c:param name="page" value="${param.page}"/>
    </c:if>
    <c:if test="${not empty param.query}">
        <c:param name="query" value="${param.query}"/>
    </c:if>

    <c:param name="sortBy" value="${sortBy}"/>
    <c:param name="orderBy" value="${orderBy}"/>
</c:url>

<c:choose>
    <c:when test="${orderBy eq 'asc'}">
        <a href="${url}" class="text-right"><i class="fas fa-angle-up"></i></a>
    </c:when>
    <c:otherwise>
        <a href="${url}" class="text-right"><i class="fas fa-angle-down"></i></a>
    </c:otherwise>
</c:choose>
