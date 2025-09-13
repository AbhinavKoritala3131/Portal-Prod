package org.example.controller;

import org.example.entity.AuthorizeUsers;
import org.example.dto.LoginDTO;
import org.example.entity.User;
import org.example.exception.UserExists;
import org.example.repository.AuthorizeUsersRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")  // allow your frontend URL
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorizeUsersRepository authorizeUsersRepository;

//    @GetMapping("/getAll")
//    public List<User> findAll() {
//        return userService.findAll();
//    }
//    GetById , DeleteById




    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> createUser(@Valid  @RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsBySsn(user.getSsn())
                || userRepository.existsByMobile(user.getMobile())) {
            throw new UserExists("User already exists, Please Sign-In");
        }
        Optional<AuthorizeUsers> v = authorizeUsersRepository.findByEmail(user.getEmail());
        if (v.isPresent()) {
            User reg = userService.registerUser(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "You are all set " + reg.getFname() + " Please go ahead and Sign-In");
            return ResponseEntity.status(200).body(response);
        }
        else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You dont have access. Please contact your administrator");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> signIn(@Valid @RequestBody LoginDTO loginDTO){

        return ResponseEntity.status(200).body(userService.login(loginDTO));

    }
    @GetMapping("/fetch/{id}")
    public ResponseEntity<Map<String,Object>> userDetails(@PathVariable Long id){
        return ResponseEntity.status(200).body(userService.getUser(id));
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
