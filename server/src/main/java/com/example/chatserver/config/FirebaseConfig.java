package com.example.chatserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // ideally, we check if generic exists
        if (FirebaseApp.getApps().isEmpty()) {
            // For local emulator usage, we can mostly use dummy credentials
            // or rely on GOOGLE_APPLICATION_CREDENTIALS being set globally?
            // Since User requested "Demo-ready locally", we'll use a dummy credential for emulator.
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.newBuilder().build()) // Uses default
                    .setProjectId("realtime-chat-demo")
                    .build();

            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }
}
