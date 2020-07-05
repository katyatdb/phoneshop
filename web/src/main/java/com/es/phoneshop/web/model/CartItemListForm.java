package com.es.phoneshop.web.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CartItemListForm {
    @NotNull
    @Valid
    private List<CartItemForm> cartItems;

    public CartItemListForm(List<CartItemForm> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItemForm> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemForm> cartItems) {
        this.cartItems = cartItems;
    }
}
