package com.example.todoList.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetailResponse {

    private String field;
    private String message;

}