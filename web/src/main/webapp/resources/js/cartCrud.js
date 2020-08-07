$("button[id^='add-to-cart-']").click(function () {
    let id = $(this).attr("id").slice(12);
    let quantity = $(`#item-quantity-${id}`).val();
    let data = {id, quantity};

    $.ajax({
        url: ajaxCartUrl,
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            updateCartInfo(data);
            $("div[id^='item-quantity-error-']").text("");
        },
        error: function (data) {
            $(`#item-quantity-error-${id}`).text(data.responseText);
        }
    });
});

$("button[id^='delete-from-cart-']").click(function () {
    let id = $(this).attr("id").slice(17);
    $("#delete-cart-item-id").val(id);
    $("#delete-cart-item-form").submit();
});

$("button[id='update-cart-btn']").click(function () {
    $("#update-cart-form").submit();
});

function updateCartInfo(data) {
    $("#cart-items-quantity").text(`${data.cartItemsQuantity} items`);
    $("#total-price").text(`$${data.totalPrice}`);
}

$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

$(function () {
    $.get(ajaxCartUrl, updateCartInfo);
});