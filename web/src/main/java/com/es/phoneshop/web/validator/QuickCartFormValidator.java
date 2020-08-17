package com.es.phoneshop.web.validator;

import com.es.core.dao.stock.StockDao;
import com.es.core.model.stock.Stock;
import com.es.phoneshop.web.model.QuickCartForm;
import com.es.phoneshop.web.model.QuickCartItemForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class QuickCartFormValidator implements Validator {
    @Resource
    private StockDao stockDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return QuickCartForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        QuickCartForm cartForm = (QuickCartForm) o;
        List<QuickCartItemForm> cartItems = new ArrayList<>(cartForm.getCartItems());

        for (int i = 0; i < cartItems.size(); i++) {
            QuickCartItemForm cartItemForm = cartItems.get(i);
            if (cartItemForm.getCode() == null || cartItemForm.getQuantity() == null) {
                continue;
            }

            Optional<Stock> stock = stockDao.get(cartItemForm.getCode());

            if (!stock.isPresent()) {
                errors.rejectValue("cartItems[" + i + "].code", "validation.productNotFound");
            } else if (cartItemForm.getQuantity() > stock.get().getStock()) {
                errors.rejectValue("cartItems[" + i + "].quantity", "validation.outOfStock");
            }
        }
    }
}
