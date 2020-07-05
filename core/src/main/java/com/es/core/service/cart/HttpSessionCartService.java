package com.es.core.service.cart;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;
    @Resource
    private CartRecalculationService cartRecalculationService;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Phone phone = phoneDao.get(phoneId).orElseThrow(ProductNotFoundException::new);
        Optional<CartItem> cartItemOptional = findCartItem(phoneId);

        if (cartItemOptional.isPresent()) {
            addCartItem(cartItemOptional.get(), quantity);
        } else {
            CartItem cartItem = new CartItem(phone, 0L);
            addCartItem(cartItem, quantity);
        }

        cartRecalculationService.recalculate(cart);
    }

    @Override
    public void update(Map<Long, Long> items) {
        List<Long> outOfStockPhoneIds = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : items.entrySet()) {
            Long phoneId = entry.getKey();
            Long quantity = entry.getValue();

            phoneDao.get(phoneId).orElseThrow(ProductNotFoundException::new);
            Optional<CartItem> cartItemOptional = findCartItem(phoneId);

            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();

                try {
                    updateCartItem(cartItem, quantity);
                } catch (OutOfStockException e) {
                    outOfStockPhoneIds.add(cartItem.getPhone().getId());
                }
            }
        }

        cartRecalculationService.recalculate(cart);

        if (!outOfStockPhoneIds.isEmpty()) {
            throw new OutOfStockException(outOfStockPhoneIds);
        }
    }

    private Optional<CartItem> findCartItem(Long phoneId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findFirst();
    }

    private void addCartItem(CartItem cartItem, Long quantity) {
        Optional<Stock> stockOptional = stockDao.get(cartItem.getPhone().getId());
        Integer quantityInStock = stockOptional.map(Stock::getStock).orElse(0);
        Long quantityInCart = cartItem.getQuantity();

        if (quantityInStock < quantity + quantityInCart) {
            throw new OutOfStockException();
        }

        if (quantityInCart == 0 && quantity > 0) {
            cart.getCartItems().add(cartItem);
        }
        cartItem.setQuantity(quantityInCart + quantity);
    }

    private void updateCartItem(CartItem cartItem, Long quantity) {
        if (quantity == 0) {
            cart.getCartItems().remove(cartItem);
        }

        Optional<Stock> stockOptional = stockDao.get(cartItem.getPhone().getId());
        Integer quantityInStock = stockOptional.map(Stock::getStock).orElse(0);

        if (quantityInStock < quantity) {
            throw new OutOfStockException();
        }

        cartItem.setQuantity(quantity);
    }

    @Override
    public void remove(Long phoneId) {
        cart.getCartItems().removeIf(cartItem -> cartItem.getPhone().getId().equals(phoneId));
        cartRecalculationService.recalculate(cart);
    }
}
