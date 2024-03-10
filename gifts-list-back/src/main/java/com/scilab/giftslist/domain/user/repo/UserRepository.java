package com.scilab.giftslist.domain.user.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.scilab.giftslist.domain.user.model.User;

public interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findUserByUsernameIgnoreCase(String username);
    Optional<User> findUserByEmailIgnoreCase(String email);
}
