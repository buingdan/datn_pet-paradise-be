package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/admin")
    public ResponseEntity<?> admin(){
        return ResponseEntity.ok("Day la route admin");
    }

    @GetMapping("/users")
    public ResponseEntity<?> users(){
        return ResponseEntity.ok("Day la route users");
    }
}
