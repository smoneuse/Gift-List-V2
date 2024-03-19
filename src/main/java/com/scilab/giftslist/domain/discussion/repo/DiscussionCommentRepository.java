package com.scilab.giftslist.domain.discussion.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.discussion.DiscussionComment;

public interface DiscussionCommentRepository extends MongoRepository<DiscussionComment, String>{
    Page<DiscussionComment> findAllByIdIn(List<String> ids, Pageable paging);
}
