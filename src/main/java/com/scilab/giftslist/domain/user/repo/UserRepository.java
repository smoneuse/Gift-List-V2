package com.scilab.giftslist.domain.user.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import com.scilab.giftslist.domain.user.model.User;

public interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findUserByUsernameIgnoreCase(String username);
    Optional<User> findUserByEmailIgnoreCase(String email);
    Page<User> findUsersByUsernameContaining(String username, Pageable pageable);
    @Query(value = "{username:?0}", fields = "{friends:1}") 
    Page<User> findUserFriends(String username,  Pageable pageable );

}
