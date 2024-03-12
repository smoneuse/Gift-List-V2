package com.scilab.giftslist.api.user.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserSearchResult {
    private String username;
    private long listCount;
    private String pictureId;
}
