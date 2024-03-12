package com.scilab.giftslist.api.user.shared;

import org.apache.commons.lang3.StringUtils;

import com.scilab.giftslist.utils.controls.InputControlsUtils;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserProfileData {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String pictureId;

    public void checkInput(){
        if(StringUtils.isAnyBlank(username, email)){            
            throw new IllegalArgumentException("Missing required data. Username and email requiered for updating a user");
        }
        if(!InputControlsUtils.checkNoSpace(username)){
            throw new IllegalArgumentException("The username should not contain space characters");
        }
        if(!InputControlsUtils.isAValidEmail(email)){
            throw new IllegalArgumentException("This is not a valid email :"+email);
        }
    }
}
