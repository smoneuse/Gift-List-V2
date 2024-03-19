package com.scilab.giftslist.domain.discussion.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.discussion.Discussion;
import com.scilab.giftslist.domain.discussion.DiscussionComment;
import com.scilab.giftslist.domain.gift.Gift;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.DefaultException;
import com.scilab.giftslist.infra.mail.MailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DiscussionNotificationService {
    private static final String DISCUSSION_TEMPLATE="templates/GiftDiscussionUpdate.html";
    private static final String DISCUSSION_NOTIFICATION_SUBJECT="Nouveau commentaire sur un cadeau de Gift list !";
    private static final String RECIEVER_NAME_TAG="{{RECIEVER_NAME}}";
    private static final String GIFT_LIST_TITLE_TAG="{{GIFT_LIST_TITLE}}";
    private static final String OWNER_USAGENAME_TAG="{{OWNER_USAGENAME}}";
    private static final String GIFT_TITLE_TAG="{{GIFT_TITLE}}";
    private static final String AUTHOR_USAGENAME_TAG="{{AUTHOR_USAGENAME}}";
    private static final String GIFT_COMMENT_TAG="{{GIFT_COMMENT}}";

    @Value(DISCUSSION_TEMPLATE)
    private File templateFile;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;
    
    public void notifyForDiscussion(Discussion discussion, DiscussionComment comment, Gift commentedGift){
        Set<String> recieversIds= recieversIds(discussion, comment);
        try{
                String content = Files.readString(templateFile.toPath());
                content = StringUtils.replace(content, GIFT_LIST_TITLE_TAG, commentedGift.getOwningList().getName());
                content = StringUtils.replace(content, OWNER_USAGENAME_TAG, commentedGift.getOwningList().getOwner().usageName());
                content = StringUtils.replace(content, GIFT_TITLE_TAG, commentedGift.getTitle());
                content = StringUtils.replace(content, AUTHOR_USAGENAME_TAG, comment.getSpeaker().getUsername());
                content = StringUtils.replace(content, GIFT_COMMENT_TAG, comment.getComment());
                for(String anId : recieversIds){
                    Optional<User> userOpt =userRepository.findById(anId);
                    if(userOpt.isEmpty()){
                        continue;
                    }
                    String toSend = StringUtils.replace(content, RECIEVER_NAME_TAG, userOpt.get().usageName());
                    mailService.sendEmail(userOpt.get().getEmail(), DISCUSSION_NOTIFICATION_SUBJECT,toSend);
                }
        }
        catch(IOException ioe){
            log.error("IOException wfile sending mail : {}", ioe.getMessage());
            throw new DefaultException("IOException while discussion notification :"+ioe.getMessage());
        }
    }

    private Set<String> recieversIds(Discussion discussion, DiscussionComment comment ){
        Set<String> result = new HashSet();
        for(DiscussionComment aComment : discussion.getComments()){
            result.add(aComment.getSpeaker().getId());
        }
        //Do not send the mail to the author
        result.remove(comment.getSpeaker().getId());
        return result;
    }
}


