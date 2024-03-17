package com.scilab.giftslist.domain.tag.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.tag.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {
    public Optional<Tag> findTagByTitle(String title);
}
