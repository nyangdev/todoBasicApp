package com.example.todoList.security;

import com.example.todoList.repository.TodoRepository;
import org.springframework.stereotype.Component;

@Component("authChecker")
public class AuthChecker {

    private final TodoRepository todoRepository;

    public AuthChecker(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public boolean checkTodoOwner(Long todoId, String username) {
        return todoRepository.findById(todoId)
                .map(todo -> todo.getUser().getUsername().equals(username))
                .orElse(false);
    }

}
