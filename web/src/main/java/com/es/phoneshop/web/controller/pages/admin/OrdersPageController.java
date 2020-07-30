package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    @Resource
    private OrderService orderService;

    @GetMapping
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.getOrders());
        return "admin/orders";
    }
}
