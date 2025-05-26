package com.example.todoList.service;

import com.example.todoList.dto.TodoRequestDto;
import com.example.todoList.dto.TodoResponseDto;
import com.example.todoList.entity.Todo;
import com.example.todoList.enums.Status;
import com.example.todoList.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<TodoResponseDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TodoResponseDto convertToDto(Todo todo) {
        TodoResponseDto todoResponseDto = new TodoResponseDto();
        todoResponseDto.setId(todo.getId());
        todoResponseDto.setTitle(todo.getTitle());
        todoResponseDto.setDescription(todo.getDescription());
        todoResponseDto.setStatus(todo.getStatus());
        todoResponseDto.setDueDate(todo.getDueDate());
        todoResponseDto.setCreatedAt(todo.getCreatedAt());
        todoResponseDto.setUpdatedAt(todo.getUpdatedAt());
        return todoResponseDto;
    }

    public TodoResponseDto createTodo(TodoRequestDto todoRequestDto) {
        Todo todo = new Todo();
        todo.setTitle(todoRequestDto.getTitle());
        todo.setDescription(todoRequestDto.getDescription());
        todo.setDueDate(todoRequestDto.getDueDate());

        // basic status
        todo.setStatus(Status.PENDING);

        Todo savedTodo = todoRepository.save(todo);

        return convertToDto(savedTodo);
    }

    public  TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Can't find todo with id: " + id));

        return convertToDto(todo);
    }

    public TodoResponseDto updateTodo(Long id, TodoRequestDto todoRequestDto) {
        Todo todo = todoRepository.findById(id).orElseThrow(()
        -> new RuntimeException("Can't find todo with id: " + id));

        todo.setTitle(todoRequestDto.getTitle());
        todo.setDescription(todoRequestDto.getDescription());
        todo.setDueDate(todoRequestDto.getDueDate());
        todo.setStatus(todoRequestDto.getStatus());

        todoRepository.save(todo);

        return convertToDto(todo);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't find todo with id: " + id));

        todoRepository.delete(todo);
    }
}
