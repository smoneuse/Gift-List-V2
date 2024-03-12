package com.scilab.giftslist.domain.gift.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scilab.giftslist.domain.gift.GiftStatus;
import com.scilab.giftslist.domain.gift.repo.GiftRepository;
import com.scilab.giftslist.domain.lists.model.GiftList;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    public long countGiftsInList(GiftList giftList, GiftStatus... status){
        return giftRepository.countGiftsByOwningListAndStatusIn(giftList, status);
    }
}
