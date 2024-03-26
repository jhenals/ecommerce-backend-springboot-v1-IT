package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.configurations.KeycloakConfig;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.*;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakService {

    @Value("${realm}")
    private String realm;
    @Autowired
    KeycloakConfig keycloak;

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
        }else{
            throw new CustomException("Cannot delete user");
        }

    }

    /*
    public void updateUser(String userId, UserDTO userDTO){
    CredentialRepresentation credential = Credentials
            .createPasswordCredentials(userDTO.getPassword());
    UserRepresentation user = new UserRepresentation();
    user.setUsername(userDTO.getUserName());
    user.setFirstName(userDTO.getFirstname());
    user.setLastName(userDTO.getLastName());
    user.setEmail(userDTO.getEmailId());
    user.setCredentials(Collections.singletonList(credential));

    UsersResource usersResource = getInstance();
    usersResource.get(userId).update(user);
}
     */
}
