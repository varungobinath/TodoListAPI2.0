package org.example.todoapplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String task;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
