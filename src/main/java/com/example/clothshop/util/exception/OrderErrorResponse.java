package com.example.clothshop.util.exception;

public class OrderErrorResponse {

    private String message;

    public OrderErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
