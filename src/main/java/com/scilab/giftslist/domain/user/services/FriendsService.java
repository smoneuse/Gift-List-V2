package com.scilab.giftslist.domain.user.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.api.friends.response.FriendResponse;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.NotFoundException;

@Service
public class FriendsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsNotificationService friendsNotificationService;


    public FriendResponse sendFriendRequest(String requester, String friend){
        FriendsActor actors = actors(requester, friend);
        
        if(actors.alreadyInfriendList()){
            return new FriendResponse(false, "This friend is already in your friend list");
        }

        if(!actors.alreadyRequested()){
            actors.reciever().getFriendsRequest().add(actors.sender());
            userRepository.save(actors.reciever());
            friendsNotificationService.notifyFriendRequest(actors);
        }
        return new FriendResponse(true, StringUtils.EMPTY);
    }


    public FriendResponse acceptFriend(String acceptingUser, String aRequester){
        FriendsActor actors = actors(aRequester, acceptingUser);
        if(!actors.alreadyRequested()){
            return new FriendResponse(false, "Can't accept a non requested friendship");
        }
        actors.reciever().getFriendsRequest().removeIf(aSender-> aSender.getId().equals(actors.sender().getId()));
        if(!actors.alreadyInfriendList()){
            actors.sender().getFriends().add(actors.reciever());
            friendsNotificationService.notifyRequestAccepted(actors);
        }
        userRepository.save(actors.reciever());
        userRepository.save(actors.sender());
        return new FriendResponse(true, StringUtils.EMPTY);
    }


    public FriendResponse refuseFriend(String aUsername, String aRequester){
        FriendsActor actors = actors(aRequester, aUsername);
        actors.reciever().getFriendsRequest().removeIf(aSender-> aSender.getId().equals(actors.sender().getId()));
        userRepository.save(actors.reciever());
        friendsNotificationService.notifyRequestRefused(actors);
        return new FriendResponse(true, StringUtils.EMPTY);
    }

    public FriendResponse removeFriend(String username, String friendUsername){
        User user = userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(()-> new NotFoundException("Remove friend - The user has not been found : "+username));
        user.getFriends().removeIf(aFriend->aFriend.getUsername().equalsIgnoreCase(friendUsername));
        userRepository.save(user);
        return new FriendResponse(true, StringUtils.EMPTY);
    }
    
    private FriendsActor actors(String requester, String friend){
        User userRequester = userRepository.findUserByUsernameIgnoreCase(requester)
                .orElseThrow(()-> new NotFoundException("The requester has not been found : "+requester));
        User userFriend =userRepository.findUserByUsernameIgnoreCase(friend)
                .orElseThrow(()-> new NotFoundException("The friend to request has not been found : "+friend));
        return new FriendsActor(userRequester, userFriend);    
    }

    public List<User> userFriends(String username){
        User aUser =userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()-> new NotFoundException("Can't get user's friends : user not found :"+username));
        List<User> result = aUser.getFriends();
        result.sort((userA,userB)-> userA.getUsername().compareTo(userB.getUsername()));
        return result;
    }

}
