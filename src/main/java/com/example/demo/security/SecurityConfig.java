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
            .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF pour les API
            .authorizeHttpRequests(authorize -> authorize
                // Autorise toutes les requêtes vers les commandes
                .requestMatchers("/commands/**").permitAll()
                // Autorise toutes les requêtes vers le test de santé
                .requestMatchers("/health").permitAll()
                // Exige une authentification pour toutes les autres requêtes (pour l'instant, aucune)
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
