package com.github.ibachyla.chleb.users.data.repositories;

import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User repository.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

  Optional<UserEntity> findByEmailOrUsername(String email, String username);
}
