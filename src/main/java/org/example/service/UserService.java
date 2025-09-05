package org.example.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.entity.Login;
import org.example.entity.User;
import org.example.exception.InvalidCredentialsException;
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


        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.updatedName(user.getFname(), user.getLname());
        String Maskedssn= "***-**"+user.getSsn();
        user.setSsn(Maskedssn);


        return userRepository.save(user);
    }

    public Map<String, Object> login(Login l) {
        return userRepository.findByEmail(l.getEmail())
                .map(u -> {Map<String, Object> map = new HashMap<>();
                    if (passwordEncoder.matches(l.getPassword(), u.getPassword())) {

                        map.put("message", "Welcome " + u.getFname() + " !");
                        map.put("userId",u.getId());
                        return map;
                    } else {

                        throw new InvalidCredentialsException("Wrong email or password");
                    }

                }).orElseThrow(() ->  new UserNotFound("Please register before login"));

    }

    public Map<String,Object> getUser(long id){
        return userRepository.findById(id).map(u->{ Map<String, Object> map = new HashMap<>();
        map.put("userId", u.getId());
        map.put("fName", u.getFname());
        map.put("lName", u.getLname());
            map.put("userName", userRepository.getById(u.getId()).getName());
            map.put("email", u.getEmail());
            map.put("mobile", u.getMobile());
            map.put("country",  u.getCountry());
            map.put("DOB", u.getDob());
            return map;
        }).orElseThrow(()-> new UserNotFound("User Details Not Found"));
    }






}


