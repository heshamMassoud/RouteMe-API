package com.routeme.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.routeme.model.User;

public interface UserRepository extends Repository<User, String> {

    void delete(User deleted);

    List<User> findAll();

    Optional<User> findOne(String id);

    User save(User saved);
}
