package com.es.core.service.cart;

import com.es.core.model.cart.Cart;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartRecalculationServiceImpl implements CartRecalculationService {
    @Override
    public void recalculate(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getPhone().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
    }
}
