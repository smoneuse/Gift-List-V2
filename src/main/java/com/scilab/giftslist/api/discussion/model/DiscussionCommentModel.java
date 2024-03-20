package com.scilab.giftslist.api.discussion.model;

import java.time.LocalDateTime;

import com.scilab.giftslist.api.user.model.UserModel;
import com.scilab.giftslist.domain.discussion.DiscussionComment;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DiscussionCommentModel {
    private String comment;
    private LocalDateTime date;
    private UserModel author;

    public static DiscussionCommentModel fromDiscussionComment(DiscussionComment comment){
        return DiscussionCommentModel.builder()
            .author(UserModel.fromUser(comment.getSpeaker()))
            .date(comment.getDate())
            .comment(comment.getComment())
            .build();
    }
}
