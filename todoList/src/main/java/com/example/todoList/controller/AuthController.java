package com.example.todoList.controller;

import com.example.todoList.dto.LoginRequestDto;
import com.example.todoList.dto.SignupResponseDto;
import com.example.todoList.dto.TokenResponseDto;
import com.example.todoList.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        TokenResponseDto tokenResponse = authService.login(request);
        return ResponseEntity.ok(tokenResponse); // 200 OK
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody LoginRequestDto request) {
        SignupResponseDto signupResponse = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponse); // 201 CREATED
    }
}
