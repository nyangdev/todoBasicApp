package com.example.todoList.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorDetailResponse error = new ErrorDetailResponse(ex.getField(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatus().value(),
                ex.getMessage(),
                List.of(error)
        );

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        List<ErrorDetailResponse> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetailResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                validationErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handlePagingIllegalArgumentException(IllegalArgumentException ex)

    {

        // 예외 메시지 분석하여 필드명 추론
        String rawMessage = ex.getMessage();
        String field = "page/size"; // 기본 필드명

        if (rawMessage.contains("index")) {
            field = "page";
        } else if (rawMessage.contains("size")) {
            field = "size";
        }

        // 오류 응답 생성
        ErrorDetailResponse error = new ErrorDetailResponse(field, rawMessage);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                List.of(error)
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
        String rawMessage = ex.getMessage();

        // 특정 키워드 포함 시 404 처리
        if (rawMessage != null && rawMessage.contains("Can't find")) {
            ErrorDetailResponse error = new ErrorDetailResponse("Not found", rawMessage);
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Todo not found",
                    List.of(error)
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // 다른 RuntimeException → CustomException으로 다시 던짐
        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "internal", rawMessage != null ? rawMessage : "Unexpected runtime error");
    }


}
