package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.service.phone.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    @Resource
    private PhoneService phoneService;

    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        Optional<Phone> phoneOptional = phoneService.getPhone(id);

        if (!phoneOptional.isPresent()) {
            return "pageNotFound";
        }

        model.addAttribute("phone", phoneOptional.get());
        return "productDetails";
    }
}
