package com.es.core.service.cart;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartRecalculationServiceImpl implements CartRecalculationService {
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Override
    public void recalculate(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getPhone().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice);
    }

    @Override
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }
}
