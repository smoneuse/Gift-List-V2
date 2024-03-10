package com.scilab.giftslist.domain.user.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.utils.hashing.GiftListPasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final GiftListPasswordEncoder passwordEncoder;

    public User updateUserPassword(User aUser, String newPassword){
        log.debug("Updating user {} password", aUser.getUsername());
        aUser.setPwdHash(passwordEncoder.encode(newPassword));
        aUser.setRenewPasswordCode(StringUtils.EMPTY);
        return userRepository.save(aUser);
    }
}
