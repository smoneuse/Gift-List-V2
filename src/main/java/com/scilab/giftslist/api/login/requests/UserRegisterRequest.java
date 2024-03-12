package com.scilab.giftslist.api.login.requests;



import org.apache.commons.lang3.StringUtils;

import com.scilab.giftslist.domain.user.model.AccountStatus;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.model.UserProfile;
import com.scilab.giftslist.utils.controls.InputControlsUtils;

import lombok.Getter;

@Getter
public class UserRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    

    public void checkInput(){
        if(StringUtils.isAnyBlank(username, password, email)){            
            throw new IllegalArgumentException("Missing required data. Username, password and email requiered for registering a user");
        }
        if(!InputControlsUtils.checkNoSpace(username)){
            throw new IllegalArgumentException("The username should not contain space characters");
        }
        if(!InputControlsUtils.isAValidEmail(email)){
            throw new IllegalArgumentException("This is not a valid email :"+email);
        }
    }

    public User toUser(){
        return User.builder()
        .username(username.toLowerCase())
        .email(email.toLowerCase())
        .firstname(firstname)
        .lastname(lastname)
        .status(AccountStatus.ACTIVE)
        .profile(UserProfile.USER)
        .build();
    }
}
