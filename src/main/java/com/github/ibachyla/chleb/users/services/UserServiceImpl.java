package com.github.ibachyla.chleb.users.services;

import static org.apache.commons.lang3.Validate.notNull;

import com.github.ibachyla.chleb.groups.data.repositories.GroupRepository;
import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.security.services.JwtTokenSupplier;
import com.github.ibachyla.chleb.security.services.PasswordEncoder;
import com.github.ibachyla.chleb.security.services.PasswordMatcher;
import com.github.ibachyla.chleb.users.UsersProperties;
import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.data.repositories.UserRepository;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Role;
import com.github.ibachyla.chleb.users.models.values.Username;
import com.github.ibachyla.chleb.users.services.exceptions.EmailAlreadyRegisteredException;
import com.github.ibachyla.chleb.users.services.exceptions.PasswordInvalidException;
import com.github.ibachyla.chleb.users.services.exceptions.UserNotFoundException;
import com.github.ibachyla.chleb.users.services.exceptions.UsernameAlreadyRegisteredException;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * User service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UsersProperties usersProperties;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;
  private final Mapper<User, UserEntity> userToPersistableMapper;
  private final Mapper<UserEntity, User> userFromPersistableMapper;
  private final PasswordMatcher passwordMatcher;
  private final JwtTokenSupplier jwtTokenSupplier;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User register(User user) {
    notNull(user, "user cannot be null");

    userRepository.findByEmailOrUsername(user.email().value(), user.username().value())
        .ifPresent(u -> throwIfEmailOrUsernameMatches(u, user.email(), user.username()));

    userRepository.save(userToPersistableMapper.map(user));

    return user;
  }

  @Override
  @Transactional
  public String login(String usernameOrEmail, char[] password) {
    User user = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
        .map(userFromPersistableMapper::map)
        .orElseThrow(UserNotFoundException::new);

    if (!user.password().matches(password, passwordMatcher)) {
      throw new PasswordInvalidException();
    }

    return jwtTokenSupplier.supply(user);
  }

  @Override
  public boolean isDefaultUserPresent() {
    return userRepository.existsByEmail(usersProperties.defaultEmail());
  }

  @Override
  @Transactional
  public void createDefaultUser(UUID groupId) {
    if (userRepository.count() != 0) {
      return;
    }

    log.info("First-time startup detected. No users found. Creating default user.");

    User defaultUser = buildDefaultUser(groupId);

    UserEntity userEntity = userToPersistableMapper.map(defaultUser);
    userEntity.group(groupRepository.getReferenceById(defaultUser.groupId()));

    userRepository.save(userEntity);
    log.info("Default user created: {}", defaultUser);
  }

  private User buildDefaultUser(UUID groupId) {
    Email email;
    HashedPassword password;

    String defaultEmail = usersProperties.defaultEmail();
    if (defaultEmail == null) {
      throw new RuntimeException("Default email is not provided. Default user creation failed.");
    }

    try {
      email = new Email(defaultEmail);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(
          "Invalid default email was provided. Default user creation failed.", ex);
    }

    String defaultPassword = usersProperties.defaultPassword();
    if (defaultPassword == null) {
      throw new RuntimeException("Default password is not provided. Default user creation failed.");
    }

    try {
      password = new HashedPassword(defaultPassword.toCharArray(), passwordEncoder);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException(
          "Invalid default password was provided. Default user creation failed.", ex);
    }

    return User.builder()
        .email(email)
        .username(new Username(usersProperties.defaultUsername()))
        .fullName(usersProperties.defaultFullName())
        .password(password)
        .role(Role.ADMIN)
        .groupId(groupId)
        .build();
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
