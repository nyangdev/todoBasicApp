package com.example.todoList.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String field;

    public CustomException(HttpStatus status, String field, String message) {
        super(message);
        this.status = status;
        this.field = field;
    }
}
