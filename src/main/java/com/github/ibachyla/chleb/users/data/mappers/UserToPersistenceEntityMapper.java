package com.github.ibachyla.chleb.users.data.mappers;

import com.github.ibachyla.chleb.mappers.AbstractMapper;
import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.models.entities.User;
import org.springframework.stereotype.Component;

/**
 * Maps a {@link User} to a {@link UserEntity}.
 */
@Component
public class UserToPersistenceEntityMapper extends AbstractMapper<User, UserEntity> {

  @Override
  protected UserEntity doMap(User source) {
    UserEntity userEntity = new UserEntity();
    userEntity.id(source.id());
    userEntity.email(source.email().value());
    userEntity.username(source.username().value());
    userEntity.fullName(source.fullName());
    userEntity.password(source.password().value());

    return userEntity;
  }
}
