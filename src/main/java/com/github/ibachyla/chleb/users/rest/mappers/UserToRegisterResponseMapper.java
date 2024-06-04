package com.github.ibachyla.chleb.users.rest.mappers;

import com.github.ibachyla.chleb.mappers.AbstractMapper;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponse;
import org.springframework.stereotype.Component;

/**
 * Maps a user entity to a register user response DTO.
 */
@Component
public class UserToRegisterResponseMapper extends AbstractMapper<User, RegisterUserResponse> {

  @Override
  protected RegisterUserResponse doMap(User source) {
    return new RegisterUserResponse(
        source.email().value(),
        source.fullName(),
        source.username().value()
    );
  }
}
