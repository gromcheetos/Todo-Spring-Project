package com.example.todoapp.repository;

import com.example.todoapp.model.TodoTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<TodoTask, Integer> {

}
