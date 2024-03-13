package com.scilab.giftslist.domain.gift.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.gift.Gift;
import com.scilab.giftslist.domain.gift.GiftStatus;
import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.user.model.User;

public interface GiftRepository extends MongoRepository<Gift, String>{
    long countGiftsByOwningListAndStatusIn(GiftList giftList, GiftStatus... status);
    long countGiftsByOwningListAndGiverIsAndStatusIn(GiftList giftList, User giver,GiftStatus... status);
}
