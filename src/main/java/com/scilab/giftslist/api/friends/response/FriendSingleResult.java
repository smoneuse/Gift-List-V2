package com.scilab.giftslist.api.friends.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class FriendSingleResult {
    private String friendUsageName;
    private String frienUserName;
    private String friendPictureId;
    private int friendLists;
}
