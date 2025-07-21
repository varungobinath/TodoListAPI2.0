package org.example.todoapplication.model;

import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String task;
    private boolean completed;
    public TaskDTO(Long id,String task,boolean completed){
        this.id = id;
        this.task = task;
        this.completed = completed;
    }
}
