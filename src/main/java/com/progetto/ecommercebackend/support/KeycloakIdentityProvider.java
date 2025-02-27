package com.progetto.ecommercebackend.support;


import com.progetto.ecommercebackend.configurations.KeycloakConfig;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.kie.internal.identity.IdentityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class KeycloakIdentityProvider {


    Keycloak keycloak;

    @Value("${server-url}")
    private String serverUrl;

    @Value("${realm}")
    private String realm ;

    @Value("${client-id}")
    private String clientId ;

    @Value("${grant-type}")
    private String grantType;

    @Value("${name}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${clientSecret")
    private String clientSecret;

    private static final Logger logger = LoggerFactory.getLogger(KeycloakIdentityProvider.class);


    @Bean
    public IdentityProvider identityProvider(ApplicationContext context){
        Environment env = context.getEnvironment();
        Keycloak keycloakAdminClient = KeycloakBuilder.builder()
                .serverUrl(env.getProperty(serverUrl))
                .realm(env.getProperty(realm))
                .clientId(env.getProperty(clientId))
                .username(env.getProperty(username))
                .password(env.getProperty(password))
                .build();

        return new IdentityProvider() {

            @Override
            public String getName() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String name = "";
                if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof KeycloakPrincipal) {
                    name = ((KeycloakPrincipal<?>) auth.getPrincipal()).getName();
                }
                logger.debug("getName: {}", name);
                return name;
            }
            
            public List<String> getRolesFor(String userId) {
                if (getName().equals(userId)) {
                    return getRoles();
                } else {
                    // presumed this is cached.

                    try {
                        UsersResource usersResource = keycloakAdminClient.realm(env.getProperty("keycloak.realm")).users();
                        List<UserRepresentation> users = usersResource.search(userId);
                        if (users.isEmpty()) {
                            return Collections.emptyList();
                        }
                        UserRepresentation user = users.get(0);
                        return usersResource.get(user.getId()).roles().realmLevel().listAll().stream().map(RoleRepresentation::getName).collect(Collectors.toList());
                    } catch (Exception e) {
                        logger.debug("getRolesFor({}) caused an error while querying keycloack", userId, e);
                        return Collections.emptyList();
                    }
                }
            }

            @Override
            public List<String> getRoles() {
                List<String> roles = new ArrayList<>();
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                if (auth != null && auth.isAuthenticated() && auth instanceof KeycloakAuthenticationToken) {
                    roles = ((KeycloakAuthenticationToken) auth).getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());
                }
                logger.debug("getRoles: {}", roles);
                return roles;
            }

            @Override
            public boolean hasRole(String role) {
                List<String> keycloakRoles = getRoles();
                return (keycloakRoles != null && keycloakRoles.contains(role));
            }
        };
    }
}
