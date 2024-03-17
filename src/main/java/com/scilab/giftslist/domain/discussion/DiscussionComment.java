package com.scilab.giftslist.domain.discussion;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.scilab.giftslist.domain.user.model.User;

import lombok.Builder;
import lombok.Data;

@Document
@Data @Builder
public class DiscussionComment {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    private User speaker;
    private LocalDateTime date;
    private String comment;
}
