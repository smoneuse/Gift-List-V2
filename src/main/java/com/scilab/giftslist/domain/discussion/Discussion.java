package com.scilab.giftslist.domain.discussion;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class Discussion {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    private List<DiscussionComment> comments;
}
