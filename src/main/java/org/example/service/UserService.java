package org.example.service;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.AuthorizedUser;
import org.example.dto.LoginDTO;
import org.example.entity.User;
import org.example.exception.InvalidCredentialsException;
import org.example.exception.UserNotFound;
import org.example.jwtConfig.JWTService;
import org.example.repository.AuthUsersRepo;
import org.example.repository.ClockRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserStatusRepository;
import org.example.security.SSNEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthUsersRepo authUsersRepo;
    @Autowired
    ClockRepository clockRepository;
    @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private UserStatusRepository userStatusRepository;

//    TO REGISTER THE USER
    public ResponseEntity<String> register(User user) {
        AuthorizedUser au = authUsersRepo.getByUsername(user.getUsername().toLowerCase());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setName(user.getName());
        user.updatedName(user.getFname(), user.getLname());
        user.setUsername(user.getUsername().toLowerCase());


        try {
            String encryptedSSN = SSNEncryptor.encrypt(user.getSsn());
            user.setSsn(encryptedSSN); // Before saving to DB
            // TO DECRYPT : String decryptedSsn = SSNEncryptor.decrypt(user.getSsn()); // Only for authorized roles
        } catch (Exception re) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid SSN");
        }

        user.setRole(au.getRole());
        user.setId(au.getId());
        authUsersRepo.delete(au); //Removes authUsers after registration.
        userRepository.save(user);
        return ResponseEntity.status(200).body(" You are all set "+user.getFname()+" !");

    }

//    THIS IS IN FUTURE FOR REFRESH TOKENS GENERATION
//    public ResponseEntity<Map<String,String>> login(LoginDTO l, HttpServletResponse response) {

    //    TO LOGIN THE USER
    public ResponseEntity<Map<String,String>> login(LoginDTO l) {        try {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(l.getUsername().toLowerCase(), l.getPassword()));

            if (authentication.isAuthenticated()) {
                String jwtTok=jwtService.generateToken(l.getUsername().toLowerCase()

                );




                Map<String,String> map = new HashMap<>();
                map.put("jwtToken",jwtTok);
                map.put("msg","Login Success");

                return ResponseEntity.ok(map);
            }
        }catch(Exception e) {
            throw new InvalidCredentialsException("Invalid Credentials");

        }
        return null;



    }


    //    TO REQUEST USER DETAILS AFTER SUCCESSFULL LOGIN

    public ResponseEntity<?> getUserRole(String authHeader) {
        String token = authHeader.substring(7);
        try {
            String username = jwtService.extractUserName(token);
            Optional<User> userOpt = userRepository.findByUsername(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }


            User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("userId", user.getId());
            response.put("fName", user.getFname());
            response.put("ClockStatus", timesheetService.userStat(user.getId()));
            response.put("country", user.getCountry());
            response.put("DOB", user.getDob());
            response.put("mobile", user.getMobile());
            response.put("lName", user.getLname());
            return ResponseEntity.ok(response);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

    }


    //    REMOVE USER FROM DB
    @Transactional
    public ResponseEntity<String> deleteUserFromDB(Long id, String username) {
        Optional<User> userOpt = (id != null)
                ? userRepository.findById(id)
                : userRepository.findByUsername(username);

        boolean deleted = false;

        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();

            // **Delete all dependent clocks first**
            clockRepository.deleteByUserId(userId);
            userStatusRepository.deleteById(userId);


            // Then delete the user
            userRepository.deleteById(userId);

            deleted = true;


        }
        else{
            Optional<AuthorizedUser> AUser= (id != null)
                    ? authUsersRepo.findById(id)
                    : authUsersRepo.findByUsername(username);
            if (AUser.isPresent()) {
                Long userId = AUser.get().getId();
                // Then delete the user
                authUsersRepo.deleteById(userId);
                deleted = true;

            }
        }

        if (deleted) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }



    }



}
