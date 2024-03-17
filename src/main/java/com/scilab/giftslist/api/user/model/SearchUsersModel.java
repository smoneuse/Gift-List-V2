package com.scilab.giftslist.api.user.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class SearchUsersModel {
    private long total;
    private List<UserModel> users;
}
