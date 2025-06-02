package org.re.notification.webpush;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

// 운영 환경에서만 fcm 사용하도록
@Component
@Profile("prod")
public class FcmInitializer implements ApplicationRunner {

    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (InputStream serviceAccount = ResourceUtils.getURL(credentialsPath).openStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize FirebaseApp", e);
        }
    }
}
