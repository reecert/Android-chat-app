package com.example.chatserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        GoogleCredentials credentials;

        // Try loading service account key from classpath first
        ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
        if (resource.exists()) {
            log.info("Loading Firebase credentials from serviceAccountKey.json on classpath");
            try (InputStream is = resource.getInputStream()) {
                credentials = GoogleCredentials.fromStream(is);
            }
        } else {
            // Fall back to Application Default Credentials (GOOGLE_APPLICATION_CREDENTIALS env var)
            log.info("serviceAccountKey.json not found — using Application Default Credentials");
            credentials = GoogleCredentials.getApplicationDefault();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId("realtime-chat-demo")
                .build();

        log.info("Firebase initialized with project: realtime-chat-demo");
        return FirebaseApp.initializeApp(options);
    }
}
