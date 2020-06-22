package com.es.phoneshop.web.controller;

import com.es.phoneshop.web.exception.InvalidFormatException;
import com.es.core.model.cart.Cart;
import com.es.core.service.cart.CartService;
import com.es.phoneshop.web.model.CartInfo;
import com.es.phoneshop.web.model.RequestCartItem;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @GetMapping
    @ResponseBody
    public CartInfo getCart() {
        Cart cart = cartService.getCart();
        return new CartInfo(cart.getCartItems().size(), cart.getTotalPrice());
    }

    @PostMapping
    @ResponseBody
    public CartInfo addPhone(@RequestBody @Valid RequestCartItem cartItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidFormatException();
        }

        cartService.addPhone(cartItem.getId(), cartItem.getQuantity());
        Cart cart = cartService.getCart();

        return new CartInfo(cart.getCartItems().size(), cart.getTotalPrice());
    }
}
