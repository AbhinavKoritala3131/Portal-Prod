package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.AuthorizedUser;
import org.example.dto.LoginDTO;
import org.example.entity.User;
import org.example.exception.UserNotFound;
import org.example.jwtConfig.JWTService;
import org.example.repository.AuthUsersRepo;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthUsersRepo authUsersRepo;

    @Autowired
    private JWTService jwtService;

//    @GetMapping("/getAll")
//    public List<User> findAll() {
//        return userService.findAll();
//    }
//    GetById , DeleteById




    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid  @RequestBody User user) {
        Optional<AuthorizedUser> authUser = authUsersRepo.findByUsername(user.getUsername());
        if (authUser.isPresent()) {
            if(userRepository.existsByMobile(user.getMobile()) || userRepository.existsBySsn(
                        user.getSsn()
            )){
                return ResponseEntity.status(409).body("Details already exists");
            }

            return userService.register(user);

        }

        else{Optional<User> exUser = userRepository.findByUsername(user.getUsername());
            if(exUser.isPresent()){
            return ResponseEntity.status(409).body("User already exists");

        }

            else{
                return ResponseEntity.status(401).body("You dont have access. Please contact your administrator");
            }}



    }



    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> signIn(@Valid @RequestBody LoginDTO loginDTO,
                                                     HttpServletResponse response){
        if(userRepository.existsByUsername(loginDTO.getUsername())){

        return userService.login(loginDTO, response);}
        else{
            throw new UserNotFound("Please register before login ");
        }

    }




    @GetMapping("/who")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        else{
            return userService.getUserRole(authHeader);
        }
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
