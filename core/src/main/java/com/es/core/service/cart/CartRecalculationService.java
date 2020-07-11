package com.es.core.service.cart;

import com.es.core.model.cart.Cart;

import java.math.BigDecimal;

public interface CartRecalculationService {
    void recalculate(Cart cart);

    BigDecimal getDeliveryPrice();
}
