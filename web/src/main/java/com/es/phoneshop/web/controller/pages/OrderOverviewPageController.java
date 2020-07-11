package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private OrderService orderService;

    @GetMapping("/{secureId}")
    public String showOrderOverview(@PathVariable String secureId, Model model) {
        try {
            Order order = orderService.getBySecureId(secureId);
            model.addAttribute("order", order);
            return "orderOverview";
        } catch (OrderNotFoundException e) {
            return "pageNotFound";
        }
    }
}
