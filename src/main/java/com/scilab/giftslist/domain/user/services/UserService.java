package com.scilab.giftslist.domain.user.services;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.api.user.model.UserModel;
import com.scilab.giftslist.domain.photo.model.Photo;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.errors.NotFoundException;
import com.scilab.giftslist.infra.security.AuthenticationFacade;
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

    public Optional<User> findByUsername(String username){
        if(username.isBlank()){return Optional.empty();}
        return userRepository.findUserByUsernameIgnoreCase(username);
    }

    public User updateUserPicture(String usernameOrEmail, Photo photo){
        User aUser = findByUserNameOrEmail(usernameOrEmail).orElseThrow(()-> new NotFoundException("The user "+usernameOrEmail+" was not found"));
        aUser.setProfilePicture(photo);
        return userRepository.save(aUser);
    }

    public Page<User> searchByUserName(String filter, int page, int pageSize){
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("username"));
        if(StringUtils.isBlank(filter)){                        
            return userRepository.findAll(pageRequest);
        }
        return userRepository.findUsersByUsernameContaining(filter, pageRequest);
    }

    public User updateUser(String username, UserModel updatedProfileData){
        User currentUser =userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()->new NotFoundException("User not found : "+AuthenticationFacade.userName()));
        if(!username.equals(updatedProfileData.getUsername())){
            userRepository.findUserByUsernameIgnoreCase(updatedProfileData.getUsername()).ifPresent(existingUser->{
                throw new BadRequestException("A user already exists with this username : "+existingUser.getUsername());
            });
        }
        if(!currentUser.getEmail().equals(updatedProfileData.getEmail())){
            userRepository.findUserByEmailIgnoreCase(updatedProfileData.getEmail()).ifPresent(existingUser->{
                throw new BadRequestException("A user already exists with this email : "+existingUser.getEmail());
            }); 
        }    
        currentUser.setUsername(updatedProfileData.getUsername().toLowerCase());
        currentUser.setEmail(updatedProfileData.getEmail().toLowerCase());
        currentUser.setFirstname(updatedProfileData.getFirstname());
        currentUser.setLastname(updatedProfileData.getLastname());
        return userRepository.save(currentUser);
    }
}
