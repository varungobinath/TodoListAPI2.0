package org.example.todoapplication.controller;

import org.example.todoapplication.dao.TaskRepo;
import org.example.todoapplication.dao.UserRepo;
import org.example.todoapplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam String username,@RequestParam String password){
        try {
            if(username==null || password==null) return new ResponseEntity<>("Input Data Error",HttpStatus.BAD_REQUEST);
            User user = userRepo.findByUsername(username);
            if(user!=null){
                return new ResponseEntity<>("User Already Exists",HttpStatus.CONFLICT);
            }
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userRepo.save(user);
            return new ResponseEntity<>("User Added Successfuly",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Network Issue",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Long> login(@RequestParam String username,@RequestParam String password){
        try {
            if(username==null || password==null) return new ResponseEntity<>(-1L,HttpStatus.BAD_REQUEST);
            User user = userRepo.findByUsername(username);
            if(user==null) return new ResponseEntity<>(-1L,HttpStatus.NOT_FOUND);
            if(user!=null && user.getPassword().equals(password)){
                return new ResponseEntity<>(user.getId(),HttpStatus.OK);
            }
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long id){
        try {
            if(id==null) return new ResponseEntity<>("Input Data Error",HttpStatus.BAD_REQUEST);
            if (userRepo.existsById(id)) {
                taskRepo.deleteByUserId(id);
                userRepo.deleteById(id);
                return new ResponseEntity<>("User Deleted Successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>("Network Issue",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
