package org.example.controller;

import jakarta.validation.Valid;
import org.example.entity.User;
import org.example.exception.UserExists;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")  // allow your frontend URL
@RestController
@RequestMapping("/csds")


public class TimesheetController {


    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;



    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if(userRepository.existsByEmail(user.getEmail()) || userRepository.existsBySsn(user.getSsn())
                || userRepository.existsByMobile(user.getMobile())){
            throw new UserExists("User already exists, Please Sign-In");
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
