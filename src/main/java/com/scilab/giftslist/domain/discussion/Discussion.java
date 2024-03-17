package com.scilab.giftslist.domain.discussion;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.scilab.giftslist.domain.gift.Gift;

import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class Discussion {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    @Indexed(unique = true)
    private Gift discussedGift;
    @DocumentReference(lazy = true)
    private List<DiscussionComment> comments;
}
