package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.model.CartItemForm;
import com.es.phoneshop.web.model.CartItemListForm;
import com.es.phoneshop.web.validator.CartItemListFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private CartItemListFormValidator cartItemsValidator;

    @InitBinder("cartItemListForm")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(cartItemsValidator);
    }

    @ModelAttribute("cartItemListForm")
    public CartItemListForm addCartItems() {
        List<CartItemForm> cartItems = cartService.getCart().getCartItems().stream()
                .map(item -> new CartItemForm(item.getPhone().getId(), item.getQuantity()))
                .collect(Collectors.toList());

        return new CartItemListForm(cartItems);
    }

    @ModelAttribute("cart")
    public Cart addCart() {
        return cartService.getCart();
    }

    @GetMapping
    public String getCart() {
        return "cart";
    }

    @PutMapping
    public String updateCart(@ModelAttribute("cartItemListForm") @Valid CartItemListForm cartItems,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cart";
        }

        Map<Long, Long> newCartItems = cartItems.getCartItems().stream()
                .collect(Collectors.toMap(CartItemForm::getId, CartItemForm::getQuantity));
        cartService.update(newCartItems);

        return "redirect:/cart";
    }

    @DeleteMapping
    public String deletePhone(Long id) {
        cartService.remove(id);

        return "redirect:/cart";
    }
}
