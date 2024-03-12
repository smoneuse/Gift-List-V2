package com.scilab.giftslist.api.glist.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class GiftListPage {
    private long total;
    private List<GiftListModel> giftLists;
}
