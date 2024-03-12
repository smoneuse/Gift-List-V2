package com.scilab.giftslist.api.login.requests;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String userOrEmail;
    private String password;
}
