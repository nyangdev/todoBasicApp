package com.example.todoList.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

// public response dto
// paging purpose
// paging treatment
@Getter
@AllArgsConstructor
public class PagedResponseDto<T> {

    private final int code;
    private final String message;
    private final LocalDateTime timestamp;

    private final PageData<T> data;

    @Getter
    @AllArgsConstructor
    public static class PageData<T> {
        private final List<T> content;
        private final int page;
        private final int size;
        private final int totalPages;
        private final long totalElements;
        private final boolean last;
    }
}