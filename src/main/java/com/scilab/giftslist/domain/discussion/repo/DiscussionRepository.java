package com.scilab.giftslist.domain.discussion.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.discussion.Discussion;

public interface DiscussionRepository extends MongoRepository<Discussion, String>{

}
