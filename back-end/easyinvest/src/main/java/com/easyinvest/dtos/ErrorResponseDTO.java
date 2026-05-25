package com.easyinvest.dtos;

import java.util.List;

public class ErrorResponseDTO {

    private String message;
    private int status;
    private String path;
    private String date;
    private List<String> errors;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String date, int status, String message, String path) {
        this.date = date;
        this.status = status;
        this.message = message;
        this.path = path;
    }
    public ErrorResponseDTO(String date, int status, String message, String path, List<String> errors) {
        this.date = date;
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }
}
