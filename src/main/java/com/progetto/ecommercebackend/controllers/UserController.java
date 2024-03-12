package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.configurations.KeycloakConfig;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.services.KeycloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    KeycloakConfig keycloakUtil;

    @Autowired
    KeycloakService keycloakService;

    @Value("${realm}")
    private String realm;

    @Value("${client-id}")
    private String clientId;

    @GetMapping("/keycloak/users")
    @PreAuthorize("hasRole('ROLE_admin')")
    public List<User> getAllUsers(){
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations=
                keycloak.realm(realm).users().list();
        return keycloakService.mapUsers(userRepresentations);
    }



}
