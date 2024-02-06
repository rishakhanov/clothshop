package com.example.clothshop.util.exception;

public class DiscountErrorResponse {

    private String message;

    public DiscountErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
