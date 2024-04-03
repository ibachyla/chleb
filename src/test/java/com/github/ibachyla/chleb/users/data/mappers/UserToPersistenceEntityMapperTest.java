package com.github.ibachyla.chleb.users.data.mappers;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Username;
import java.util.UUID;
import org.junit.jupiter.api.Test;

final class UserToPersistenceEntityMapperTest {

  private final UserToPersistenceEntityMapper mapper = new UserToPersistenceEntityMapper();

  @Test
  void doMap() {
    // Arrange
    UUID id = randomUUID();
    User user = new User(id,
        new Email("test@example.com"),
        new Username("testuser"),
        "Test User",
        new HashedPassword("ValidPassword1!"));

    // Act
    UserEntity userEntity = mapper.doMap(user);

    // Assert
    assertThat(userEntity.id()).isEqualTo(user.id());
    assertThat(userEntity.email()).isEqualTo(user.email().value());
    assertThat(userEntity.username()).isEqualTo(user.username().value());
    assertThat(userEntity.fullName()).isEqualTo(user.fullName());
    assertThat(userEntity.password()).isEqualTo(user.password().value());
  }
}