package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.service.phone.PhoneService;
import com.es.core.util.PaginationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Value("${phonesPerPage}")
    private int phonesPerPage;
    @Resource
    private PhoneService phoneService;

    void setPhonesPerPage(int phonesPerPage) {
        this.phonesPerPage = phonesPerPage;
    }

    @GetMapping
    public String showProductList(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "query", required = false) String query,
                                  @RequestParam(name = "sortBy", defaultValue = "brand") String sortBy,
                                  @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy) {
        List<Phone> phones = phoneService.getPhoneList(query, page, phonesPerPage, sortBy, orderBy);
        int phonesNumber = phoneService.getPhonesNumber(query);

        PaginationResult<Phone> paginationResult = new PaginationResult<>(phonesNumber, phonesPerPage, phones);
        model.addAttribute("paginationResult", paginationResult);
        return "productList";
    }
}
