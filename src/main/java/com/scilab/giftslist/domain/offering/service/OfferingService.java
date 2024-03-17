package com.scilab.giftslist.domain.offering.service;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.api.PageInputs;
import com.scilab.giftslist.domain.gift.Gift;
import com.scilab.giftslist.domain.gift.repo.GiftRepository;
import com.scilab.giftslist.domain.offering.model.Offering;
import com.scilab.giftslist.domain.offering.repo.OfferingRepository;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.NotFoundException;

@Service
public class OfferingService {

    @Autowired
    private OfferingRepository offeringRepository;

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private UserRepository userRepository;


    public Offering offerGift(Gift giftToOffer){
        User reciever = giftToOffer.getOwningList().getOwner();
        User giver = giftToOffer.getGiver();

        Offering offering = Offering.builder()
            .reciever(reciever)
            .giver(giver)            
            .givenDate(LocalDateTime.now())
            .title(giftToOffer.getTitle())
            .comment(giftToOffer.getComment())
            .rating(giftToOffer.getRating())
            .owningList(giftToOffer.getOwningList())
            .listTitle(giftToOffer.getOwningList().getName())
            .externalLinks(giftToOffer.getExternalLinks())
            .discussion(giftToOffer.getDiscussion())
            .build();
        giftRepository.delete(giftToOffer);
        return offeringRepository.insert(offering);
    }

   public Page<Offering> allOfferedByTo(String giverName, String recieverName, String giftTitleFilter, PageInputs pageSettings, String sortBy, String sortDirection){
        User giver = userRepository.findUserByUsernameIgnoreCase(giverName).orElseThrow(()->new NotFoundException("No user with username :"+giverName));
        User reciever = userRepository.findUserByUsernameIgnoreCase(recieverName).orElseThrow(()->new NotFoundException("No user with username :"+recieverName));

        Pageable paging = PageRequest.of(pageSettings.page(), pageSettings.pageSize(), Sort.by(Direction.fromString(sortDirection), sortBy));
        if(StringUtils.isBlank(giftTitleFilter)){
            return offeringRepository.findOfferingsByGiverIsAndRecieverIs(giver, reciever, paging);
        }
        return offeringRepository.findOfferingsByGiverIsAndRecieverIsAndTitleContainsIgnoreCase(giver, reciever, giftTitleFilter, paging);
    }

    public Page<Offering> allOfferedByToWithListTitle(String giverName, String recieverName, String giftTitleFilter, String listTitle, PageInputs pageSettings, String sortBy, String sortDirection){
        User giver = userRepository.findUserByUsernameIgnoreCase(giverName).orElseThrow(()->new NotFoundException("No user with username :"+giverName));
        User reciever = userRepository.findUserByUsernameIgnoreCase(recieverName).orElseThrow(()->new NotFoundException("No user with username :"+recieverName));

        Pageable paging = PageRequest.of(pageSettings.page(), pageSettings.pageSize(), Sort.by(Direction.fromString(sortDirection), sortBy));
        if(StringUtils.isBlank(giftTitleFilter)){
            return offeringRepository.findOfferingsByGiverIsAndRecieverIsAndListTitleIs(giver, reciever, listTitle, paging);
        }
        return offeringRepository.findOfferingsByGiverIsAndRecieverIsAndListTitleIsAndTitleContainsIgnoreCase(giver, reciever, listTitle, giftTitleFilter, paging);
    }

  
    public Page<Offering> allOfferedByWithListTitle(String giverName, String giftTitleFilter, String listTitle, PageInputs pageSettings, String sortBy, String sortDirection){
        User giver = userRepository.findUserByUsernameIgnoreCase(giverName).orElseThrow(()->new NotFoundException("No user with username :"+giverName));
      
        Pageable paging = PageRequest.of(pageSettings.page(), pageSettings.pageSize(), Sort.by(Direction.fromString(sortDirection), sortBy));
        if(StringUtils.isNotBlank(giftTitleFilter)){
            return offeringRepository.findOfferingsByGiverIsAndListTitleIsAndTitleContainsIgnoreCase(giver, listTitle, giftTitleFilter, paging);
        }
        return offeringRepository.findOfferingsByGiverIsAndListTitleIs(giver, listTitle, paging);
    }

    public Page<Offering> allOfferedBy(String giverName, String giftTitleFilter, PageInputs pageSettings, String sortBy, String sortDirection){
        User giver = userRepository.findUserByUsernameIgnoreCase(giverName).orElseThrow(()->new NotFoundException("No user with username :"+giverName));
        Pageable paging = PageRequest.of(pageSettings.page(), pageSettings.pageSize(), Sort.by(Direction.fromString(sortDirection), sortBy));
        if(StringUtils.isNotBlank(giftTitleFilter)){
            return offeringRepository.findOfferingsByGiverIsAndTitleContainsIgnoreCase(giver, giftTitleFilter, paging);
        }
        return offeringRepository.findOfferingsByGiverIs(giver, paging);
    }

    public Page<Offering> allOfferedTo(String recieverName, String giftTitleFilter, PageInputs pageSettings, String sortBy, String sortDirection){
        User reciever = userRepository.findUserByUsernameIgnoreCase(recieverName).orElseThrow(()-> new NotFoundException("No user with username :"+recieverName));
        Pageable paging = PageRequest.of(pageSettings.page(), pageSettings.pageSize(), Sort.by(Direction.fromString(sortDirection), sortBy));
        if(StringUtils.isNotBlank(giftTitleFilter)){
            return offeringRepository.findOfferingsByRecieverIsAndTitleContainsIgnoreCase(reciever, giftTitleFilter, paging);
        }
        return offeringRepository.findOfferingsByRecieverIs(reciever, paging);
    }

    public Page<Offering> allOfferedToWithListTitle(String recieverName, String giftTitleFilter, String listTitle, PageInputs pageSettings, String sortBy, String sortDirection){
        User reciever = userRepository.findUserByUsernameIgnoreCase(recieverName).orElseThrow(()->new NotFoundException("No user with username :"+recieverName));
      
        Pageable paging = PageRequest.of(pageSettings.page(), pageSettings.pageSize(), Sort.by(Direction.fromString(sortDirection), sortBy));
        if(StringUtils.isNotBlank(giftTitleFilter)){
            return offeringRepository.findOfferingsByRecieverIsAndListTitleIsAndTitleContainsIgnoreCase(reciever, listTitle, giftTitleFilter, paging);
        }
        return offeringRepository.findOfferingsByRecieverIsAndListTitleIs(reciever, listTitle, paging);
    }
}
