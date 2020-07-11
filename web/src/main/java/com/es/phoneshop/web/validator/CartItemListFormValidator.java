package com.es.phoneshop.web.validator;

import com.es.core.dao.stock.StockDao;
import com.es.core.model.stock.Stock;
import com.es.phoneshop.web.model.CartItemForm;
import com.es.phoneshop.web.model.CartItemListForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CartItemListFormValidator implements Validator {
    @Resource
    private StockDao stockDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return CartItemListForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CartItemListForm cartItemListForm = (CartItemListForm) o;
        List<CartItemForm> cartItems = new ArrayList<>(cartItemListForm.getCartItems());

        for (int i = 0; i < cartItems.size(); i++) {
            CartItemForm cartItemForm = cartItems.get(i);
            Optional<Stock> stock = stockDao.get(cartItemForm.getId());

            if (cartItemForm.getQuantity() > stock.map(Stock::getStock).orElse(0)) {
                errors.rejectValue("cartItems[" + i + "].quantity", "validation.outOfStock");
            }
        }
    }
}
