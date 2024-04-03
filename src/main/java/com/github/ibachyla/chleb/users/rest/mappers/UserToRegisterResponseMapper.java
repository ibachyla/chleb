package com.github.ibachyla.chleb.users.rest.mappers;

import com.github.ibachyla.chleb.mappers.AbstractMapper;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponseDto;
import org.springframework.stereotype.Component;

/**
 * Maps a user entity to a register user response DTO.
 */
@Component
public class UserToRegisterResponseMapper extends AbstractMapper<User, RegisterUserResponseDto> {

  @Override
  protected RegisterUserResponseDto doMap(User source) {
    return new RegisterUserResponseDto(
        source.email().value(),
        source.fullName(),
        source.username().value()
    );
  }
}
