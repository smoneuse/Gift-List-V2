package com.scilab.giftslist.domain.gift.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.gift.GiftStatus;
import com.scilab.giftslist.domain.gift.repo.GiftRepository;
import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.user.model.User;
import com.scilab.giftslist.domain.user.repo.UserRepository;
import com.scilab.giftslist.infra.errors.NotFoundException;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private UserRepository userRepository;

    public long countGiftsInList(GiftList giftList, GiftStatus... status){
        return giftRepository.countGiftsByOwningListAndStatusIn(giftList, status);
    }

    public long countUserReservedGiftsInList(GiftList giftList, String giverUsername){
        User giver = userRepository.findUserByUsernameIgnoreCase(giverUsername).orElseThrow(()->new NotFoundException("The giver user name is not found :"+giverUsername));
        return giftRepository.countGiftsByOwningListAndGiverIsAndStatusIn(giftList, giver, GiftStatus.RESERVED);
    }
}
