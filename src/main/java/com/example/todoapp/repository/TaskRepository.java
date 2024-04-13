package com.example.todoapp.repository;

import com.example.todoapp.model.Priority;
import com.example.todoapp.model.Status;
import com.example.todoapp.model.TodoTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<TodoTask, Integer> {

    List<TodoTask> findByPriority(Priority priority);
    List<TodoTask> findByDeadline(LocalDate deadline);
    List<TodoTask> findByStatus(Status status);
}
