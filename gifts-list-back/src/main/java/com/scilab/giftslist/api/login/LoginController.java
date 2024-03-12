package com.scilab.giftslist.api.login;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.ApiOperationStatus;
import com.scilab.giftslist.api.GiftListRootController;
import com.scilab.giftslist.api.login.requests.AuthenticationRequest;
import com.scilab.giftslist.api.login.requests.UserRegisterRequest;
import com.scilab.giftslist.api.login.responses.AskForPasswordResetResponse;
import com.scilab.giftslist.api.login.responses.AuthenticationResponse;
import com.scilab.giftslist.api.login.responses.UserRegisterResponse;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.domain.authentication.models.AuthCredentials;
import com.scilab.giftslist.domain.authentication.models.AuthToken;
import com.scilab.giftslist.domain.authentication.models.PasswordRenew;
import com.scilab.giftslist.domain.authentication.services.AuthenticationService;
import com.scilab.giftslist.domain.authentication.services.PasswordRenewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController extends GiftListRootController{

    
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final PasswordRenewService passwordRenewService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterRequest user){
        try{
            user.checkInput();
            User createdUser = userService.registerUser(user.toUser(), user.getPassword());
            return ResponseEntity.ok().body(UserRegisterResponse.builder()
                .status(ApiOperationStatus.OK)
                .username(createdUser.getUsername())
                .build()
                );
        }
        catch(IllegalArgumentException ia){
            log.warn("Illegal argument exception while registering new user : "+ia.getMessage());
            return ResponseEntity.badRequest().body(UserRegisterResponse.builder()
                .apiMessage(ia.getMessage())
                .status(ApiOperationStatus.FAILURE)
                .build());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest){
        try{
            AuthToken token = authenticationService.authenticate(new AuthCredentials(authRequest.getUserOrEmail(), authRequest.getPassword()));
            return ResponseEntity.ok().body(AuthenticationResponse.builder()
                .success(true)
                .token(token.token())
                .build());
        }
        catch(BadCredentialsException bce){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch(IllegalArgumentException iae){
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder()
                .success(false)
                .message(iae.getMessage())
                .build()
            );
        }
        catch(IllegalStateException ise){
            return ResponseEntity.internalServerError().body(AuthenticationResponse.builder()
            .success(false)
            .message(ise.getMessage())
            .build()
        );
        }
    }
    
    @PutMapping("/reset")
    public ResponseEntity<AskForPasswordResetResponse> startResetPassword(@RequestParam("userOrEmail") String userOrEmail){
        return passwordRenewService.startPasswordReset(userOrEmail).map(userEmail -> 
            ResponseEntity.ok().body(AskForPasswordResetResponse.builder().email(userEmail).build())
        ).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reset")
    public ResponseEntity<String> confirmPasswordReset(@RequestBody PasswordRenew passwordRenew){
        if(StringUtils.isAnyBlank(passwordRenew.usernameOrEmail(), passwordRenew.renewCode(), passwordRenew.newPassord())){
            throw new BadRequestException("Can't renew password : missing parameters");
        }
        passwordRenewService.confirmPasswordUpdate(passwordRenew);
        return ResponseEntity.ok().body("Ok password renewed");
    }
}
