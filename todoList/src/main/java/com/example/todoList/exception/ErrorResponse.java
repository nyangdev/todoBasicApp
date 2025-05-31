package com.example.todoList.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {

    private int code;
    private String message;
    private LocalDateTime timestamp;
    private List<ErrorDetailResponse> errors;

    public ErrorResponse(int code, String message, List<ErrorDetailResponse> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}