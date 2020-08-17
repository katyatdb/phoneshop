package com.es.phoneshop.web.controller.pages;

import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.model.QuickCartForm;
import com.es.phoneshop.web.model.QuickCartItemForm;
import com.es.phoneshop.web.validator.QuickCartFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/quickOrder")
public class QuickOrderController {
    @Resource
    private CartService cartService;
    @Resource
    private QuickCartFormValidator quickCartFormValidator;

    @InitBinder("quickCartForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(quickCartFormValidator);
    }

    @ModelAttribute
    public QuickCartForm addCartItems() {
        ArrayList<QuickCartItemForm> cartItems = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            cartItems.add(new QuickCartItemForm());
        }

        return new QuickCartForm(cartItems);
    }

    @GetMapping
    public String showQuickOrder() {
        return "quickOrder";
    }

    @PostMapping
    public String addToCart(@ModelAttribute("quickCartForm") @Valid QuickCartForm cartForm,
                            BindingResult bindingResult, Model model) {
        for (int i = 0; i < cartForm.getCartItems().size(); i++) {
            if (!bindingResult.hasFieldErrors("cartItems[" + i + "].code") &&
                    !bindingResult.hasFieldErrors("cartItems[" + i + "].quantity")) {
                QuickCartItemForm cartItem = cartForm.getCartItems().get(i);

                if (cartItem.getCode() != null && cartItem.getQuantity() != null) {
                    cartService.addPhone(cartItem.getCode(), cartItem.getQuantity());
                }
            }
        }

        return "quickOrder";
    }
}
