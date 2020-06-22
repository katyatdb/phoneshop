<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:master pageTitle="Product List">
    <div class="container mt-5">
        <div class="row">
            <div class="col">
                <h3>Phones</h3>
            </div>
            <div class="col">
                <form method="get" class="form-inline float-right">
                    <input class="form-control mr-2" type="text" name="query" value="${param.query}"/>
                    <button class="btn btn-outline-primary">Search</button>
                </form>
            </div>
        </div>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th id="image-col" scope="col">Image</th>
                <th id="brand-col" scope="col">
                    Brand
                    <span class="float-right">
                        <tags:sortLink sortBy="brand" orderBy="asc"/>
                        <tags:sortLink sortBy="brand" orderBy="desc"/>
                    </span>
                </th>
                <th id="model-col" scope="col">
                    Model
                    <span class="float-right">
                        <tags:sortLink sortBy="model" orderBy="asc"/>
                        <tags:sortLink sortBy="model" orderBy="desc"/>
                    </span>
                </th>
                <th id="color-col" scope="col">Color</th>
                <th id="display-size-col" scope="col">
                    Display size
                    <span class="float-right">
                        <tags:sortLink sortBy="displaySizeInches" orderBy="asc"/>
                        <tags:sortLink sortBy="displaySizeInches" orderBy="desc"/>
                    </span>
                </th>
                <th id="price-col" scope="col">
                    Price
                    <span class="float-right">
                        <tags:sortLink sortBy="price" orderBy="asc"/>
                        <tags:sortLink sortBy="price" orderBy="desc"/>
                    </span>
                </th>
                <th id="quantity-col" scope="col">Quantity</th>
                <th id="action-col" scope="col">Action</th>
            </tr>
            </thead>
            <spring:message code="phoneImageUrl" var="imageUrl"/>
            <c:forEach var="phone" items="${paginationResult.pageItems}">
                <tr>
                    <td>
                        <img src="${imageUrl}${phone.imageUrl}">
                    </td>
                    <td>${phone.brand}</td>
                    <td>${phone.model}</td>
                    <td>
                        <c:forEach var="color" items="${phone.colors}" varStatus="index">
                            ${color.code}
                            <c:if test="${not index.last}">, </c:if>
                        </c:forEach>
                    </td>
                    <td>${phone.displaySizeInches}"</td>
                    <td>$ ${phone.price}</td>
                    <td>
                        <input type="text" id="item-quantity-${phone.id}" value="1"/>
                        <div id="item-quantity-error-${phone.id}"></div>
                    </td>
                    <td>
                        <button type="button" id="add-to-cart-${phone.id}" class="btn btn-outline-primary">
                            Add to
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${paginationResult.itemsNumber > 0}">
            <tags:pagination currentPage="${not empty param.page ? param.page : 1}"
                             pagesNumber="${paginationResult.pagesNumber}"/>
        </c:if>
    </div>
</tags:master>