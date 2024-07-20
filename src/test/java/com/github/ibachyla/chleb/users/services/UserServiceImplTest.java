package com.github.ibachyla.chleb.users.services;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.security.services.JwtTokenSupplier;
import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.data.mappers.UserFromPersistenceEntityMapper;
import com.github.ibachyla.chleb.users.data.mappers.UserToPersistenceEntityMapper;
import com.github.ibachyla.chleb.users.data.repositories.UserRepository;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Role;
import com.github.ibachyla.chleb.users.models.values.Username;
import com.github.ibachyla.chleb.users.services.exceptions.EmailAlreadyRegisteredException;
import com.github.ibachyla.chleb.users.services.exceptions.UsernameAlreadyRegisteredException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class UserServiceImplTest {

  public static final UUID ID = randomUUID();
  public static final Email EMAIL = new Email("test@example.com");
  public static final Username USERNAME = new Username("testuser");
  public static final String FULL_NAME = "Test User";
  public static final HashedPassword PASSWORD = new HashedPassword("ValidPassword1!");

  @Mock
  UserRepository userRepositoryMock;

  @Mock
  JwtTokenSupplier jwtTokenSupplier;

  UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(userRepositoryMock,
        new UserToPersistenceEntityMapper(),
        new UserFromPersistenceEntityMapper(),
        (password, encodedPassword) -> true,
        jwtTokenSupplier);
  }

  @Test
  void register_positive() {
    // Arrange
    User user = new User(ID, EMAIL, USERNAME, FULL_NAME, PASSWORD, Role.USER);
    when(userRepositoryMock.findByEmailOrUsername(user.email().value(), user.username().value()))
        .thenReturn(Optional.empty());

    // Act
    User registeredUser = userService.register(user);

    // Assert
    assertThat(registeredUser).isNotNull();
    verify(userRepositoryMock).save(any(UserEntity.class));
  }

  @Test
  void register_negative_emailAlreadyRegistered() {
    // Arrange
    User user = new User(ID, EMAIL, USERNAME, FULL_NAME, PASSWORD, Role.USER);
    UserEntity existingUser = new UserEntity();
    existingUser.email(user.email().value());
    when(userRepositoryMock.findByEmailOrUsername(user.email().value(), user.username().value()))
        .thenReturn(Optional.of(existingUser));

    // Act & Assert
    assertThatThrownBy(() -> userService.register(user))
        .isInstanceOf(EmailAlreadyRegisteredException.class);
  }

  @Test
  void register_negative_usernameAlreadyRegistered() {
    // Arrange
    User user = new User(ID, EMAIL, USERNAME, FULL_NAME, PASSWORD, Role.USER);
    UserEntity existingUser = new UserEntity();
    existingUser.username(user.username().value());
    when(userRepositoryMock.findByEmailOrUsername(user.email().value(), user.username().value()))
        .thenReturn(Optional.of(existingUser));

    // Act & Assert
    assertThatThrownBy(() -> userService.register(user))
        .isInstanceOf(UsernameAlreadyRegisteredException.class);
  }

  @Test
  void registerUser_negative_null() {
    // Act & Assert
    assertThatThrownBy(() -> userService.register(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("user cannot be null");
  }
}