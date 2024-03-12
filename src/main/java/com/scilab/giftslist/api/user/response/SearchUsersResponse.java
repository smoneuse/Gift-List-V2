package com.scilab.giftslist.api.user.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class SearchUsersResponse {
    private long total;
    private List<UserSearchResult> users;
}
