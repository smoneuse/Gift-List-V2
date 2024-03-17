package com.scilab.giftslist.domain.lists.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.api.glist.model.GiftListModel;
import com.scilab.giftslist.api.shared.ApiResponse;
import com.scilab.giftslist.domain.gift.repo.GiftRepository;
import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.lists.repo.GiftListRepository;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.errors.NotFoundException;

@Service
public class GiftListService {
    
    @Autowired
    private GiftListRepository giftListRepository;
    @Autowired
    private GiftRepository giftRepository;
    @Autowired
    private UserRepository userRepository;
    
    public Page<GiftList> userLists(String username, PageInputs paging, String sortField){
        ensureUsernameNotBlank(username);        
        User owner= userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()-> new NotFoundException("Cannot create list : user not found : "+username));

        Pageable pageSetting = PageRequest.of(paging.page(), paging.pageSize(), Sort.by(sortField));
        return giftListRepository.findGiftListsByOwner(owner, pageSetting);
    }

    public Page<GiftList> friendLists(String requestingUsername, String friendId, PageInputs paging, String sortField){
        ensureUsernameNotBlank(requestingUsername);
        if(StringUtils.isBlank(friendId)){throw new BadRequestException("Can't request a friend's list without it ID");}
        User requestingUser = userRepository.findUserByUsernameIgnoreCase(requestingUsername).orElseThrow(()->new NotFoundException("Requesting user not found : "+requestingUsername));
        if(!isFriend(requestingUser, friendId)){throw new BadRequestException("Specified user is not a friend.");}
        User friendUser =userRepository.findById(friendId).orElseThrow(()->new NotFoundException("Friend user ID not found : "+friendId));
        Pageable pageSetting = PageRequest.of(paging.page(), paging.pageSize(), Sort.by(sortField));
        return giftListRepository.findGiftListsByOwner(friendUser, pageSetting);
    }

    private boolean isFriend(User aUser, String friendId){
        return aUser.getFriends().stream().anyMatch(aFriend-> aFriend.getId().equals(friendId));
    }

    public GiftList createList(String username, GiftListModel model){
        ensureUsernameNotBlank(username);
        User owner= userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()-> new NotFoundException("Cannot create list : user not found : "+username));
        if(giftListRepository.findGiftListByNameIsAndOwnerIs(model.getTitle(), owner).isPresent()){
            throw new BadRequestException("A list with the same name already exists for the user");
        }
        GiftList newList = GiftList.builder()
            .name(model.getTitle())
            .description(model.getDescription())
            .lastUpdate(LocalDateTime.now())
            .owner(owner)
            .build();
        GiftList createdList =giftListRepository.insert(newList);
        owner.getGiftLists().add(createdList);
        userRepository.save(owner);
        return createdList;
    }

    public GiftList updateList(String userName, GiftListModel model){
        ensureUsernameNotBlank(userName);
        User owner= userRepository.findUserByUsernameIgnoreCase(userName).orElseThrow(()-> new NotFoundException("Cannot update list : user not found : "+userName));
        if( giftListRepository.findGiftListByNameIsAndOwnerIsAndIdIsNot(model.getTitle(), owner, model.getId()).isPresent()){
            throw new BadRequestException("Can't proceed : Another list exists with the same title");
        }
        GiftList toUpdate = giftListRepository.findGiftListByIdAndOwnerIs(model.getId(), owner).orElseThrow(()-> new BadRequestException("Can't proceed : You're not the owner of this list"));
        toUpdate.setDescription(model.getDescription());
        toUpdate.setName(model.getTitle());
        toUpdate.setLastUpdate(LocalDateTime.now());
        return giftListRepository.save(toUpdate);
    }

    public ApiResponse deleteGiftList(String username, String giftListId){
        ensureUsernameNotBlank(username);        
        Optional<GiftList> giftListOpt= giftListRepository.findById(giftListId);
        if(giftListOpt.isEmpty()){
            //The list has already been deleted. OK.
            return new ApiResponse(true, StringUtils.EMPTY);
        }
        GiftList listToDelete = giftListOpt.get();
        if(listToDelete.getOwner().getUsername().equalsIgnoreCase(username)){
            User listOwner = userRepository.findById(listToDelete.getOwner().getId()).orElseThrow(()-> new NotFoundException("Can't find the user ownling the list to delete : "+username));
            listOwner.getGiftLists().removeIf(aList-> aList.getId().equals(listToDelete.getId()));
            userRepository.save(listOwner);
            giftListRepository.delete(listToDelete);
            //Now delete associated Gifts
            giftRepository.deleteByOwningList(listToDelete);
            return new ApiResponse(true, StringUtils.EMPTY);
        }
        return new ApiResponse(false,"Can't delete a list the user don't own");
    }

    private void ensureUsernameNotBlank(String username){
        if(StringUtils.isBlank(username)){
            throw new BadRequestException("The username cannot be blank");
        }
    }
}
