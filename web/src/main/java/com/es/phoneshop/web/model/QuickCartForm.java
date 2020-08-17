package com.es.phoneshop.web.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class QuickCartForm {
    @NotNull
    @Valid
    private List<QuickCartItemForm> cartItems;

    public QuickCartForm(List<QuickCartItemForm> cartItems) {
        this.cartItems = cartItems;
    }

    public List<QuickCartItemForm> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<QuickCartItemForm> cartItems) {
        this.cartItems = cartItems;
    }
}
