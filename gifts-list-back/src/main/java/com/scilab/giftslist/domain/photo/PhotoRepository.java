package com.scilab.giftslist.domain.photo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.photo.model.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String>{

}
