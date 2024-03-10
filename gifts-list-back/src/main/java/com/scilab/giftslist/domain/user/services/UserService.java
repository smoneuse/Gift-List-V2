package com.scilab.giftslist.domain.user.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.utils.hashing.GiftListPasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GiftListPasswordEncoder passwordEncoder;

    public User registerUser(User aUser, String password){

        userRepository.findUserByUsernameIgnoreCase(aUser.getUsername()).ifPresent(existingUser->{
            throw new IllegalArgumentException("A user already exists with this username : "+existingUser.getUsername());
        });
        userRepository.findUserByEmailIgnoreCase(aUser.getEmail()).ifPresent(existingUser->{
            throw new IllegalArgumentException("A user already exists with this email : "+existingUser.getEmail());
        });        
        aUser.setPwdHash(passwordEncoder.encode(password));        
        return userRepository.insert(aUser);
    }

    public Optional<User> findByUserNameOrEmail(String usernameOrEmail){
        if(usernameOrEmail.isBlank()){return Optional.empty();}
        Optional<User> userOpt = userRepository.findUserByUsernameIgnoreCase(usernameOrEmail);
        if(userOpt.isPresent()){return userOpt;}
        return userRepository.findUserByEmailIgnoreCase(usernameOrEmail);
    }
}
