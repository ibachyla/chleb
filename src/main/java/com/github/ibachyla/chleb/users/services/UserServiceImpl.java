package com.github.ibachyla.chleb.users.services;

import static org.apache.commons.lang3.Validate.notNull;

import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.data.repositories.UserRepository;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.Username;
import com.github.ibachyla.chleb.users.services.exceptions.EmailAlreadyRegisteredException;
import com.github.ibachyla.chleb.users.services.exceptions.UsernameAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * User service implementation.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final Mapper<User, UserEntity> userToPersistableMapper;

  @Override
  public User registerUser(User user) {
    notNull(user, "user cannot be null");

    userRepository.findByEmailOrUsername(user.email().value(), user.username().value())
        .ifPresent(u -> throwIfEmailOrUsernameMatches(u, user.email(), user.username()));

    userRepository.save(userToPersistableMapper.map(user));

    return user;
  }

  private static void throwIfEmailOrUsernameMatches(UserEntity user,
                                                    Email email,
                                                    Username username) {
    if (email.matches(user.email())) {
      throw new EmailAlreadyRegisteredException();
    }

    if (username.matches(user.username())) {
      throw new UsernameAlreadyRegisteredException();
    }
  }
}
