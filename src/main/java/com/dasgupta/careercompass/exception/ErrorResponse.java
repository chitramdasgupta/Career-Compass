package com.dasgupta.careercompass.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Constructor for validation errors
    public ErrorResponse(String code, String message, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
}
