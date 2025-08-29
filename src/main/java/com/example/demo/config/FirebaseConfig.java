package com.example.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

  @Bean
  public FirebaseAuth firebaseAuth() throws IOException {
    String base64 = System.getenv("FIREBASE_CONFIG_BASE64");
    if (base64 == null || base64.isBlank()) {
      throw new IOException("FIREBASE_CONFIG_BASE64 not set");
    }
    byte[] decoded = Base64.getDecoder().decode(base64);
    try (InputStream is = new ByteArrayInputStream(decoded)) {
      GoogleCredentials creds = GoogleCredentials.fromStream(is);
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(creds)
          .build();
      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
      }
      return FirebaseAuth.getInstance();
    }
  }
}
