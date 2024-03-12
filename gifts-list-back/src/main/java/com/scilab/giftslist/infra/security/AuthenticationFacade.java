package com.scilab.giftslist.infra.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthenticationFacade {
 
    public static String userName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

