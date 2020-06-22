package com.es.phoneshop.web.model;

import java.math.BigDecimal;

public class CartInfo {
    private long cartItemsQuantity;
    private BigDecimal totalPrice;

    public CartInfo() {
    }

    public CartInfo(long cartItemsQuantity, BigDecimal totalPrice) {
        this.cartItemsQuantity = cartItemsQuantity;
        this.totalPrice = totalPrice;
    }

    public long getCartItemsQuantity() {
        return cartItemsQuantity;
    }

    public void setCartItemsQuantity(Long cartItemsQuantity) {
        this.cartItemsQuantity = cartItemsQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
