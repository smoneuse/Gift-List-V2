package com.scilab.giftslist.domain.photo.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Photo {
    @Id
    private String id;

    private Binary image;
}
