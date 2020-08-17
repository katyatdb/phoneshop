<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Quick Order">
    <div class="container mt-3 mb-3">
        <a href="<c:url value="/productList"/>" class="btn btn-outline-primary mt-3">
            Back to product list
        </a>

        <c:forEach var="message" items="${success}">
            <div class="success">${message}</div>
        </c:forEach>

        <form:form method="post" modelAttribute="quickCartForm">
            <table class="table table-bordered mt-3 w-25">
                <thead class="thead-light">
                <tr>
                    <th id="code-col" scope="col" class="col-1">Product code</th>
                    <th id="quantity-col" scope="col" class="col-2">Quantity</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="cartItem" items="${quickCartForm.cartItems}" varStatus="status">
                    <c:set var="i" value="${status.index}"/>
                <tr>
                    <td>
                        <form:input path="cartItems[${i}].code" cssClass="form-control"/>
                        <form:errors path="cartItems[${i}].code" cssClass="error"/>
                    </td>

                    <td>
                        <form:input path="cartItems[${i}].quantity" cssClass="form-control"/>
                        <form:errors path="cartItems[${i}].quantity" cssClass="error"/>
                    </td>
                </tr>
                </c:forEach>
            </table>

            <div class="mt-4">
                <button type="submit" class="btn btn-outline-primary mr-2 pr-4 pl-4">
                    Add to cart
                </button>
                <a href="<c:url value="/order"/>" type="button" class="btn btn-outline-primary pr-4 pl-4">
                    Order
                </a>
            </div>
            </tbody>
        </form:form>
    </div>
</tags:master>
