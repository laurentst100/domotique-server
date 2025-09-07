package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF car tu utilises probablement une API sans état (stateless)
            .csrf(csrf -> csrf.disable())
            
            // Définir les règles d'autorisation
            .authorizeHttpRequests(auth -> auth
                // Autoriser l'accès à tes endpoints d'API sans authentification
                .requestMatchers("/api/**").permitAll() 
                // Exiger une authentification pour toutes les autres requêtes
                .anyRequest().authenticated()
            )
            
            // Utiliser la configuration httpBasic par défaut si nécessaire
            .httpBasic(withDefaults())
            
            // Configurer la gestion de session pour qu'elle soit stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
