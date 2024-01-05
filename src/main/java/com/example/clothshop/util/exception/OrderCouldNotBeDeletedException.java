package com.example.clothshop.util.exception;

public class OrderCouldNotBeDeletedException extends RuntimeException {
    public OrderCouldNotBeDeletedException(String msg) {
        super(msg);
    }
}
