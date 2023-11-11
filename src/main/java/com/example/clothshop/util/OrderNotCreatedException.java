package com.example.clothshop.util;

public class OrderNotCreatedException extends RuntimeException {
    public OrderNotCreatedException(String msg) {
        super(msg);
    }
}
