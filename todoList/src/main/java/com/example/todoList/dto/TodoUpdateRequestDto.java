package com.example.todoList.dto;

import com.example.todoList.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoUpdateRequestDto {

    private String title;
    private String description;
    private LocalDate dueDate;

    private Status status;
}