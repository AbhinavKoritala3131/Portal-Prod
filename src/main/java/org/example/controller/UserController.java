package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.requestLogs.Mapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UpdateInfoDTO;
import org.example.entity.AuthorizedUser;
import org.example.dto.LoginDTO;
import org.example.entity.User;
import org.example.exception.UserExists;
import org.example.exception.UserNotFound;
import org.example.jwtConfig.JWTService;
import org.example.repository.AuthUsersRepo;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

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



// TO REGISTER THE USER
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid  @RequestBody User user) {
        Optional<AuthorizedUser> authUser = authUsersRepo.findByUsername(user.getUsername().toLowerCase());

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

/*  THIS IS FOR FUTURE REFRESHTOKENS
public ResponseEntity<Map<String,String>> signIn(@Valid @RequestBody LoginDTO loginDTO,
                                                 HttpServletResponse response){ */
// TO LOGIN THE USER
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> signIn(@Valid @RequestBody LoginDTO loginDTO){

        if (userRepository.existsByUsernameIgnoreCase(loginDTO.getUsername())) {


            logger.info("User Controller signIn: User {} requested to login ",loginDTO.getUsername());

            return userService.login(loginDTO);}
        else{
            logger.info("UserController signIn: User {} tried to login without registering",loginDTO.getUsername());

            throw new UserNotFound("Please register before login ");
        }

    }



//    TO REQUEST USER DETAILS AFTER SUCCESSFULL LOGIN
    @GetMapping("/who")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("UserController getCurrentUser: JWT Token not found");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        else{
            return userService.getUserRole(authHeader);
        }
    }

    //    GIVE AUTHORIZED ACCESS TO REGISTER
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authuser")
    public ResponseEntity<String> authEmp(@RequestBody AuthorizedUser authUser) {

        if(userRepository.existsByUsernameIgnoreCase(authUser.getUsername()) ||
                userRepository.existsById(authUser.getId())){
            throw new UserExists("User already Registered");
        }
        if (authUsersRepo.existsByUsernameIgnoreCase(authUser.getUsername()) ||
                authUsersRepo.existsById(authUser.getId())) {
            throw new UserExists("User already authorized");
        }
        authUser.setUsername(authUser.getUsername().toLowerCase());
        authUsersRepo.save(authUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS");

    }

    //    REMOVE USER FROM DB
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username) {

        if (id == null && (username == null || username.isEmpty())) {
            return ResponseEntity.badRequest().body("Either 'id' or 'username' must be provided.");
        }
        return userService.deleteUserFromDB(id, username);

    }

    //         API IN PROGRESS : TO UPDATE THE USER INFO FROM PROFILE
    @PutMapping("/updateInfo")
    public ResponseEntity<User> updateInfo(@RequestBody UpdateInfoDTO infoDto) {

        // Step 1: Fetch the existing user from the database (using empId)
        Optional<User> userx = userRepository.findById(infoDto.getEmpId());


        // Step 2: If user doesn't exist, return 404 Not Found
        if (!userx.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userx.get();

        // Step 3: Update non-sensitive fields
        if (infoDto.getFname() != null && !infoDto.getFname().isEmpty()) {
            user.setFname(infoDto.getFname());
            user.setName(user.getName());
        }
        if (infoDto.getLname() != null && !infoDto.getLname().isEmpty()) {
            user.setLname(infoDto.getLname());
            user.setName(user.getName());
        }
        if (infoDto.getMobile() != null && !infoDto.getMobile().isEmpty()) {
            String sanitizedMobile = infoDto.getMobile().replaceAll("[^0-9+]", "");  // Allow only numbers and '+'
            user.setMobile(sanitizedMobile);        }
        user.updatedName(user.getFname(), user.getLname());

        // Step 4: Save the updated user back to the database
        User updatedUser = userRepository.save(user);

        // Step 5: Nullify sensitive fields before sending response
        updatedUser.setPassword(null);  // Ensure password is not returned
        updatedUser.setSsn(null);       // Ensure SSN is not returned
        // You can nullify other sensitive fields here as well (if any)

        // Step 6: Return the updated user object without sensitive data
        return ResponseEntity.ok(updatedUser);
    }



}
