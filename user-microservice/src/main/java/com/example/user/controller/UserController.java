package com.example.user.controller;

import com.example.user.http.requests.RegisterRequest;
import com.example.user.http.requests.LoginRequest;
import com.example.user.service.UserService;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Set;


@RestController
@RequestMapping("/")
@CrossOrigin
public class UserController {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest userRegisterRequest) {
        try (Response createdResponse = userService.createKeycloakUser(userRegisterRequest)) {
            return ResponseEntity.status(createdResponse.getStatus()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody LoginRequest loginRequest) {
        AccessTokenResponse accessTokenResponse = userService.login(loginRequest);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }
    }

    @GetMapping("/student")
    @ResponseBody
    public Set<UserRepresentation> getStudentsByRole() throws NotFoundException {
        return userService.getUsersByRole("student");
    }
}
