package com.example.user.helper;

import com.example.user.http.requests.RegisterRequest;
import org.keycloak.representations.idm.CredentialRepresentation;

import java.util.Collections;

public class userRepresentation {
    public static org.keycloak.representations.idm.UserRepresentation createUserRepresentation(RegisterRequest userRegisterRequest, CredentialRepresentation passwordCredential) {
        org.keycloak.representations.idm.UserRepresentation userRepresentation = new org.keycloak.representations.idm.UserRepresentation();
        userRepresentation.setUsername(userRegisterRequest.getEmail());
        userRepresentation.setCredentials(Collections.singletonList(passwordCredential));
        userRepresentation.setFirstName(userRegisterRequest.getFirstname());
        userRepresentation.setLastName(userRegisterRequest.getLastname());
        userRepresentation.setEmail(userRegisterRequest.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        return userRepresentation;
    }
}
