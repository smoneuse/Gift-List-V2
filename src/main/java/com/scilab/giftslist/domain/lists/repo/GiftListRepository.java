package com.scilab.giftslist.domain.lists.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.lists.model.GiftList;
import com.scilab.giftslist.domain.user.model.User;

public interface GiftListRepository extends MongoRepository<GiftList, String>{
    Page<GiftList> findGiftListsByOwner(User user,Pageable pageable);
    Optional<GiftList> findGiftListByNameIsAndOwnerIs(String name, User user);
    Optional<GiftList> findGiftListByNameIsAndOwnerIsAndIdIsNot(String name, User owner, String id);
    Optional<GiftList> findGiftListByIdAndOwnerIs(String id, User owner);
}
