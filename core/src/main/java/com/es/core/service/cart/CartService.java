package com.es.core.service.cart;

import java.util.Map;

import com.es.core.model.cart.Cart;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity);

    void update(Map<Long, Long> items);

    void remove(Long phoneId);
}
