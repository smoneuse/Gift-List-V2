package com.scilab.giftslist.api.offering.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class OfferingPageModel {
    public long total;
    public int page;
    public List<OfferingModel> offerings;
}
