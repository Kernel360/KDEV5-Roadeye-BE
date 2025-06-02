package org.re.notification.mail;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailClient {

    private final JavaMailSender mailSender;

    public void sendMail(String to, String subject, String content) {
        var mimeMessage = mailSender.createMimeMessage();
        try {
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, false);
            mailSender.send(mimeMessage);
        } catch (MessagingException ignored) {
        }
    }
}
