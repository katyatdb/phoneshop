package com.es.core.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException() {
        super();
    }

    public StockNotFoundException(String message) {
        super(message);
    }
}
