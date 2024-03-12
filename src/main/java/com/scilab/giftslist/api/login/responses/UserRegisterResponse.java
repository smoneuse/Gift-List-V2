package com.scilab.giftslist.api.login.responses;

import com.scilab.giftslist.api.ApiOperationStatus;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserRegisterResponse {
    private ApiOperationStatus status;
    private String apiMessage;
    private String username;
}
