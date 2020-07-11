package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.service.cart.CartService;
import com.es.core.service.order.OrderService;
import com.es.phoneshop.web.converter.UserDataFormToOrderConverter;
import com.es.phoneshop.web.model.UserDataForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;

    @ModelAttribute
    public Order addOrder() {
        Cart cart = cartService.getCart();
        return orderService.createOrder(cart);
    }

    @ModelAttribute
    public UserDataForm addUserDataForm() {
        return new UserDataForm();
    }

    @GetMapping
    public String getOrder() {
        return "order";
    }

    @PostMapping
    public String placeOrder(@ModelAttribute("userDataForm") @Valid UserDataForm userDataForm,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order";
        }

        Order order = orderService.createOrder(cartService.getCart());
        UserDataFormToOrderConverter converter = new UserDataFormToOrderConverter(order);
        order = converter.convert(userDataForm);

        if (order.getOrderItems().isEmpty()) {
            return "order";
        }

        try {
            orderService.placeOrder(order);
        } catch (OutOfStockException e) {
            model.addAttribute("outOfStockMessage", e.getMessage());
            return "order";
        }

        return "redirect:/orderOverview/" + order.getSecureId();
    }
}
