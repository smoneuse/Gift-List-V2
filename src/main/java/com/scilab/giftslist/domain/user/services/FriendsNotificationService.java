package com.scilab.giftslist.domain.user.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.infra.errors.DefaultException;
import com.scilab.giftslist.infra.mail.MailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FriendsNotificationService {
    private static final String MAIL_FRIEND_REQUEST_NOTIFICATION_TEMPLATE="templates/FriendRequestNotification.html";
    private static final String MAIL_FRIEND_REQUEST_ACCEPTED_TEMPLATE="templates/AcceptedFriendRequest.html";
    private static final String MAIL_FRIEND_REQUEST_REFUSED_TEMPLATE="templates/RefusedFriendRequest.html";
    private static final String MAIL_FRIEND_REQUEST_SUBJECT="Un utilisateur souhaite accéder à vos lites de cadeaux !";
    private static final String MAIL_FRIEND_REQUEST_ACCEPTED_SUBJECT="Vous avez accès à de nouvelles listes de cadeaux !";
    private static final String MAIL_FRIEND_REQUEST_REFUSED_SUJECT="Votre demande d'accès à de nouvelles listes a été refusée";
    private static final String RECIEVER_TAG="{{RECIEVER_NAME}}";
    private static final String SENDER_TAG="{{SENDER_NAME}}";

    @Value(MAIL_FRIEND_REQUEST_NOTIFICATION_TEMPLATE)
    private File notificationMailTemplate;
    @Value(MAIL_FRIEND_REQUEST_ACCEPTED_TEMPLATE)
    private File acceptedRequestMailTemplate;
    @Value(MAIL_FRIEND_REQUEST_REFUSED_TEMPLATE)
    private File refusedRequestMailTemplate;

    
    @Autowired
    private MailService mailService;


    public void notifyFriendRequest(FriendsActor actors){
        log.info("Sending notification mail to {}", actors.reciever().getUsername());
        try{
            String content = Files.readString(notificationMailTemplate.toPath());
            content = StringUtils.replace(content, RECIEVER_TAG, actors.reciever().usageName());
            content = StringUtils.replace(content, SENDER_TAG, actors.sender().usageName());
            mailService.sendEmail(actors.reciever().getEmail(), MAIL_FRIEND_REQUEST_SUBJECT, content);
        }
        catch(IOException ioe){
            log.error("IOException wfile sending mail : {}", ioe.getMessage());
            throw new DefaultException("IOException while sending friend request notification :"+ioe.getMessage());
        }
    }

    public void notifyRequestAccepted(FriendsActor actors){
        log.info("Sending accepted notification mail to {}", actors.sender().getUsername());
        try{
            String content = Files.readString(acceptedRequestMailTemplate.toPath());
            content = StringUtils.replace(content, RECIEVER_TAG, actors.reciever().usageName());
            content = StringUtils.replace(content, SENDER_TAG, actors.sender().usageName());
            mailService.sendEmail(actors.sender().getEmail(), MAIL_FRIEND_REQUEST_ACCEPTED_SUBJECT, content);
        }
        catch(IOException ioe){
            log.error("IOException wfile sending mail : {}", ioe.getMessage());
            throw new DefaultException("IOException while sending friend accepted notification :"+ioe.getMessage());
        }
    }

    public void notifyRequestRefused(FriendsActor actors){
        log.info("Sending refused notification mail to {}", actors.sender().getUsername());
        try{
            String content = Files.readString(refusedRequestMailTemplate.toPath());
            content = StringUtils.replace(content, RECIEVER_TAG, actors.reciever().usageName());
            content = StringUtils.replace(content, SENDER_TAG, actors.sender().usageName());
            mailService.sendEmail(actors.sender().getEmail(), MAIL_FRIEND_REQUEST_REFUSED_SUJECT, content);
        }
        catch(IOException ioe){
            log.error("IOException wfile sending mail : {}", ioe.getMessage());
            throw new DefaultException("IOException while sending friend refused notification :"+ioe.getMessage());
        }
    }

}
