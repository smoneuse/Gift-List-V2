package com.scilab.giftslist.domain.tag.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class Tag {
    @Id
    private String id;
    @Indexed(unique = true)
    private String title;
}
