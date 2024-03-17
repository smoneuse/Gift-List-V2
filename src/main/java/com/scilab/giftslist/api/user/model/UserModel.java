package com.scilab.giftslist.api.user.model;

import org.apache.commons.lang3.StringUtils;

import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.utils.controls.InputControlsUtils;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserModel {
    private String id;
    private String username;
    private String usagename;
    private String firstname;
    private String lastname;
    private String email;
    private long listCount;
    private String pictureId;

    public static UserModel fromUser(User user){
        return UserModel.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .pictureId(user.getProfilePicture()==null ? null : user.getProfilePicture().getId())
            .build();
    }

    public void checkInput(){
        if(StringUtils.isAnyBlank(username, email)){            
            throw new IllegalArgumentException("Missing required data. Username and email requiered for a user");
        }
        if(!InputControlsUtils.checkNoSpace(username)){
            throw new IllegalArgumentException("The username should not contain space characters");
        }
        if(!InputControlsUtils.isAValidEmail(email)){
            throw new IllegalArgumentException("This is not a valid email :"+email);
        }
    }
}
