package com.ertb.model.error;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private String message;
    private ErrorType type;
    private LocalDateTime timeStamp;

    public ErrorResponse(String message, ErrorType type) {
        this.message = message;
        this.type = type;
        this.timeStamp = LocalDateTime.now();
    }
}
