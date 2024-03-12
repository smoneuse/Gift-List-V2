package com.scilab.giftslist.domain.authentication.services;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.authentication.models.PasswordRenew;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.domain.user.services.UserProfileService;
import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.errors.DefaultException;
import com.scilab.giftslist.infra.errors.NotFoundException;
import com.scilab.giftslist.infra.mail.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRenewService {
    private static final int RANDOM_PASSWORD_RESET_CODE_LENGTH=6;
    private static final String PASSWORD_RENEW_MAIL_SUBJECT="Gift - list : Votre code de renouvellement de mot de passe";
    private static final String RENEW_MAIL_TEMPLATE_FILE="templates/PasswordRenewTemplate.html";
    public static final String TEMPLATE_USER_TAG="{{USER_NAME}}";
    public static final String TEMPLATE_CODE_TAG="{{RENEW_CODE}}";
    
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final MailService mailService;

    @Value(RENEW_MAIL_TEMPLATE_FILE)
    private File mailRenewTemplateFile;

    public Optional<String> startPasswordReset(String userOrEmail){
        log.info("Password reset was asked for user {}", userOrEmail);
        return userService.findByUserNameOrEmail(userOrEmail).map(aUser->{
            aUser.setRenewPasswordCode(generateRandomAlphanumericString(RANDOM_PASSWORD_RESET_CODE_LENGTH));
            userRepository.save(aUser);
            sendCodeViaEmail(aUser);
            log.debug("Random password reset code generated and user updated in database");
            return aUser.getEmail();
        });
    }

    private void sendCodeViaEmail(User aUser){
        if(!this.mailRenewTemplateFile.exists()){
            throw new DefaultException("Template file not found for password renewal mail");
        }        
        try{
            String content = Files.readString(mailRenewTemplateFile.toPath());
            content=StringUtils.replace(content, TEMPLATE_USER_TAG, aUser.usageName());
            content=StringUtils.replace(content, TEMPLATE_CODE_TAG, aUser.getRenewPasswordCode());
            mailService.sendEmail(aUser.getEmail(), PASSWORD_RENEW_MAIL_SUBJECT, content);
        }
        catch(IOException ioe){
            log.error("IOException while reading template file :"+ioe.getMessage());
            throw new DefaultException("Can't read password renew template file : "+ioe.getMessage());
        }

    }

    public void confirmPasswordUpdate(PasswordRenew passwordRenew){
        User theUser =  userService.findByUserNameOrEmail(passwordRenew.usernameOrEmail()).orElseThrow(() -> new NotFoundException("User not found : "+passwordRenew.usernameOrEmail()));
        if(!theUser.getRenewPasswordCode().equals(passwordRenew.renewCode())){
            throw new BadRequestException("The password renew code does not match with the expected one.");
        }
        userProfileService.updateUserPassword(theUser, passwordRenew.newPassord());
    }

    private String generateRandomAlphanumericString(int strLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
    
        return random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(strLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
    }
}
