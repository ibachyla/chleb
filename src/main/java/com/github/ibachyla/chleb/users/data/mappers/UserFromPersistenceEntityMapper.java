package com.github.ibachyla.chleb.users.data.mappers;

import com.github.ibachyla.chleb.mappers.AbstractMapper;
import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Username;
import org.springframework.stereotype.Component;

/**
 * Maps a {@link UserEntity} to a {@link User}.
 */
@Component
public class UserFromPersistenceEntityMapper extends AbstractMapper<UserEntity, User> {

  @Override
  protected User doMap(UserEntity source) {
    return new User(
        source.id(),
        new Email(source.email()),
        new Username(source.username()),
        source.fullName(),
        new HashedPassword(source.password()),
        source.role()
    );
  }
}