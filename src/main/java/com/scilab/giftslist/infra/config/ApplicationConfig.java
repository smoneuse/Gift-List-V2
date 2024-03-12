package com.scilab.giftslist.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.utils.hashing.GiftListPasswordEncoder;
import com.scilab.giftslist.utils.hashing.HashingService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor

public class ApplicationConfig {

    private final HashingService hashingService;
    private final UserService userService;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider =new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userService.findByUserNameOrEmail(username)
                .orElseThrow(() ->new UsernameNotFoundException("Can' find username : "+username));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new GiftListPasswordEncoder(hashingService);
    }
}
