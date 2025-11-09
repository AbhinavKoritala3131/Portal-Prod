package org.example.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    //    THIS API IS FOR HEALTH CHECKING BY TARGET GROUP
    @GetMapping("/health")
    public ResponseEntity health() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
