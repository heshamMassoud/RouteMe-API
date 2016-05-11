package repositories;

import java.util.List;
import java.util.Optional;

import models.User;

import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, String> {

    void delete(User deleted);

    List<User> findAll();

    Optional<User> findOne(String id);

    User save(User saved);
}
