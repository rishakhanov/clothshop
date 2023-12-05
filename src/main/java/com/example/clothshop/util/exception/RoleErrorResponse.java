package com.example.clothshop.util.exception;

public class RoleErrorResponse {

    private String message;

    public RoleErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
