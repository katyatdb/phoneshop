package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders/{id}")
public class OrderDetailsPageController {
    @Resource
    private OrderService orderService;

    @GetMapping
    public String getOrder(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getById(id);
            model.addAttribute("order", order);
            return "admin/orderDetails";
        } catch (OrderNotFoundException e) {
            return "pageNotFound";
        }
    }

    @PostMapping
    public String updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders/" + id;
    }
}
