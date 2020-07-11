<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:master pageTitle="Cart">
    <div class="container mt-3 mb-3">
        <h2 class="mt-3">
                ${not empty cart.cartItems ? "Cart" : "Cart is empty"}
        </h2>
        <a href="<c:url value="/productList"/>" class="btn btn-outline-primary mt-3">
            Back to product list
        </a>

        <c:if test="${not empty cart.cartItems}">
            <table class="table table-bordered mt-3">
                <thead class="thead-light">
                <tr>
                    <th id="brand-col" scope="col">Brand</th>
                    <th id="model-col" scope="col">Model</th>
                    <th id="color-col" scope="col">Color</th>
                    <th id="display-size-col" scope="col">Display size</th>
                    <th id="price-col" scope="col">Price</th>
                    <th id="quantity-col" scope="col">Quantity</th>
                    <th id="action-col" scope="col">Action</th>
                </tr>
                </thead>

                <tbody>
                <form:form method="put" modelAttribute="cartItemListForm" id="update-cart-form">
                    <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">
                        <tr id="cart-item-${cartItem.phone.id}">
                            <td>${cartItem.phone.brand}</td>
                            <td>
                                <a href="<c:url value="/productDetails/${cartItem.phone.id}"/>">
                                        ${cartItem.phone.model}
                                </a>
                            </td>
                            <td>
                                <c:if test="${empty cartItem.phone.colors}">&mdash;</c:if>

                                <c:forEach var="color" items="${cartItem.phone.colors}" varStatus="index">
                                    ${color.code}<c:if test="${not index.last}">,</c:if>
                                </c:forEach>
                            </td>
                            <td>${cartItem.phone.displaySizeInches}"</td>
                            <td>
                                <fmt:formatNumber type="currency" value="${cartItem.phone.price}" currencySymbol="$"/>
                            </td>

                            <td>
                                <c:set var="cartItems" value="${cartItemListForm.cartItems}"/>
                                <c:set var="i" value="${status.index}"/>

                                <form:hidden path="cartItems[${i}].id"/>
                                <form:input path="cartItems[${i}].quantity" id="item-quantity-${cartItem.phone.id}"
                                            cssClass="form-control text-right"/>
                                <div class="error text-right">
                                    <form:errors path="cartItems[${i}].quantity"/>
                                </div>
                            </td>
                            <td class="text-center">
                                <button type="button" id="delete-from-cart-${cartItem.phone.id}"
                                        class="btn btn-outline-danger">
                                    Delete
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </form:form>
                </tbody>
            </table>

            <form:form method="delete" id="delete-cart-item-form">
                <input type="hidden" name="id" id="delete-cart-item-id"/>
            </form:form>

            <div class="text-right mt-4">
                <button type="button" id="update-cart-btn" class="btn btn-outline-primary mr-2 pr-4 pl-4">
                    Update
                </button>
                <a href="<c:url value="/order"/>" type="button" class="btn btn-outline-primary pr-4 pl-4">
                    Order
                </a>
            </div>
        </c:if>
    </div>
</tags:master>