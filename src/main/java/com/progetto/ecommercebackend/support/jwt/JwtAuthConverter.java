package com.progetto.ecommercebackend.support.jwt;

import com.progetto.ecommercebackend.entities.User;
import io.micrometer.common.KeyValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principal-attribute}")
    private String principalAttribute;

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt){
        Collection<GrantedAuthority> authorities = Stream.concat(
                        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractResourceRoles(jwt).stream()
                )
                .collect(Collectors.toSet());

        String userId = jwt.getSubject();
        String firsname = jwt.getClaim("given_name");
        String  lastname = jwt.getClaim("family_name");
        String email = jwt.getClaim("email");

        Map<String, Object> claims = jwt.getClaims();

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipleClaimName(jwt)
        );
    }

    private String getPrincipleClaimName(Jwt jwt) {
        String claimName= JwtClaimNames.SUB; //default in jwt token
        if (principalAttribute !=null){
            claimName= principalAttribute;
        }
        return jwt.getClaim(claimName);
    } //to extract preferred_username from jwt token


    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (jwt.getClaim("resource_access")== null) {
            return Set.of(); //empty set
        }
        resourceAccess= jwt.getClaim("resource_access");
        if(resourceAccess.get(resourceId)==null){
            return Set.of();
        }
        resource=(Map<String, Object>) resourceAccess.get(resourceId);

        resourceRoles= (Collection<String>)resource.get("roles");



        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority(("ROLE_"+role)))
                .collect(Collectors.toSet());
    }
}
