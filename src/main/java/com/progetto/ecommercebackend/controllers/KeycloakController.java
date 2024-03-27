package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.configurations.KeycloakConfig;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.services.KeycloakService;
import org.springframework.http.HttpStatus;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class KeycloakController {

    @Autowired
    KeycloakConfig keycloakUtil;

    @Autowired
    KeycloakService keycloakService;

    @Value("${realm}")
    private String realm;

    @Value("${client-id}")
    private String clientId;


    //READ
    @GetMapping("/keycloak/users")
    @PreAuthorize("hasRole('ROLE_admin')")
    public List<User> getAllUsers(){
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations=
                keycloak.realm(realm).users().list();
        return keycloakService.mapUsers(userRepresentations);
    }

    //DELETE
    @RequestMapping(value = "/keycloak/users", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserAccount(@RequestParam(name = "id") String userId){
       keycloakService.deleteUserAccount(realm, userId);
        return new ResponseEntity<>("Account deleted!", HttpStatus.ACCEPTED);
    }

    //UPDATE
    @RequestMapping(value = "keycloak/users", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUserAccount(@RequestParam(name = "id") String userId, String firstname, String lastname){
        UserRepresentation userRepresentation = keycloakService.updateUserAccount(userId, firstname, lastname);
        User updatedUser = new User();
        updatedUser.setId(userRepresentation.getId());
        updatedUser.setFirstName(userRepresentation.getFirstName());
        updatedUser.setLastName(userRepresentation.getLastName());
        updatedUser.setEmail(userRepresentation.getEmail());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }



}
