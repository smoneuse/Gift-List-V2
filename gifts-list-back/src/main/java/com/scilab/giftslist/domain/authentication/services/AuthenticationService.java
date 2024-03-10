package com.scilab.giftslist.domain.authentication.services;

import java.util.Optional;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.authentication.models.AuthCredentials;
import com.scilab.giftslist.domain.authentication.models.AuthToken;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.infra.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthToken authenticate(AuthCredentials creds){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.userOrEmail(), creds.password()));
        //At this point the user is authenticated remember to catch BadCredentials exception
        return userService.findByUserNameOrEmail(creds.userOrEmail()).map(aUser->{
            return new AuthToken(jwtService.generateToken(aUser));
        }).orElseThrow(()->new IllegalStateException("User passed the authentication by wasn't found while generating JWT token : "+creds.userOrEmail()));
    }



}
