package com.example.clothshop.util.exception;

public class ImageErrorResponse {

    private String message;

    public ImageErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
