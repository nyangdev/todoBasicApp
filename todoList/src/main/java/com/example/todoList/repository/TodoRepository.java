package com.example.todoList.repository;

import com.example.todoList.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // paging treatment
    Page<Todo> findAll(Pageable pageable);
}
