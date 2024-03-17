package com.scilab.giftslist.domain.offering.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.scilab.giftslist.domain.discussion.Discussion;
import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.user.model.User;

import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class Offering {
    @Id
    private String id;
    @Indexed @DocumentReference(lazy = true)
    private User reciever;

    @Indexed @DocumentReference(lazy = true)
    private User giver;

    @Indexed
    private String listTitle;
    @Indexed @DocumentReference(lazy = true)
    private GiftList owningList;

    private LocalDateTime givenDate;
    private List<String> externalLinks;
    @DocumentReference(lazy = true)
    private Discussion discussion;
    @Indexed
    private String title;
    private int rating;
    private String comment;
}
