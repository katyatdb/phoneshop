<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="currentPage" required="true" %>
<%@ attribute name="pagesNumber" required="true" %>

<nav aria-label="Page navigation">
    <ul class="pagination justify-content-end">
        <c:if test="${currentPage ne 1}">
            <li class="page-item">
                <a class="page-link" href="<tags:pageUrl page="1"/>" aria-label="First">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<tags:pageUrl page="${currentPage - 1}"/>" aria-label="Previous">
                    <span aria-hidden="true">&LT;</span>
                </a>
            </li>
        </c:if>

        <c:choose>
            <c:when test="${pagesNumber le 9}">
                <c:forEach var="i" begin="1" end="${pagesNumber}">
                    <li class="page-item">
                        <a class="page-link ${i eq currentPage ? "active" : ""}"
                           href="<tags:pageUrl page="${i}"/>">${i}</a>
                    </li>
                </c:forEach>
            </c:when>
            <c:when test="${pagesNumber gt 9 && currentPage le 4}">
                <c:forEach var="i" begin="1" end="9">
                    <li class="page-item ${i eq currentPage ? "active" : ""}">
                        <a class="page-link" href="<tags:pageUrl page="${i}"/>">${i}</a>
                    </li>
                </c:forEach>
            </c:when>
            <c:when test="${pagesNumber gt 9 && pagesNumber - currentPage le 4}">
                <c:forEach var="i" begin="${pagesNumber - 9 + 1}" end="${pagesNumber}">
                    <li class="page-item ${i eq currentPage ? "active" : ""}">
                        <a class="page-link" href="<tags:pageUrl page="${i}"/>">${i}</a>
                    </li>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:forEach var="i" begin="${currentPage - 4}" end="${currentPage + 4}">
                    <li class="page-item ${i eq currentPage ? "active" : ""}">
                        <a class="page-link" href="<tags:pageUrl page="${i}"/>">${i}</a>
                    </li>
                </c:forEach>
            </c:otherwise>
        </c:choose>

        <c:if test="${currentPage ne pagesNumber}">
            <li class="page-item">
                <a class="page-link" href="<tags:pageUrl page="${currentPage + 1}"/>" aria-label="Next">
                    <span aria-hidden="true">&GT;</span>
                </a>
            </li>
            <li class="page-item">
                <a class="page-link" href="<tags:pageUrl page="${pagesNumber}"/>" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </c:if>
    </ul>
</nav>