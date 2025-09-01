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
         // Dans SecurityConfig.java

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/commands/**").permitAll()
                .requestMatchers("/commands/health").permitAll() // Ajoute cette ligne
                .anyRequest().authenticated()
            );

        // Si tu utilises un filtre Firebase, tu peux le commenter ou le laisser pour le moment
        // http.addFilterBefore(new FirebaseAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
