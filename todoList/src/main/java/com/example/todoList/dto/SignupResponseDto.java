package com.example.todoList.dto;

import com.example.todoList.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {
    private String username;
    private Role role;
}
