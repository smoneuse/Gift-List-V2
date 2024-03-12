package com.scilab.giftslist.api.picture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.domain.photo.model.Photo;
import com.scilab.giftslist.domain.photo.service.PhotoService;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.services.UserService;
import com.scilab.giftslist.infra.errors.DefaultException;
import com.scilab.giftslist.infra.errors.NotFoundException;
import com.scilab.giftslist.infra.security.AuthenticationFacade;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/picture")
@Slf4j
public class PictureController {
        private static final String DEFAULT_PROFILE_PICTURE_FILE="assets/blank-profile-picture.png";    
    
    @Value(DEFAULT_PROFILE_PICTURE_FILE)
    private File defaultUserPicture;
    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;
    
    @PostMapping()    
    public ResponseEntity<String> updloadProfilePicture(@RequestParam("image") MultipartFile image){               
        Photo photo= photoService.addPhotoFromFile(image);
        userService.updateUserPicture(AuthenticationFacade.userName(), photo);
        return ResponseEntity.ok(ApiConstants.API_RESPONSE_OK);
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<String> getUserPictureB64(@RequestParam(name="userName") String userName){        
        User aUser = userService.findByUserNameOrEmail(userName).orElseThrow(()->new NotFoundException("User not found :"+userName));
        if(StringUtils.isNotBlank(aUser.getProfilePicture().getId())){
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(aUser.getProfilePicture().getImage().getData()));
        }
        try{
            byte[] pictureBytes =Files.readAllBytes(defaultUserPicture.toPath());
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(pictureBytes));
        }
        catch(IOException ioe){
            log.error("Error while reading default picture file : "+ioe.getMessage());
            throw new DefaultException("Can't read user default picture file :"+ioe.getMessage());
        }
    }
}
