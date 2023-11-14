package com.example.clothshop.util;

public class OrderNotFulfilledException extends RuntimeException {
    public OrderNotFulfilledException(String msg) {
        super(msg);
    }
}
