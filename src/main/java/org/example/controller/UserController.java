package org.example.controller;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")  // allow your frontend URL
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/getAll")
//    public List<User> findAll() {
//        return userService.findAll();
//    }
//    GetById , DeleteById

    @PostMapping("/create")
    public ResponseEntity<Map<String,String>> createUser(@Valid  @RequestBody User user) {
        if (user.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Do not send ID while creating User");
        }
        User registeredUser = userService.registerUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("firstName", registeredUser.getFname());

        return ResponseEntity.status(201).body(response);

    }

//    }
//    @PutMapping("/update/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable Long id,  @RequestBody User updatedUser) {
////        if (updatedUser.getId() == null) {
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is Required to update this user");
////        }
//        if (updatedUser.getName() == null && updatedUser.getEmail() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data provided to update.");
//        }
//        if ( updatedUser.getId()!=null && id!=updatedUser.getId()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is not matching");
//        }
//        return userService.update(id,updatedUser);
//
//    }
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> RemoveUser(@PathVariable Long id) {
//        if ( id <1) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enter a valid ID");
//
//        } else {
//
//            return userService.del(id);
//        }
//
//
//    }
//    @GetMapping("getUser/{id}")
//    public ResponseEntity<User> getUser(@PathVariable Long id){
//        if (id>0){
//            return ResponseEntity.ok(userService.getUser(id));
//        }
//        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check your ID and Try again ");
//    }
}
