package com.example.todoList.service;

import com.example.todoList.dto.PagedResponseDto;
import com.example.todoList.dto.TodoCreateDto;
import com.example.todoList.dto.TodoUpdateRequestDto;
import com.example.todoList.dto.TodoResponseDto;
import com.example.todoList.entity.Todo;
import com.example.todoList.entity.User;
import com.example.todoList.enums.Status;
import com.example.todoList.exception.CustomException;
import com.example.todoList.repository.TodoRepository;
import com.example.todoList.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    // model mapper import
    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

//    public List<TodoResponseDto> getAllTodos() {
//        return todoRepository.findAll().stream()
//                .map(todo -> modelMapper.map(todo, TodoResponseDto.class))
//                .collect(Collectors.toList());
//    }

    public TodoResponseDto createTodo(TodoCreateDto todoRequestDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "username","This user is not found"));

        Todo todo = new Todo();
        todo.setTitle(todoRequestDto.getTitle());
        todo.setDescription(todoRequestDto.getDescription());
        todo.setDueDate(todoRequestDto.getDueDate());
        todo.setUser(user);

        // basic status
        todo.setStatus(Status.PENDING);

        Todo savedTodo = todoRepository.save(todo);

        return modelMapper.map(savedTodo, TodoResponseDto.class);
    }

    public  TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Can't find todo with id: " + id));


        return modelMapper.map(todo, TodoResponseDto.class);
    }

    // paging treatment
    public PagedResponseDto<TodoResponseDto> getPagedTodos(Pageable pageable) {
        Page<Todo> page = todoRepository.findAll(pageable);

        List<TodoResponseDto> content = page.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoResponseDto.class))
                .toList();

        PagedResponseDto.PageData<TodoResponseDto> pageData = new PagedResponseDto.PageData<>(
                content,                   // 변환된 DTO 목록
                page.getNumber(),         // 현재 페이지 번호 (0부터 시작)
                page.getSize(),           // 페이지 당 항목 수
                page.getTotalPages(),     // 전체 페이지 수
                page.getTotalElements(),  // 전체 항목 수
                page.isLast()             // 현재 페이지가 마지막 페이지인지 여부
        );

        return new PagedResponseDto<>(
                200,                        // HTTP 상태 코드
                "Todos retrieved successfully",  // 메시지
                LocalDateTime.now(),             // 응답 시간
                pageData                         // 실제 데이터와 페이징 정보 포함
        );
    }

    public TodoResponseDto updateTodo(Long id, TodoUpdateRequestDto todoRequestDto) {
        Todo todo = todoRepository.findById(id).orElseThrow(()
        -> new RuntimeException("Can't find todo with id: " + id));

        todo.setTitle(todoRequestDto.getTitle());
        todo.setDescription(todoRequestDto.getDescription());
        todo.setDueDate(todoRequestDto.getDueDate());
        todo.setStatus(todoRequestDto.getStatus());

        todoRepository.save(todo);

        return modelMapper.map(todo, TodoResponseDto.class);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't find todo with id: " + id));

        todoRepository.delete(todo);
    }
}
