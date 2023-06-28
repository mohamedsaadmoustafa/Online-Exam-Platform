package com.example.user.http.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    String username;
    String password;
    String email;
    String firstname;
    String lastname;
    String role;
}
