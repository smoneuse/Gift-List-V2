package com.scilab.giftslist.domain.authentication.models;

import org.apache.commons.lang3.StringUtils;

public record AuthToken(String token) {
    
    public AuthToken(String token){
        if(StringUtils.isBlank(token)){
            throw new IllegalArgumentException("Token can't be empty");
        }
        this.token=token;
    }
}
