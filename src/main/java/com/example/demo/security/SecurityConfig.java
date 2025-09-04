package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Désactiver CSRF pour les tests
            .authorizeHttpRequests()
                .requestMatchers("/api/commands/**").permitAll()  // Permettre tous les accès aux commandes
                .requestMatchers("/api/health").permitAll()       // Health check public
                .anyRequest().authenticated()  // Autres endpoints protégés
            .and()
            .httpBasic();
        
        return http.build();
    }
}
