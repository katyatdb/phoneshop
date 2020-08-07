<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<tags:master pageTitle="Order Details">
    <div class="container mt-3 mb-3">
        <div class="d-flex justify-content-between mt-3">
            <h4>Order number: ${order.id}</h4>
            <h4>Order status: ${order.status}</h4>
        </div>

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

        <div>
            <div class="row mb-3">
                <div class="col-2">First name</div>
                <div class="col">${order.firstName}</div>
            </div>
            <div class="row mb-3">
                <div class="col-2">Last name</div>
                <div class="col">${order.lastName}</div>
            </div>
            <div class="row mb-3">
                <div class="col-2">Address</div>
                <div class="col">${order.deliveryAddress}</div>
            </div>
            <div class="row mb-3">
                <div class="col-2">Phone</div>
                <div class="col">${order.contactPhoneNo}</div>
            </div>
            <div class="row mb-3">
                <div class="col-6">${order.additionalInfo}</div>
            </div>
        </div>

        <form method="post">
            <div class="row">
                <a href="<c:url value="/admin/orders"/>" class="col-2 btn btn-outline-primary ml-3">Back</a>
                <sec:csrfInput/>

                <button type="submit" name="status" value="DELIVERED" class="col-2 btn btn-outline-primary ml-2">
                    Delivered
                </button>
                <button type="submit" name="status" value="REJECTED" class="col-2 btn btn-outline-primary ml-2">
                    Rejected
                </button>
            </div>
        </form>
    </div>
</tags:master>