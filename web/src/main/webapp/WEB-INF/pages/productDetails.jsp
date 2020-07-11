<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:master pageTitle="Product Details">
    <div class="container mt-4 mb-3">
        <a href="<c:url value="/productList"/>" class="btn btn-outline-primary">
            Back to product list
        </a>
        <div class="row">
            <div class="col-5">
                <h2 class="mt-3">${phone.model}</h2>
                <img class="mt-3" src="<spring:message code="phoneImageUrl"/>${phone.imageUrl}">
                <div class="mt-3">${phone.description}</div>


                <div class="row border mt-3 ml-1 w-75">
                    <div class="col p-2">
                        <label class="h4" for="item-quantity-${phone.id}">
                            Price:
                            <fmt:formatNumber type="currency" value="${phone.price}" currencySymbol="$"/>
                        </label>
                        <div class="row">
                            <div class="col-7 pr-1">
                                <input type="text" id="item-quantity-${phone.id}"
                                       class="form-control text-right" value="1"/>
                                <div id="item-quantity-error-${phone.id}" class="error text-right"></div>
                            </div>
                            <div class="col-5 pl-1 text-center">
                                <button id="add-to-cart-${phone.id}" class="btn btn-outline-primary">
                                    Add to cart
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-4 offset-1">
                <h4>Display</h4>
                <table class="table table-sm table-bordered">
                    <tr>
                        <td>Size</td>
                        <td>
                                ${not empty phone.displaySizeInches ?
                                    phone.displaySizeInches.toString().concat("\"") : "&ndash;"}
                        </td>
                    </tr>
                    <tr>
                        <td>Resolution</td>
                        <td>${not empty phone.displayResolution ? phone.displayResolution : "&ndash;"}</td>
                    </tr>
                    <tr>
                        <td>Technology</td>
                        <td>${not empty phone.displayTechnology ? phone.displayTechnology : "&ndash;"}</td>
                    </tr>
                    <tr>
                        <td>Pixel density</td>
                        <td>${not empty phone.pixelDensity ? phone.pixelDensity : "&ndash;"}</td>
                    </tr>
                </table>

                <h4>Dimensions & weight</h4>
                <table class="table table-sm table-bordered">
                    <tr>
                        <td>Length</td>
                        <td>${not empty phone.lengthMm ? phone.lengthMm.toString().concat("mm") : "&ndash;"}</td>
                    </tr>
                    <tr>
                        <td>Width</td>
                        <td>${not empty phone.widthMm ? phone.widthMm.toString().concat("mm") : "&ndash;"}</td>
                    </tr>
                    <tr>
                        <td>Weight</td>
                        <td>${not empty phone.weightGr ? phone.weightGr : "&ndash;"}</td>
                    </tr>
                </table>

                <h4>Camera</h4>
                <table class="table table-sm table-bordered">
                    <tr>
                        <td>Front</td>
                        <td>
                                ${not empty phone.frontCameraMegapixels ?
                                    phone.frontCameraMegapixels.toString().concat(" megapixels") : "&ndash;"}
                        </td>
                    </tr>
                    <tr>
                        <td>Back</td>
                        <td>
                                ${not empty phone.backCameraMegapixels ?
                                    phone.backCameraMegapixels.toString().concat(" megapixels") : "&ndash;"}
                        </td>
                    </tr>
                </table>

                <h4>Battery</h4>
                <table class="table table-sm table-bordered">
                    <tr>
                        <td>Talk time</td>
                        <td>
                                ${not empty phone.talkTimeHours ?
                                    phone.talkTimeHours.toString().concat(" hours") : "&ndash;"}
                        </td>
                    </tr>
                    <tr>
                        <td>Stand by time</td>
                        <td>
                                ${not empty phone.standByTimeHours ?
                                    phone.standByTimeHours.toString().concat(" hours") : "&ndash;"}
                        </td>
                    </tr>
                    <tr>
                        <td>Battery capacity</td>
                        <td>
                                ${not empty phone.batteryCapacityMah ?
                                    phone.batteryCapacityMah.toString().concat("mAh") : "&ndash;"}
                        </td>
                    </tr>
                </table>

                <h4>Other</h4>
                <table class="table table-sm table-bordered">
                    <tr>
                        <td>Colors</td>
                        <td>
                            <c:if test="${empty phone.colors}">&ndash;</c:if>

                            <c:forEach var="color" items="${phone.colors}" varStatus="index">
                                ${color.code}<c:if test="${not index.last}">,</c:if>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td>Device type</td>
                        <td>${not empty phone.deviceType ? phone.deviceType : "&ndash;"}</td>
                    </tr>
                    <tr>
                        <td>Bluetooth</td>
                        <td>${not empty phone.bluetooth ? phone.bluetooth : "&ndash;"}</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</tags:master>
