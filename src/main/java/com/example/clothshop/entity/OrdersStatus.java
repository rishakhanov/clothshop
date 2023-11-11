package com.example.clothshop.entity;

public enum OrdersStatus {
    NEW("N"),
    PAID("P"),
    CANCELED("CD"),
    COMPLETE("CE");

    private String code;

    OrdersStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
