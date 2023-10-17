package com.example.clothshop.util;

public class ProductNotCreatedException extends RuntimeException{
    public ProductNotCreatedException(String msg) {
        super(msg);
    }
}
