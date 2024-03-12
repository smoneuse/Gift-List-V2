package com.scilab.giftslist.utils.hashing;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GiftListPasswordEncoder implements PasswordEncoder{
    private final HashingService hashingService;

    @Override
    public String encode(CharSequence rawPassword) {
        Hash hash = hashingService.createHash(rawPassword.toString());
        return hash.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        Hash currentPasswordHash= new Hash(encodedPassword);
        return hashingService.validatePassword(rawPassword.toString(), currentPasswordHash);
    }
}
