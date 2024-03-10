package com.scilab.giftslist.api.login.responses;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AskForPasswordResetResponse {
    private String email;    
}
