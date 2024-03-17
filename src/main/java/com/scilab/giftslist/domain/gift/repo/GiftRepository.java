package com.scilab.giftslist.domain.gift.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.gift.Gift;
import com.scilab.giftslist.domain.gift.GiftStatus;
import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.user.model.User;

public interface GiftRepository extends MongoRepository<Gift, String>{
    long countGiftsByOwningListAndStatusIn(GiftList giftList, GiftStatus... status);
    Page<Gift> findGiftsByOwningListAndStatusIn(GiftList giftList, List<GiftStatus> status, Pageable paging);
    Page<Gift> findGiftsByOwningListAndGiverIsOrStatusIs(GiftList giftList, User giver, GiftStatus status, Pageable paging);
    Optional<Gift> findGiftByOwningListAndTitleIs(GiftList owningList, String title);
    long countGiftsByOwningListAndGiverIsAndStatusIn(GiftList giftList, User giver,GiftStatus... status);

}
