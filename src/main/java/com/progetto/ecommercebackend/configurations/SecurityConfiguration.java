package com.progetto.ecommercebackend.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


import com.progetto.ecommercebackend.support.jwt.JwtAuthConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/books/**").permitAll()
                        .requestMatchers("/api/v1/categories/**").permitAll()
                        .requestMatchers("/api/v1/authors/**").permitAll()
                        .requestMatchers("/api/v1/orders/**").hasRole("user")
                        .requestMatchers("/api/v1/customers/**").hasRole("user")
                        .requestMatchers("/admin/**").hasRole("admin") //ADMIN
                        .anyRequest().authenticated()
                );
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .oauth2ResourceServer()
                .jwt()  // i have a jwt that has to be validated using oauth2ResourceServer
                //i need to add few parmeters or configurations to my application context in order to tell spring what is my resource server; url to validate token -> go to application.properties to add configs
                .jwtAuthenticationConverter(jwtAuthConverter) // iw want to mention to psring that i want to use my own jwt converter instead of the default on
        ;

        return http.build();
    }



}
