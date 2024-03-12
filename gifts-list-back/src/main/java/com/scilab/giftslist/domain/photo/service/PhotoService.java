package com.scilab.giftslist.domain.photo.service;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scilab.giftslist.domain.photo.PhotoRepository;
import com.scilab.giftslist.domain.photo.model.Photo;
import com.scilab.giftslist.infra.errors.DefaultException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;


    public Photo addPhotoFromFile(MultipartFile file){
        Photo photo = new Photo();
        try{
            photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            return photoRepository.insert(photo);
        }
        catch(IOException ioe){
            log.error("IOException while storing photo to database");
            throw new DefaultException("IOException while storing photo to database :"+ioe.getMessage());
        }
    }
}
