package com.github.ibachyla.chleb.users.services;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.groups.data.entities.GroupEntity;
import com.github.ibachyla.chleb.groups.data.repositories.GroupRepository;
import com.github.ibachyla.chleb.security.services.JwtTokenSupplier;
import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.UsersProperties;
import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.data.mappers.UserFromPersistenceEntityMapper;
import com.github.ibachyla.chleb.users.data.mappers.UserToPersistenceEntityMapper;
import com.github.ibachyla.chleb.users.data.repositories.UserRepository;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.Role;
import com.github.ibachyla.chleb.users.models.values.Username;
import com.github.ibachyla.chleb.users.services.exceptions.EmailAlreadyRegisteredException;
import com.github.ibachyla.chleb.users.services.exceptions.UsernameAlreadyRegisteredException;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
final class UserServiceImplTest {

  static final User USER = TestValues.user();

  @InjectSoftAssertions
  SoftAssertions softly;

  @Mock
  UsersProperties usersPropertiesMock;

  @Mock
  UserRepository userRepositoryMock;

  @Mock
  GroupRepository groupRepositoryMock;

  @Mock
  JwtTokenSupplier jwtTokenSupplier;

  UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(
        usersPropertiesMock,
        userRepositoryMock,
        groupRepositoryMock,
        new UserToPersistenceEntityMapper(),
        new UserFromPersistenceEntityMapper(),
        (password, encodedPassword) -> true,
        jwtTokenSupplier,
        String::new
    );
  }

  @Test
  void register_positive() {
    // Arrange
    when(userRepositoryMock.findByEmailOrUsername(USER.email().value(), USER.username().value()))
        .thenReturn(Optional.empty());

    // Act
    User registeredUser = userService.register(USER);

    // Assert
    assertThat(registeredUser).isNotNull();
    verify(userRepositoryMock).save(any(UserEntity.class));
  }

  @Test
  void register_negative_emailAlreadyRegistered() {
    // Arrange
    UserEntity existingUser = new UserEntity();
    existingUser.email(USER.email().value());
    when(userRepositoryMock.findByEmailOrUsername(USER.email().value(), USER.username().value()))
        .thenReturn(Optional.of(existingUser));

    // Act & Assert
    assertThatThrownBy(() -> userService.register(USER))
        .isInstanceOf(EmailAlreadyRegisteredException.class);
  }

  @Test
  void register_negative_usernameAlreadyRegistered() {
    // Arrange
    UserEntity existingUser = new UserEntity();
    existingUser.username(USER.username().value());
    when(userRepositoryMock.findByEmailOrUsername(USER.email().value(), USER.username().value()))
        .thenReturn(Optional.of(existingUser));

    // Act & Assert
    assertThatThrownBy(() -> userService.register(USER))
        .isInstanceOf(UsernameAlreadyRegisteredException.class);
  }

  @Test
  void registerUser_negative_null() {
    // Act & Assert
    assertThatThrownBy(() -> userService.register(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("user cannot be null");
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void isDefaultUserPresent(boolean isDefaultUserPresent) {
    // Arrange
    String defaultEmail = TestValues.email().value();

    when(usersPropertiesMock.defaultEmail()).thenReturn(defaultEmail);
    when(userRepositoryMock.existsByEmail(defaultEmail)).thenReturn(isDefaultUserPresent);

    // Act & Assert
    assertThat(userService.isDefaultUserPresent()).isEqualTo(isDefaultUserPresent);
  }

  @Test
  void createDefaultUser() {
    // Arrange
    when(userRepositoryMock.count()).thenReturn(0L);

    Email defaultEmail = TestValues.email();
    when(usersPropertiesMock.defaultEmail()).thenReturn(defaultEmail.value());

    Username defaultUsername = TestValues.username();
    when(usersPropertiesMock.defaultUsername()).thenReturn(defaultUsername.value());

    String defaultFullName = TestValues.fullName();
    when(usersPropertiesMock.defaultFullName()).thenReturn(defaultFullName);

    String defaultPassword = new String(TestValues.password());
    when(usersPropertiesMock.defaultPassword()).thenReturn(defaultPassword);

    UUID groupId = randomUUID();
    GroupEntity groupEntity = new GroupEntity();
    groupEntity.id(groupId);
    when(groupRepositoryMock.getReferenceById(groupId)).thenReturn(groupEntity);

    // Act
    userService.createDefaultUser(groupId);

    // Assert
    ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepositoryMock).save(userEntityCaptor.capture());
    UserEntity capturedUserEntity = userEntityCaptor.getValue();

    softly.assertThat(capturedUserEntity.email()).isEqualTo(defaultEmail.value());
    softly.assertThat(capturedUserEntity.username()).isEqualTo(defaultUsername.value());
    softly.assertThat(capturedUserEntity.fullName()).isEqualTo(defaultFullName);
    softly.assertThat(capturedUserEntity.password()).isEqualTo(defaultPassword);
    softly.assertThat(capturedUserEntity.role()).isEqualTo(Role.ADMIN);
    softly.assertThat(capturedUserEntity.group()).isEqualTo(groupEntity);
  }

  @Test
  void createDefaultUser_userAlreadyExists() {
    // Arrange
    when(userRepositoryMock.count()).thenReturn(1L);

    // Act
    userService.createDefaultUser(randomUUID());

    // Assert
    verify(userRepositoryMock, never()).save(any(UserEntity.class));
  }

  @Test
  void createDefaultUser_negative_nullDefaultEmail() {
    // Arrange
    when(userRepositoryMock.count()).thenReturn(0L);

    // Act & Assert
    assertThatThrownBy(() -> userService.createDefaultUser(randomUUID()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Default email is not provided. Default user creation failed.");
    verify(userRepositoryMock, never()).save(any(UserEntity.class));
  }

  @Test
  void createDefaultUser_negative_invalidDefaultEmail() {
    // Arrange
    when(userRepositoryMock.count()).thenReturn(0L);
    when(usersPropertiesMock.defaultEmail()).thenReturn("test");

    // Act & Assert
    assertThatThrownBy(() -> userService.createDefaultUser(randomUUID()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Invalid default email was provided. Default user creation failed.");
    verify(userRepositoryMock, never()).save(any(UserEntity.class));
  }

  @Test
  void createDefaultUser_negative_nullDefaultPassword() {
    // Arrange
    when(userRepositoryMock.count()).thenReturn(0L);
    when(usersPropertiesMock.defaultEmail()).thenReturn(TestValues.email().value());

    // Act & Assert
    assertThatThrownBy(() -> userService.createDefaultUser(randomUUID()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Default password is not provided. Default user creation failed.");
    verify(userRepositoryMock, never()).save(any(UserEntity.class));
  }

  @Test
  void createDefaultUser_negative_invalidDefaultPassword() {
    // Arrange
    when(userRepositoryMock.count()).thenReturn(0L);
    when(usersPropertiesMock.defaultEmail()).thenReturn(TestValues.email().value());
    when(usersPropertiesMock.defaultPassword()).thenReturn("test");

    // Act & Assert
    assertThatThrownBy(() -> userService.createDefaultUser(randomUUID()))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Invalid default password was provided. Default user creation failed.");
    verify(userRepositoryMock, never()).save(any(UserEntity.class));
  }
}