package com.example.todoapp.model;

import lombok.*;

@Data // Lombok annotation to generate getters and setters
@AllArgsConstructor //Annotation to generate a constructor with all Arguments
@NoArgsConstructor //Annotation to generate a constructor with no arguments
@ToString //Annotation to generate a toString method
@EqualsAndHashCode //Annotation to generate equals and hashCode methods
public class TodoTask {
    private String title;
    private String description;
}
