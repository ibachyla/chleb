package com.github.ibachyla.chleb.users.data.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.data.entities.UserEntity;
import com.github.ibachyla.chleb.users.models.entities.User;
import org.junit.jupiter.api.Test;

final class UserToPersistenceEntityMapperTest {

  private final UserToPersistenceEntityMapper mapper = new UserToPersistenceEntityMapper();

  @Test
  void doMap() {
    // Arrange
    User user = TestValues.user();

    // Act
    UserEntity userEntity = mapper.doMap(user);

    // Assert
    assertThat(userEntity.id()).isEqualTo(user.id());
    assertThat(userEntity.email()).isEqualTo(user.email().value());
    assertThat(userEntity.username()).isEqualTo(user.username().value());
    assertThat(userEntity.fullName()).isEqualTo(user.fullName());
    assertThat(userEntity.password()).isEqualTo(user.password().value());
    assertThat(userEntity.role()).isEqualTo(user.role());
  }
}