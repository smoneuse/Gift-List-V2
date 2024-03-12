package com.scilab.giftslist.domain.authentication.models;

import org.apache.commons.lang3.StringUtils;

public record AuthCredentials(String userOrEmail, String password) {

    public AuthCredentials(String userOrEmail, String password){
        if(StringUtils.isAnyBlank(userOrEmail, password)){
            throw new IllegalArgumentException("Credentials can't be empty");
        }
        this.userOrEmail=userOrEmail;
        this.password=password;
    }

}
