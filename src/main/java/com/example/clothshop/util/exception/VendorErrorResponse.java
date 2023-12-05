package com.example.clothshop.util.exception;

public class VendorErrorResponse {

    private String message;

    public VendorErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
