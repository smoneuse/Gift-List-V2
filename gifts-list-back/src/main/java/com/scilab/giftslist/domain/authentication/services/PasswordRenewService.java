package com.scilab.giftslist.domain.authentication.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.authentication.models.PasswordRenew;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.domain.user.services.UserProfileService;
import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.errors.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRenewService {
    private static final int RANDOM_PASSWORD_RESET_CODE_LENGTH=6;
    
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;

    public Optional<String> startPasswordReset(String userOrEmail){
        log.info("Password reset was asked for user {}", userOrEmail);
        return userService.findByUserNameOrEmail(userOrEmail).map(aUser->{
            aUser.setRenewPasswordCode(generateRandomAlphanumericString(RANDOM_PASSWORD_RESET_CODE_LENGTH));
            userRepository.save(aUser);
            log.debug("Random password reset code generated and user updated in database");
            return aUser.getEmail();
        });
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
