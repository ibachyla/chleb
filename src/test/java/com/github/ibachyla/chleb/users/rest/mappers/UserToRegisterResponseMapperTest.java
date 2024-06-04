package com.github.ibachyla.chleb.users.rest.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Username;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponse;
import org.junit.jupiter.api.Test;

final class UserToRegisterResponseMapperTest {

  private final Mapper<User, RegisterUserResponse> mapper = new UserToRegisterResponseMapper();

  @Test
  void map_success() {
    // Arrange
    User user = new User(
        new Email("test@test.com"),
        new Username("testUser"),
        "Test User",
        new HashedPassword("encodedPassword")
    );

    // Act
    RegisterUserResponse responseDto = mapper.map(user);

    // Assert
    assertThat(responseDto.email()).isEqualTo(user.email().value());
    assertThat(responseDto.username()).isEqualTo(user.username().value());
    assertThat(responseDto.fullName()).isEqualTo(user.fullName());
  }

  @Test
  void map_null_throwsException() {
    // Act & Assert
    assertThatThrownBy(() -> mapper.map(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("source cannot be null");
  }
}