package com.example.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseAuth firebaseAuth() throws Exception {
        // Dev: tenter de charger la clé service depuis le classpath
        try (InputStream serviceAccount =
                     getClass().getResourceAsStream("/firebase-service-account.json")) {
            if (serviceAccount != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
                return FirebaseAuth.getInstance();
            }
        }

        // Prod: si le fichier n’est pas dans le classpath,
        // utiliser les Default Credentials (ex. GOOGLE_APPLICATION_CREDENTIALS)
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp();
        }
        return FirebaseAuth.getInstance();
    }
}
