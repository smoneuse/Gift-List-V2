package com.scilab.giftslist.api.friends.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class FriendListResult {
    private long total;
    private List<FriendSingleResult> friends;
}
