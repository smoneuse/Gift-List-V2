package com.scilab.giftslist.infra.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.infra.errors.DefaultException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    
    private final JavaMailSender mailSender;

    @Value("${gift-list.mail.from}")
    private String mailFrom;

    @Value("${gift-list.mail.active}")
    private boolean active;
    
    public void sendEmail(String to, String subject, String body) {
        log.info("Sending a mail to [{}] with subject [{}]", to, subject);
        if(!active){
            log.warn("Mailing is disabled by configuration - No mail sent.");
            return;
        }
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(body, true); // Use this or above line.
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("gift.list@free.fr");            
            mailSender.send(mimeMessage);            
        }
        catch(MessagingException me){
            log.error("Error wile sending a e-mail : [{}]", me.getMessage());
            throw new DefaultException(me.getMessage());
        }
    }
}
