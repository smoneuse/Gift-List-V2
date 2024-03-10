package com.scilab.giftslist.domain.gift;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.tag.model.Tag;
import com.scilab.giftslist.domain.user.model.User;

import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class Gift {
    @Id
    private String id;
    private String title;
    private String comment;
    private int rating;
    private LocalDateTime lastUpdate;
    private List<String> externalLinks;
    @DocumentReference(lazy = true)
    private GiftList owningList;
    @DocumentReference(lazy = true)
    @Indexed
    private User giver;
    @DocumentReference(lazy = true)
    private List<Tag> tags;
    private LocalDateTime giveDate;
    private GiftStatus status;
}
