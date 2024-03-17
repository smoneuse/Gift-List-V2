package com.scilab.giftslist.api.gift.models;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.scilab.giftslist.domain.gift.Gift;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class GiftModel {
    private String id;
    private String title;
    private String comment;
    private int rating;
    private LocalDateTime lastUpdate;
    private List<String> tags;
    private List<String> externalList;
    private String status;    
    private String discussionId;
    private LocalDateTime giveDate;

    public static GiftModel fromGift(Gift from) {
        return GiftModel.builder()
            .id(from.getId())
            .title(from.getTitle())
            .comment(from.getComment())
            .rating(from.getRating())
            .lastUpdate(from.getLastUpdate())
            .tags(from.getTags().stream().map(aTag->aTag.getTitle()).toList())
            .externalList(from.getExternalLinks())
            .status(from.getStatus().name())
            .giveDate(from.getGiveDate())
            .discussionId(from.getDiscussion() == null ? StringUtils.EMPTY : from.getDiscussion().getId())
            .build();
    }
}
