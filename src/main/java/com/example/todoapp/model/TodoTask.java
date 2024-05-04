package com.example.todoapp.model;

import com.example.todoapp.model.enums.Priority;
import com.example.todoapp.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data // Lombok annotation to generate getters and setters
@AllArgsConstructor //Annotation to generate a constructor with all Arguments
@NoArgsConstructor //Annotation to generate a constructor with no arguments
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public TodoTask(String title, String description, Priority priority, LocalDate deadline, Status status) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }

    @Override
    public String toString() {
        if (user == null) {
            return "task_id: " + id + ", title: " + title + ", description: " + description;
        }
        return "task_id: " + id + ", title: " + title + ", description: " + description + "user_id: " + user.getId()
                + ", user_name: " + user.getName() + ", user_email: " + user.getEmail();
    }

}
