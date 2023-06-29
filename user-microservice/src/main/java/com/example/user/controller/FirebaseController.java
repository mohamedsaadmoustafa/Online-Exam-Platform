package com.example.user.controller;

import com.example.user.service.FirebaseService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin
public class FirebaseController {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private final FirebaseService firebaseService;

    public FirebaseController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/{username}/token")
    public ResponseEntity<String> saveFirebaseToken(@PathVariable("username") String username, @RequestBody String token) {
        firebaseService.saveNewFirebaseToken(username, token);
        LOG.info("New user and token saved: " + username);
        return ResponseEntity.ok("Firebase token saved successfully.");
    }

    @GetMapping("/{username}/token")
    public String getFirebaseToken(@PathVariable("username") String userId) {
        return firebaseService.getFirebaseToken(userId);
    }
}
