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
import java.util.List;
import java.util.stream.IntStream;

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
        IntStream.range(0, 10)
                .forEach(i -> cartItems.add(new QuickCartItemForm()));

        return new QuickCartForm(cartItems);
    }

    @GetMapping
    public String showQuickOrder() {
        return "quickOrder";
    }

    @PostMapping
    public String addToCart(@ModelAttribute("quickCartForm") @Valid QuickCartForm cartForm,
                            BindingResult bindingResult, Model model) {
        List<String> successMessages = new ArrayList<>();

        for (int i = 0; i < cartForm.getCartItems().size(); i++) {
            if (!hasFieldErrors(bindingResult, i)) {
                QuickCartItemForm cartItem = cartForm.getCartItems().get(i);

                if (cartItem.getCode() != null && cartItem.getQuantity() != null) {
                    cartService.addPhone(cartItem.getCode(), cartItem.getQuantity());
                    successMessages.add("Product " + cartItem.getCode() + " added successfully");

                    cartItem.setCode(null);
                    cartItem.setQuantity(null);
                }
            }
        }

        model.addAttribute("success", successMessages);
        return "quickOrder";
    }

    private boolean hasFieldErrors(BindingResult bindingResult, int index) {
        return bindingResult.hasFieldErrors("cartItems[" + index + "].code") ||
                bindingResult.hasFieldErrors("cartItems[" + index + "].quantity");
    }
}
