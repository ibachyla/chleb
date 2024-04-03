package com.github.ibachyla.chleb.users.rest.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequestDto;
import org.junit.jupiter.api.Test;

final class UserFromRegisterRequestMapperTest {

  private static final String ENCODED_PASSWORD = "encodedPassword";

  private final Mapper<RegisterUserRequestDto, User> mapper =
      new UserFromRegisterRequestMapper(password -> ENCODED_PASSWORD);

  @Test
  void map_success() {
    // Arrange
    RegisterUserRequestDto requestDto = new RegisterUserRequestDto(
        "test@test.com",
        "testUser",
        "Test User",
        new char[] {'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '!'},
        new char[] {'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '!'}
    );

    // Act
    User user = mapper.map(requestDto);

    // Assert
    assertThat(user.email().value()).isEqualTo(requestDto.email());
    assertThat(user.username().value()).isEqualTo(requestDto.username());
    assertThat(user.fullName()).isEqualTo(requestDto.fullName());
    assertThat(user.password().value()).isEqualTo(ENCODED_PASSWORD);

    assertThat(requestDto.password()).containsOnly('0');
    assertThat(requestDto.passwordConfirm()).containsOnly('0');
  }

  @Test
  void map_null_throwsException() {
    // Act & Assert
    assertThatThrownBy(() -> mapper.map(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("source cannot be null");
  }
}