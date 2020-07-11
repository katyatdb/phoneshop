<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order">
    <div class="container mt-3 mb-3">
        <h2 class="mt-3">
                ${not empty order.orderItems ? "Order" : "Cart is empty"}
        </h2>
        <a href="<c:url value="/cart"/>" class="btn btn-outline-primary mt-3">
            Back to cart
        </a>

        <c:if test="${not empty order.orderItems}">
            <table class="table table-custom mt-3">
                <thead class="thead-light">
                <tr>
                    <th scope="col">Brand</th>
                    <th scope="col">Model</th>
                    <th scope="col">Color</th>
                    <th scope="col">Display size</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Price</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="orderItem" items="${order.orderItems}">
                    <tr>
                        <td>${orderItem.phone.brand}</td>
                        <td>${orderItem.phone.model}</td>
                        <td>
                            <c:if test="${empty orderItem.phone.colors}">&mdash;</c:if>

                            <c:forEach var="color" items="${orderItem.phone.colors}" varStatus="index">
                                ${color.code}<c:if test="${not index.last}">,</c:if>
                            </c:forEach>
                        </td>
                        <td>${orderItem.phone.displaySizeInches}"</td>
                        <td>${orderItem.quantity}</td>
                        <td>
                            <fmt:formatNumber type="currency" value="${orderItem.phone.price}" currencySymbol="$"/>
                        </td>
                    </tr>
                </c:forEach>

                <tr>
                    <td rowspan="3" colspan="4" id="empty-cell"></td>
                    <td>Subtotal</td>
                    <td>
                        <fmt:formatNumber type="currency" value="${order.subtotal}" currencySymbol="$"/>
                    </td>
                </tr>
                <tr>
                    <td>Delivery</td>
                    <td>
                        <fmt:formatNumber type="currency" value="${order.deliveryPrice}" currencySymbol="$"/>
                    </td>
                </tr>
                <tr id="total-price-row">
                    <td>TOTAL</td>
                    <td>
                        <fmt:formatNumber type="currency" value="${order.totalPrice}" currencySymbol="$"/>
                    </td>
                </tr>
                </tbody>
            </table>

            <form:form method="post" modelAttribute="userDataForm" cssClass="pl-3">
                <c:if test="${not empty outOfStockMessage}">
                    <div class="row mb-3">
                        <div class="col-4 error pl-0">${outOfStockMessage}</div>
                    </div>
                </c:if>

                <div class="form-group">
                    <div class="row">
                        <label for="firstName" class="col-2 pl-0">First name*</label>
                        <form:input path="firstName" id="firstName" cssClass="col-2 form-control"/>
                    </div>
                    <div class="row">
                        <form:errors path="firstName" cssClass="offset-2 col-2 error pl-0 pr-0"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <label for="lastName" class="col-2 pl-0">Last name*</label>
                        <form:input path="lastName" id="lastName" cssClass="col-2 form-control"/>
                    </div>
                    <div class="row">
                        <form:errors path="lastName" cssClass="offset-2 col-2 error pl-0 pr-0"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <label for="address" class="col-2 pl-0">Address*</label>
                        <form:input path="address" id="address" cssClass="col-2 form-control"/>
                    </div>
                    <div class="row">
                        <form:errors path="address" cssClass="offset-2 col-2 error pl-0 pr-0"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <label for="phone" class="col-2 pl-0">Phone*</label>
                        <form:input path="phone" id="phone" cssClass="col-2 form-control"/>
                    </div>
                    <div class="row">
                        <form:errors path="phone" cssClass="offset-2 col-2 error pl-0 pr-0"/>
                    </div>
                </div>
                <div class="form-group row">
                    <form:textarea path="additionalInfo" id="additional-info" cssClass="col-4 form-control pl-3"
                                   placeholder="Additional information"/>
                </div>

                <div class="form-group row">
                    <button type="submit" class="col-auto btn btn-outline-success pl-4 pr-4">Order</button>
                </div>
            </form:form>
        </c:if>
    </div>
</tags:master>
