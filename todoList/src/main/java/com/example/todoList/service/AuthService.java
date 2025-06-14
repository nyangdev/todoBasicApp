package com.example.todoList.service;

import com.example.todoList.config.JwtUtil;
import com.example.todoList.dto.LoginRequestDto;
import com.example.todoList.dto.TokenResponseDto;
import com.example.todoList.dto.SignupResponseDto;
import com.example.todoList.entity.User;
import com.example.todoList.enums.Role;
import com.example.todoList.exception.CustomException;
import com.example.todoList.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public TokenResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "username", "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(HttpStatus.NOT_FOUND, "username","Invalid password");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUsername());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public SignupResponseDto signup(LoginRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "username", "Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        return modelMapper.map(user, SignupResponseDto.class);
    }
}
