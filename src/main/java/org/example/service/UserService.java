package org.example.service;


import org.example.entity.AuthorizeUsers;
import org.example.dto.LoginDTO;
import org.example.entity.User;
import org.example.exception.InvalidCredentialsException;
import org.example.exception.UserNotFound;
import org.example.repository.AuthorizeUsersRepository;
import org.example.repository.UserRepository;
import org.example.security.SSNEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private AuthorizeUsersRepository authorizeUsersRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public User registerUser(User user) {

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.updatedName(user.getFname(), user.getLname());
        Optional<AuthorizeUsers> au=authorizeUsersRepository.getByEmail(user.getEmail());
        if(au.isPresent()){ AuthorizeUsers xy= au.get();
            user.setAuthorizeUsers(xy);
            user.setRole(xy.getRole());
        }


        try {
            String encryptedSSN=SSNEncryptor.encrypt(user.getSsn());
            user.setSsn(encryptedSSN); // Before saving to DB
            // TO DECRYPT : String decryptedSsn = SSNEncryptor.decrypt(user.getSsn()); // Only for authorized roles
        }catch(Exception re){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid SSN");
        }


        return userRepository.save(user);
    }

    public Map<String, Object> login(LoginDTO l) {
        return userRepository.findByEmail(l.getEmail())
                .map(u -> {Map<String, Object> map = new HashMap<>();
                    if (passwordEncoder.matches(l.getPassword(), u.getPassword())) {

                        map.put("message", "Welcome " + u.getFname() + " !");
                        map.put("userId",u.getId());
                        map.put("role",u.getRole());
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
            map.put("role", u.getRole());
            return map;
        }).orElseThrow(()-> new UserNotFound("User Details Not Found"));
    }






}


