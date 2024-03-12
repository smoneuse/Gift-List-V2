package com.scilab.giftslist.api.login.responses;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AuthenticationResponse {
    private boolean success;
    private String message;
    private String token;
}
