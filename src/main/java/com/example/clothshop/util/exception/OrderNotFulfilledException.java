package com.example.clothshop.util.exception;

public class OrderNotFulfilledException extends RuntimeException {
    public OrderNotFulfilledException(String msg) {
        super(msg);
    }
}
