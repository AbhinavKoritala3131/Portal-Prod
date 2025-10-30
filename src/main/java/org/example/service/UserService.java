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
import org.example.repository.UserRepository;
import org.example.security.SSNEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
    private TimesheetService timesheetService;


    public ResponseEntity<String> register(User user) {
        AuthorizedUser au = authUsersRepo.getByUsername(user.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setName(user.getName());
        user.updatedName(user.getFname(), user.getLname());
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

    public ResponseEntity<Map<String,String>> login(LoginDTO l, HttpServletResponse response) {
        try {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(l.getUsername(), l.getPassword()));
            if (authentication.isAuthenticated()) {
                String jwtTok=jwtService.generateToken(l.getUsername()
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




}
