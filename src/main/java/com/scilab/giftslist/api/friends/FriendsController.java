package com.scilab.giftslist.api.friends;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.GiftListRootController;
import com.scilab.giftslist.api.friends.response.FriendResponse;
import com.scilab.giftslist.api.friends.response.FriendSingleResult;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.services.FriendsService;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.security.AuthenticationFacade;


@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/friends")
public class FriendsController extends GiftListRootController{

    @Autowired
    private FriendsService friendsService;

    @PutMapping("/request/{friendUsername}")
    public ResponseEntity<FriendResponse> sendFriendRequest(@PathVariable("friendUsername") String friendUsername){
        if(StringUtils.isBlank(friendUsername)){
            throw new BadRequestException("Need a friend user name to make a friend request.");
        }
        if(AuthenticationFacade.userName().equalsIgnoreCase(friendUsername)){
            throw new BadRequestException("Cannot request self as friend");
        }
        FriendResponse response =friendsService.sendFriendRequest(AuthenticationFacade.userName(), friendUsername.toLowerCase());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/accept/{requester}")
    public ResponseEntity<FriendResponse> acceptAUserAsFriend(@PathVariable("requester") String requester){
        if(StringUtils.isBlank(requester)){
            throw new BadRequestException("Need a requester user name to accept a friend request.");
        }
        if(AuthenticationFacade.userName().equalsIgnoreCase(requester)){
            throw new BadRequestException("Cannot request self as friend");
        }
        FriendResponse response =friendsService.acceptFriend(AuthenticationFacade.userName(), requester.toLowerCase());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/refuse/{requester}")
    public ResponseEntity<FriendResponse> refuseFriendRequest(@PathVariable("requester") String requester){
        if(StringUtils.isBlank(requester)){
            throw new BadRequestException("Need a requester user name to refuse a friend request.");
        }
        if(AuthenticationFacade.userName().equalsIgnoreCase(requester)){
            throw new BadRequestException("Cannot request self as friend");
        }
        FriendResponse response = friendsService.refuseFriend(AuthenticationFacade.userName(), requester);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove/{friendUsername}")
    public ResponseEntity<FriendResponse> removeFriend(@PathVariable("friendUsername") String friendUsername){
        if(StringUtils.isBlank(friendUsername)){
            throw new BadRequestException("Can't remove a friend without its username.");
        }
        FriendResponse response = friendsService.removeFriend(AuthenticationFacade.userName(), friendUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FriendSingleResult>> allFriends() {
        List<User> userFriends = friendsService.userFriends(AuthenticationFacade.userName());
        List<FriendSingleResult> result = userFriends.stream().map(aFriend->{
                return FriendSingleResult.builder()
                        .friendUsageName(aFriend.usageName())
                        .frienUserName(aFriend.getUsername())
                        .friendLists(aFriend.getGiftLists().size())
                        .friendPictureId(aFriend.getProfilePicture().getId())
                        .build();}
                        ).toList();
        return ResponseEntity.ok(result);
    }
}
