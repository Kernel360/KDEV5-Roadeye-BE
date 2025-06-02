package org.re.notification.webpush;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import org.springframework.stereotype.Component;

@Component
public class WebPushClient {

    // TODO 메시지 전송 실패 및 복구 처리 기능 고민
    public void send(String token, String title, String body) {
        sendWebPush(token, title, body);
    }

    public void sendWebPush(String registrationToken, String title, String body) {
        Message message = Message.builder()
            .setToken(registrationToken)
            .setWebpushConfig(WebpushConfig.builder()
                .setNotification(new WebpushNotification(title, body, null))
                .build())
            .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            throw new RuntimeException("FCM WebPush 전송 실패", e);
        }
    }
}
