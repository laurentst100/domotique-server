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
            .authorizeHttpRequests(authz -> authz
                // --- MODIFICATION IMPORTANTE ---
                // Autorise toutes les requêtes vers les endpoints de commande sans authentification
                .requestMatchers("/commands/**").permitAll() 
                // Exige une authentification pour toutes les autres requêtes
                .anyRequest().authenticated() 
            );

        // Si tu utilises un filtre Firebase, tu peux le commenter ou le laisser pour le moment
        // http.addFilterBefore(new FirebaseAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
