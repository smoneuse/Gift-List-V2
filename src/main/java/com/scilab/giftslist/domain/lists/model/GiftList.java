package com.scilab.giftslist.domain.lists.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.scilab.giftslist.domain.photo.model.Photo;
import com.scilab.giftslist.domain.user.model.User;

import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class GiftList {
    @Id
    private String id;
    @Indexed
    private String name;
    private String description;
    private LocalDateTime lastUpdate;
    @DocumentReference(lazy = true)
    private User owner;
    @DocumentReference(lazy = true)
    private Photo picture;
}
