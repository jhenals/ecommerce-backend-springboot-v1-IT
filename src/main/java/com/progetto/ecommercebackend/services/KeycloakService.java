package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.configurations.KeycloakConfig;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.repositories.OrderBookRepository;
import com.progetto.ecommercebackend.repositories.OrderRepository;
import com.progetto.ecommercebackend.repositories.UserRepository;
import com.progetto.ecommercebackend.support.KeycloakIdentityProvider;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.resource.*;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.kie.internal.identity.IdentityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.security.core.Authentication;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakService {
    @Autowired
    private OrderBookRepository orderBookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${realm}")
    private String realm;
    @Autowired
    KeycloakConfig keycloak;


    public String getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        String userId = authentication.getName(); //this will be keycloak id
        if (token.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) token.getPrincipal();
            //option to use username instead of id
            if (kp.getKeycloakSecurityContext().getToken() != null && kp.getKeycloakSecurityContext().getToken().getPreferredUsername() != null) {
                //userId = kp.getKeycloakSecurityContext().getToken().getPreferredUsername(); //replace with username - could be changed to e.g. email if desired
                userId = kp.getKeycloakSecurityContext().getToken().getId();
            }
        }
        //setAuthenticatedUserId(userId);
       //return super.authenticate(authentication);
        return userId;
    }


    public KeycloakSecurityContext getSecurityContext(){

        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
            return authentication.getAccount().getKeycloakSecurityContext();
        }
        return null;
    }

    public List<User> mapUsers(List<UserRepresentation> userRepresentations) {
        List<User> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
            userRepresentations.forEach(userRep -> {
                users.add(mapUser(userRep));
            });
        }
        return users;
    }

    private User mapUser(UserRepresentation userRep) {
        User user = new User();
        user.setId(userRep.getId());
        user.setFirstName(userRep.getFirstName());
        user.setLastName(userRep.getLastName());
        user.setEmail(userRep.getEmail());
        user.setRoles(extractRoles(userRep));
        user.setCreatedAt(extractCreatedAt(userRep));
        return user;
    }

    private Set<String> extractRoles(UserRepresentation userRepresentation) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(userRepresentation.getId());
        RoleMappingResource roleMappingResource = userResource.roles();
        RoleScopeResource roleScopeResource = roleMappingResource.realmLevel();
        List<RoleRepresentation> rolesRepresentation = roleScopeResource.listAll();
        Set<String> roleNames = rolesRepresentation.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toSet());
        return roleNames;
    }

    private LocalDateTime extractCreatedAt(UserRepresentation userRepresentation) {
        long timestamp = userRepresentation.getCreatedTimestamp();
        LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return createdAt;
    }

    private static String extractAttribute(UserRepresentation userRepresentation, String property) {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.size() > 0) {
            List<String> properties = attributes.get(property);
            if (!CollectionUtils.isEmpty(properties)) {
                return properties.get(0);
            }
        }
        return null;
    }

    private static UserRepresentation mapUserRep(User user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(user.getId());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        return userRep;
    }

    public Optional<UserRepresentation> getUserById(String userId) {
        UsersResource usersResource = keycloak.realm(realm).users();
        return Optional.ofNullable(usersResource.get(userId).toRepresentation());
    }

    public void deleteUserAccount(String realm, String userId) {
      //  log.info("Deleting user {} in realm {}", user.getSpec().getUsername(), realm);
        Optional<UserRepresentation> userRepresentation = getUserById(userId);
        if( userRepresentation.isPresent() ){
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            usersResource.get(userId)
                    .remove();
            userRepository.deleteById(userId);
        }else{
            throw new CustomException("Errore nell'eliminare l'utente.");
        }

    }

    public UserRepresentation updateUserAccount(String userId, String firstname, String lastname) {
     //   log.info("Updated firstname:{}  and lastname:{} of id:{}", firstname,lastname, userId);
        RealmResource realmResource = keycloak.realm(realm);

        UserRepresentation userRep = new UserRepresentation();
        userRep.setFirstName(firstname);
        userRep.setLastName(lastname);
        UsersResource usersResource = realmResource.users();
        usersResource.get(userId).update(userRep);

        Optional<User> userOptional = userRepository.findById(userId);
        if( userOptional.isEmpty() ){
            throw new CustomException("Utente non trovato.");
        }else{
            User user = userOptional.get();
            user.setFirstName(firstname);
            user.setLastName(lastname);
            userRepository.save(user);
        }
        return userRep;
    }

}
