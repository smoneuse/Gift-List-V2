package com.scilab.giftslist.api.discussion.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class DiscussionModel {
    private String id;
    private long total;
    private List<DiscussionCommentModel> comments;
}
