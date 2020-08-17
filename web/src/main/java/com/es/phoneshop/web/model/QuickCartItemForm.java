package com.es.phoneshop.web.model;

import javax.validation.constraints.Min;

public class QuickCartItemForm {
    private Long code;

    @Min(1)
    private Long quantity;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
