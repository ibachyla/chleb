package com.github.ibachyla.chleb.users.data.mappers;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.models.entities.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;

final class UserFromPersistenceEntityMapperTest {

  private final UserFromPersistenceEntityMapper mapper = new UserFromPersistenceEntityMapper();

  @Test
  void doMap() {
    // Arrange
    UUID id = randomUUID();
    UserEntity userEntity = new UserEntity();
    userEntity.id(id);
    userEntity.email("test@example.com");
    userEntity.username("testuser");
    userEntity.fullName("Test User");
    userEntity.password("ValidPassword1!");

    // Act
    User user = mapper.doMap(userEntity);

    // Assert
    assertThat(user.id()).isEqualTo(userEntity.id());
    assertThat(user.email().value()).isEqualTo(userEntity.email());
    assertThat(user.username().value()).isEqualTo(userEntity.username());
    assertThat(user.fullName()).isEqualTo(userEntity.fullName());
    assertThat(user.password().value()).isEqualTo(userEntity.password());
  }
}