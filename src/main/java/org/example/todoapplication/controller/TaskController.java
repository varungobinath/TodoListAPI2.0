package org.example.todoapplication.controller;

import org.example.todoapplication.dao.TaskRepo;
import org.example.todoapplication.dao.UserRepo;
import org.example.todoapplication.model.Task;
import org.example.todoapplication.model.TaskDTO;
import org.example.todoapplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todoapp")
public class TaskController {
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody Task task){
        try {
            if(task.getTask()==null || task.getUser().getId()==null) return new ResponseEntity<>("Input Data Error",HttpStatus.BAD_REQUEST);
            User user = userRepo.findById(task.getUser().getId()).orElse(null);
            if (user == null) return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);

            task.setUser(user);
            task.setCompleted(false);
            taskRepo.save(task);
            return new ResponseEntity<>("Task Is Added Successful",HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Network Issue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(required = false) Long user_id,@RequestParam(required = false) Long task_id){
        try {
            if(task_id==null && user_id==null) return new ResponseEntity<>("Input Data Error",HttpStatus.BAD_REQUEST);
            if (task_id != null) {
                if(!userRepo.existsById(user_id))
                    return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
                taskRepo.deleteById(task_id);
                return new ResponseEntity<>("Task Deleted Successful",HttpStatus.OK);
            }else {
                taskRepo.deleteByUserId(user_id);
                return new ResponseEntity<>("All Tasks Deleted Successfull",HttpStatus.OK);
            }
        } catch (Exception e){
            return new ResponseEntity<>("Network Issue",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/toggle")
    public ResponseEntity<Void> toggle(@RequestParam Long id){
        try {
            if(id==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            taskRepo.findById(id).ifPresent(task -> {
                task.setCompleted(!task.isCompleted());
                taskRepo.save(task);
            });
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllByUserId(@RequestParam Long id,@RequestParam(required = false) Boolean toggle){
        try {
            if(id==null) return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
            User user = userRepo.findById(id).orElse(null);
            if (user != null) {
                List<TaskDTO> task = new ArrayList<>();
                for (Task t : (toggle == null) ? taskRepo.findByUser(user) : taskRepo.findByUserAndCompletedIs(user, toggle)) {
                    TaskDTO tsk = new TaskDTO(t.getId(), t.getTask(), t.isCompleted());
                    task.add(tsk);
                }
                return new ResponseEntity<>(task,HttpStatus.OK);
            }
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
