package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.configuration.ApplicationContextHolder;
import org.jruchel.carworkshop.utils.Properties;
import org.jruchel.carworkshop.models.Email;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@DependsOn("Properties")
@Service
public class MailingService {

    private final String sender;
    private final JavaMailSender javaMailSender;
    private final Properties properties = ApplicationContextHolder.getContext().getBean(Properties.class);

    public MailingService(JavaMailSender javaMailSender) {
        this.sender = properties.getUsername();
        this.javaMailSender = javaMailSender;
    }

    private void sendEmail(String from, String to, String subject, String message, List<Byte[]> attachments) throws MessagingException {
        if (attachments == null || attachments.size() == 0) {
            sendEmail(from, to, subject, message);
        }
    }

    public void sendEmail(String to, String subject, String content, boolean async) throws MessagingException {
        if (async) {
            new Thread(() -> {
                try {
                    sendEmail(sender, to, subject, content);
                } catch (MessagingException ignored) {
                }
            }).start();
        } else {
            sendEmail(sender, to, subject, content);
        }
    }

    public void sendEmail(Email email, boolean async) throws MessagingException {
        if (async) {
            new Thread(() -> {
                try {
                    sendEmail(sender, email.getTo(), email.getSubject(), email.getMessage(), email.getAttachments());
                } catch (MessagingException ignored) {

                }
            }).start();
        } else {
            sendEmail(sender, email.getTo(), email.getSubject(), email.getMessage(), email.getAttachments());
        }
    }

    private void sendEmail(String from, String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        messageHelper.setText(content, true);
        messageHelper.setSubject(subject);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        javaMailSender.send(mimeMessage);
    }
}
