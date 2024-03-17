package com.scilab.giftslist.domain.gift.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.api.gift.models.GiftModel;
import com.scilab.giftslist.domain.gift.Gift;
import com.scilab.giftslist.domain.gift.GiftStatus;
import com.scilab.giftslist.domain.gift.repo.GiftRepository;
import com.scilab.giftslist.domain.tag.repo.TagRepository;
import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.lists.repo.GiftListRepository;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.errors.NotFoundException;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GiftListRepository giftListRepository;

    @Autowired
    private TagRepository tagRepository;

    public long countGiftsInList(GiftList giftList, GiftStatus... status){
        return giftRepository.countGiftsByOwningListAndStatusIn(giftList, status);
    }

    public long countUserReservedGiftsInList(GiftList giftList, String giverUsername){
        User giver = userRepository.findUserByUsernameIgnoreCase(giverUsername).orElseThrow(()->new NotFoundException("The giver user name is not found :"+giverUsername));
        return giftRepository.countGiftsByOwningListAndGiverIsAndStatusIn(giftList, giver, GiftStatus.RESERVED);
    }

    public Page<Gift> findUserGiftsInListWithStatus(String username, String listId, PageInputs paging, String sort, String sortDirection,GiftStatus... status){
        User owner =checkAndProvideUser(username);        
        GiftList giftList = checkAndProvideListByOwnerAndId(owner, listId);
        Pageable pageSettings = PageRequest.of(paging.page(), paging.pageSize(), Sort.by(Direction.fromString(sortDirection), sort));
        return giftRepository.findGiftsByOwningListAndStatusIn(giftList, List.of(status), pageSettings);
    }

    public Page<Gift> findFriendGiftsInList(String requestingUsername, String listId, PageInputs paging, String sort, String sortDirection){
        User requester = checkAndProvideUser(requestingUsername);
        GiftList requestedList = checkAndProvideListById(listId);
        if(!requester.getFriends().stream().anyMatch(aFriend-> aFriend.getId().equals(requestedList.getOwner().getId()))){
            throw new BadRequestException("The list owner is not registered as Friend");
        }
        Pageable pageSettings = PageRequest.of(paging.page(), paging.pageSize(), Sort.by(Direction.fromString(sortDirection), sort));
        return giftRepository.findGiftsByOwningListAndGiverIsOrStatusIs(requestedList,requester, GiftStatus.AVAILABLE, pageSettings);
    }

    public Gift addGiftToList(String username, String listId, GiftModel model){
        User owner = checkAndProvideUser(username);
        GiftList giftList = checkAndProvideListByOwnerAndId(owner, listId);
        if(giftRepository.findGiftByOwningListAndTitleIs(giftList, model.getTitle()).isPresent()){
            throw new BadRequestException("Can't add gift : Another gift with same title already exists in this list");
        }

        Gift giftToAdd =  Gift.builder()
            .title(model.getTitle())
            .comment(model.getComment())
            .rating(model.getRating())
            .owningList(giftList)
            .lastUpdate(LocalDateTime.now())
            .externalLinks(model.getExternalList())
            .status(GiftStatus.AVAILABLE)
            .tags(model.getTags())
            .build();
        //Add gift to the repository
        return giftRepository.insert(giftToAdd);        
    }

    public void removeGiftFromList(String username, String listId, GiftModel model){
        User owner = checkAndProvideUser(username);
        GiftList giftList = checkAndProvideListByOwnerAndId(owner, listId);
        Gift toRemove = giftRepository.findById(model.getId()).orElseThrow(()-> new NotFoundException("No gift with ID "+model.getId()));
        //Check the owning list
        if(! toRemove.getOwningList().getId().equals(giftList.getId())){
            throw new BadRequestException("The git with ID "+model.getId()+" doesn't belong to the list with ID :"+listId);
        }        
        giftRepository.delete(toRemove);
    }

    public Gift updateGift(String username, String listId, GiftModel model){
        User owner = checkAndProvideUser(username);
        GiftList giftList = checkAndProvideListByOwnerAndId(owner, listId);
        Gift toUpdate = giftRepository.findById(model.getId()).orElseThrow(()-> new NotFoundException("No gift with ID "+model.getId()));
        giftRepository.findGiftByOwningListAndTitleIs(giftList, model.getTitle()).ifPresent(aGift->{
            if(!aGift.getId().equals(model.getId())){
                throw new BadRequestException("Can't update gift : Another gift with same title exists");
            }
        });
        toUpdate.setTitle(model.getTitle());
        toUpdate.setComment(model.getComment());
        toUpdate.setRating(model.getRating());
        toUpdate.setExternalLinks(model.getExternalList());
        toUpdate.setTags(model.getTags());
        toUpdate.setLastUpdate(LocalDateTime.now());
        Gift updatedGift =giftRepository.save(toUpdate);
        //Updating the list last update date
        giftList.setLastUpdate(LocalDateTime.now());
        giftListRepository.save(giftList);
        
        return updatedGift;
    }

    private User checkAndProvideUser(String username){
        return userRepository.findUserByUsernameIgnoreCase(username).orElseThrow(()->new NotFoundException("No user found for identifier :"+username));
    }

    private GiftList checkAndProvideListByOwnerAndId(User owner, String listId){
        return giftListRepository.findGiftListByIdAndOwnerIs(listId, owner).orElseThrow(()-> new NotFoundException("No list found for Id "+listId+" and owner "+owner.getUsername()));
    }

    private GiftList checkAndProvideListById(String listId){
        return giftListRepository.findById(listId).orElseThrow(()->new NotFoundException("No list found with the ID"+ listId));
    }
}
