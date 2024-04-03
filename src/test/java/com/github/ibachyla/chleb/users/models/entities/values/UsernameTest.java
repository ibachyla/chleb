package com.github.ibachyla.chleb.users.models.entities.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ibachyla.chleb.users.models.values.Username;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class UsernameTest {

  @ParameterizedTest
  @ValueSource(strings = {"username",
      "user_name",
      "user-name",
      "user123",
      "123_user",
      " username ",
      "user.name"})
  void constructor_positive_validUsername(String username) {
    // Act
    Username usernameObj = new Username(username);

    // Assert
    assertThat(usernameObj.value()).isEqualTo(username.trim());
  }

  @Test
  void constructor_negative_nullUsername() {
    // Act & Assert
    assertThatThrownBy(() -> new Username(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("value cannot be null");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t", "\n", "\r", " \t\n\r"})
  void constructor_negative_blankUsername(String username) {
    // Act & Assert
    assertThatThrownBy(() -> new Username(username))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value cannot be blank");
  }

  @ParameterizedTest
  @ValueSource(strings = {"us", "a_very_long_username_that_exceeds_twenty_characters"})
  void constructor_negative_tooShortOrLongUsername(String username) {
    // Act & Assert
    assertThatThrownBy(() -> new Username(username))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must be between 3 and 20 characters long");
  }

  @ParameterizedTest
  @ValueSource(strings = {"user name", "user@name", "user#name", "user$name", "user%name"})
  void constructor_negative_invalidCharacters(String username) {
    // Act & Assert
    assertThatThrownBy(() -> new Username(username))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(
            "value can only contain alphanumeric characters, underscores, hyphens and dots");
  }
}