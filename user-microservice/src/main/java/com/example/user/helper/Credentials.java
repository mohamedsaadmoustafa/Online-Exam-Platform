package com.example.user.helper;

import org.keycloak.representations.idm.CredentialRepresentation;

public class Credentials {
    public static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        return credentials;
    }
}
