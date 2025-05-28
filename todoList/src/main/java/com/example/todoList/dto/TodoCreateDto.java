package com.example.todoList.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoCreateDto {
    private String title;
    private String description;
    private LocalDate dueDate;

}
