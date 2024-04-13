package com.example.todoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Data // Lombok annotation to generate getters and setters
@AllArgsConstructor //Annotation to generate a constructor with all Arguments
@NoArgsConstructor //Annotation to generate a constructor with no arguments
@ToString //Annotation to generate a toString method
@EqualsAndHashCode //Annotation to generate equals and hashCode methods
public class TodoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate deadline;
    private Status status;
    public TodoTask(String title, String description, Priority priority, LocalDate deadline, Status status){
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }


}
