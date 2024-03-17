package com.scilab.giftslist.api.gift.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class GiftPage {
    private long total;
    private List<GiftModel> gifts;
}
