package com.example.todoList.controller;

import com.example.todoList.dto.PagedResponseDto;
import com.example.todoList.dto.TodoCreateDto;
import com.example.todoList.dto.TodoUpdateRequestDto;
import com.example.todoList.dto.TodoResponseDto;
import com.example.todoList.entity.Todo;
import com.example.todoList.exception.ErrorResponse;
import com.example.todoList.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Operation(summary = "Todo 목록 조회", description = "목록을 페이징 처리해서 조회 가능하도록 했습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 접근입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PreAuthorize("hasRole('ROLE_USER') and @authChecker.checkTodoOwner(#id, principal.username)")
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

    @Operation(summary = "특정 id를 가진 Todo 조회", description = "넘어오는 id에 해당하는 todo를 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지않는 아이디입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and @authChecker.checkTodoOwner(#id, principal.username)")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        TodoResponseDto todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    @Operation(summary = "todo 등록하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "올바르지 않은 형식의 등록 시도",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody @Valid TodoCreateDto todoCreateRequestDto) {
        TodoResponseDto todo = todoService.createTodo(todoCreateRequestDto);
        return new ResponseEntity<>(todo, HttpStatus.CREATED); // 201 CREATED
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지않는 아이디",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "올바르지 않은 형식의 등록 시도",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @Operation(summary = "Todo 수정하기", description = "넘어오는 id에 해당하는 todo를 수정할 수 있습니다.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and @authChecker.checkTodoOwner(#id, principal.username)")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long id, @RequestBody @Valid TodoUpdateRequestDto todoUpdateRequestDto) {

        TodoResponseDto todo = todoService.updateTodo(id, todoUpdateRequestDto);

        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지않는 아이디",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @Operation(summary = "Todo 삭제하기")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and @authChecker.checkTodoOwner(#id, principal.username)")
    public ResponseEntity<Todo> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
