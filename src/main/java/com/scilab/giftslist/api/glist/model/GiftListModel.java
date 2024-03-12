package com.scilab.giftslist.api.glist.model;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class GiftListModel {
    private String id;
    private String title;
    private String description;
    private String pictureId;
    private long availableGifts;
    private long reservedGifts;
}
