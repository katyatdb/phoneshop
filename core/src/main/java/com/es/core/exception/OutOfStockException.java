package com.es.core.exception;

import java.util.List;

public class OutOfStockException extends RuntimeException {
    private List<Long> productIds;

    public OutOfStockException() {
        super();
    }

    public OutOfStockException(List<Long> productIds) {
        this.productIds = productIds;
    }

    public OutOfStockException(String message) {
        super(message);
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
