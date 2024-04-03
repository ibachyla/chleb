package com.github.ibachyla.chleb.users.rest.mappers;

import com.github.ibachyla.chleb.mappers.AbstractMapper;
import com.github.ibachyla.chleb.security.services.PasswordEncoder;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Username;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Maps a register user request DTO to a user entity.
 */
@Component
@RequiredArgsConstructor
public class UserFromRegisterRequestMapper extends AbstractMapper<RegisterUserRequestDto, User> {

  private final PasswordEncoder passwordEncoder;

  @Override
  protected User doMap(RegisterUserRequestDto source) {
    HashedPassword password = new HashedPassword(source.password(), passwordEncoder);
    source.cleanPassword();

    return new User(
        new Email(source.email()),
        new Username(source.username()),
        source.fullName(),
        password
    );
  }
}
