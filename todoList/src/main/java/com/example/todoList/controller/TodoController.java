package com.example.todoList.controller;

import com.example.todoList.dto.PagedResponseDto;
import com.example.todoList.dto.TodoCreateDto;
import com.example.todoList.dto.TodoUpdateRequestDto;
import com.example.todoList.dto.TodoResponseDto;
import com.example.todoList.entity.Todo;
import com.example.todoList.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

//    @GetMapping
//    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {
//        List<TodoResponseDto> todos = todoService.getAllTodos();
//
//        return ResponseEntity.ok(todos); // 200 OK
//    }

    // paging treatment
    @GetMapping
    public ResponseEntity<PagedResponseDto<TodoResponseDto>> getPagedTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<TodoResponseDto> response = todoService.getPagedTodos(pageable);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        TodoResponseDto todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody @Valid TodoCreateDto todoCreateRequestDto) {
        TodoResponseDto todo = todoService.createTodo(todoCreateRequestDto);
        return new ResponseEntity<>(todo, HttpStatus.CREATED); // 201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long id, @RequestBody @Valid TodoUpdateRequestDto todoUpdateRequestDto) {

        TodoResponseDto todo = todoService.updateTodo(id, todoUpdateRequestDto);

        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
