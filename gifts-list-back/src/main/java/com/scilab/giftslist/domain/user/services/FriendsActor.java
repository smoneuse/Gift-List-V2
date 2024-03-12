package com.scilab.giftslist.domain.user.services;

import com.scilab.giftslist.domain.user.model.User;

public record FriendsActor(User sender, User reciever){
        
    public boolean alreadyInfriendList(){
        return sender.getFriends().stream().anyMatch(aFriend->aFriend.getId().equals(reciever.getId()));            
    }

    public boolean alreadyRequested(){
        return reciever.getFriendsRequest().stream().anyMatch(aSender-> aSender.getId().equals(sender.getId()));
    }
}
