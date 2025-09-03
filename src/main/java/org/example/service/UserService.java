package org.example.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.entity.Login;
import org.example.entity.User;
import org.example.exception.UserNotFound;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;
    public User registerUser(User user) {
        // Hash the plain password before saving

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.updatedName(user.getFname(),user.getLname());

        // Save user to DB (using repository)
        return userRepository.save(user);
    }
    public Map<String, String> login(Login l){
        if(userRepository.existsByEmail(l.getEmail()) && userRepository.existsByPassword(l.getPassword())){
        Map<String,String> response = new HashMap<>();
        response.put("fname",userRepository.findByEmail(l.getEmail()).getFname());
        return response; }
        else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }


    }

}
