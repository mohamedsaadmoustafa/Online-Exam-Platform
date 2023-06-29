package com.example.user.service;

import com.example.user.config.KeycloakProvider;
import com.example.user.controller.UserController;
import com.example.user.http.requests.LoginRequest;
import com.example.user.http.requests.RegisterRequest;
import com.example.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.user.helper.Credentials.createPasswordCredentials;
import static com.example.user.helper.userRepresentation.createUserRepresentation;
import static com.example.user.helper.validateRequests.validateRegisterRequest;

@Slf4j
@Service
public class UserService {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private final KeycloakProvider keycloakProvider;
    private final UserRepository userRepository;
    private final String realm;

    public UserService(
            KeycloakProvider keycloakProvider,
            @Value("${keycloak.realm}") String realm,
            UserRepository userRepository
    ) {
        this.keycloakProvider = keycloakProvider;
        this.realm = realm;
        this.userRepository = userRepository;
    }

    public Response createKeycloakUser(RegisterRequest user) throws Exception {
        validateRegisterRequest(user);

        CredentialRepresentation passwordCredential = createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = createUserRepresentation(user, passwordCredential);

        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == 201) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            RolesResource roleResource = keycloakProvider.getInstance().realm(realm).roles();
            RoleRepresentation role;
            if (user.getRole().equals("teacher")) {
                role = roleResource.get("teacher").toRepresentation();
            } else {
                role = roleResource.get("student").toRepresentation();
            }
            List<RoleRepresentation> rolesToAdd = new ArrayList<>();
            rolesToAdd.add(role);
            keycloakProvider.getInstance().realm(realm).users().get(userId).roles().realmLevel().add(rolesToAdd);
            LOG.info(response.getStatus() + " Created: User Created successfully.");
            LOG.info("User role added successfully to user.");
        } else if (response.getStatus() == 400) {
            LOG.warn(response.getStatus() + " Bad Request: Request body does not match user requirements.");
        } else if (response.getStatus() == 409) {
            LOG.warn(response.getStatus() + " Conflict: User email or username found.");
        } else {
            LOG.warn("Error Code: " + response.getStatus());
            throw new Exception();
        }
        return response;
    }

    public AccessTokenResponse login(LoginRequest loginRequest) {
        Keycloak keycloak = keycloakProvider.newKeycloakBuilderWithPasswordCredentials(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ).build();
        return keycloak.tokenManager().getAccessToken();
    }

    public Set<UserRepresentation> getUsersByRole(String roleName) throws NotFoundException {
        if (roleName.equals("student") || roleName.equals("teacher")){
            return keycloakProvider.getInstance().realm(realm)
                    .roles()
                    .get(roleName)
                    .getRoleUserMembers();
        }
        else {
            throw new NotFoundException();
        }
    }
}
