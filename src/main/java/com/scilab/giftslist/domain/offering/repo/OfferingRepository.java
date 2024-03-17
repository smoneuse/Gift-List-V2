package com.scilab.giftslist.domain.offering.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.offering.model.Offering;
import com.scilab.giftslist.domain.user.model.User;

public interface OfferingRepository extends MongoRepository<Offering, String>{

    Page<Offering> findOfferingsByGiverIs(User giver, Pageable pageable);    
    Page<Offering> findOfferingsByGiverIsAndTitleContainsIgnoreCase(User giver, String title,Pageable pageable);
    Page<Offering> findOfferingsByGiverIsAndListTitleIs(User giver, String listTitle,Pageable pageable);
    Page<Offering> findOfferingsByGiverIsAndListTitleIsAndTitleContainsIgnoreCase(User giver, String listTitle,String title, Pageable pageable);

    Page<Offering> findOfferingsByRecieverIs(User reciever, Pageable pageable);    
    Page<Offering> findOfferingsByRecieverIsAndTitleContainsIgnoreCase(User reciever, String title,Pageable pageable);
    Page<Offering> findOfferingsByRecieverIsAndListTitleIs(User reciever, String listTitle,Pageable pageable);
    Page<Offering> findOfferingsByRecieverIsAndListTitleIsAndTitleContainsIgnoreCase(User reciever, String listTitle,String title, Pageable pageable);

    Page<Offering> findOfferingsByGiverIsAndRecieverIs(User giver, User reciever, Pageable pageable);
    Page<Offering> findOfferingsByGiverIsAndRecieverIsAndTitleContainsIgnoreCase(User giver, User reciever, String title, Pageable pageable);
    Page<Offering> findOfferingsByGiverIsAndRecieverIsAndListTitleIs(User giver, User reciever, String listTitle, Pageable pageable);
    Page<Offering> findOfferingsByGiverIsAndRecieverIsAndListTitleIsAndTitleContainsIgnoreCase(User giver, User reciever,String listTitle, String title, Pageable pageable);
}
