package com.example.todoList.dto;

import com.example.todoList.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoUpdateRequestDto {

    @NotBlank(message = "Title must not be blank.")
    @Size(max = 100, message = "Title must be at most 100 characters long.")
    private String title;

    @Size(max = 1000, message = "Description must be at most 1000 characters long.")
    private String description;

    @FutureOrPresent(message = "Due date must be today or in the future.")
    private LocalDate dueDate; // nullable

    private Status status;
}