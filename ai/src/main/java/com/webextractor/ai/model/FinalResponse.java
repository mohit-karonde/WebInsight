package com.webextractor.ai.model;

public class FinalResponse {

    private String message;
    private String statusCode;
    private boolean success;
    private  ApiResponse data;

    public FinalResponse(String message, String statusCode, boolean success, ApiResponse data) {
        this.message = message;
        this.statusCode = statusCode;
        this.success = success;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public ApiResponse getData() {
        return data;
    }
}
