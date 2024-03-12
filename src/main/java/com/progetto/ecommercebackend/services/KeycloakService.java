package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.configurations.KeycloakConfig;
import com.progetto.ecommercebackend.entities.User;
import org.keycloak.admin.client.resource.*;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KeycloakService {

    @Value("${realm}")
    private String realm;
    @Autowired
    KeycloakConfig keycloak;

    public List<User> mapUsers(List<UserRepresentation> userRepresentations) {
        List<User> users = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(userRepresentations)) {
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
        /*
        user.setEmail(userRep.getEmail());
        user.setRoles(extractRoles(userRep));
        //user.setCreatedAt(extractCreatedAt(userRep));
        user.setMobile(extractAttribute(userRep,"mobile"));
        user.setAddress(extractAttribute(userRep,"address"));
         */
        return user;
    }

    private Set<String> extractRoles(UserRepresentation userRepresentation){
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource =  usersResource.get(userRepresentation.getId());
        RoleMappingResource roleMappingResource = userResource.roles();
        RoleScopeResource roleScopeResource = roleMappingResource.realmLevel();
        List<RoleRepresentation> rolesRepresentation = roleScopeResource.listAll();
        Set<String> roleNames = rolesRepresentation.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toSet());
        return roleNames;
    }

    private LocalDateTime extractCreatedAt(UserRepresentation userRepresentation){
        long timestamp = userRepresentation.getCreatedTimestamp();
        LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return createdAt;
    }

    private static String extractAttribute( UserRepresentation userRepresentation, String property){
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
        /*
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);

         */
        return userRep;
    }

}
