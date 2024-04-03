package com.github.ibachyla.chleb.users.models.entities.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ibachyla.chleb.security.services.PasswordEncoder;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class HashedPasswordTest {

  private final PasswordEncoder passwordEncoder = String::new;

  @Test
  void constructor_positive_validPassword() {
    // Arrange
    char[] password = "ValidPassword1!".toCharArray();

    // Act
    HashedPassword hashedPassword = new HashedPassword(password, passwordEncoder);

    // Assert
    assertThat(hashedPassword.value()).isEqualTo(passwordEncoder.encode(password));
  }

  @Test
  void constructor_negative_nullPassword() {
    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(null, passwordEncoder))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("value cannot be null");
  }

  @ParameterizedTest
  @ValueSource(chars = {' ', '\t', '\n', '\r'})
  void constructor_negative_passwordContainsWhitespace(char whitespace) {
    // Arrange
    char[] password = ("ValidPassword1!" + whitespace).toCharArray();

    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(password, passwordEncoder))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must not contain any whitespace characters");
  }

  @Test
  void constructor_negative_passwordTooShort() {
    // Arrange
    char[] password = "Short1!".toCharArray();

    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(password, passwordEncoder))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must be between 8 and 64 characters long");
  }

  @Test
  void constructor_negative_passwordTooLong() {
    // Arrange
    char[] password = new char[65];
    Arrays.fill(password, 'a');

    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(password, passwordEncoder))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must be between 8 and 64 characters long");
  }

  @Test
  void constructor_negative_passwordWithoutDigit() {
    // Arrange
    char[] password = "InvalidPassword!".toCharArray();

    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(password, passwordEncoder))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must contain at least one digit");
  }

  @Test
  void constructor_negative_passwordWithoutLowercase() {
    // Arrange
    char[] password = "INVALIDPASSWORD1!".toCharArray();

    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(password, passwordEncoder))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must contain at least one lowercase letter");
  }

  @Test
  void constructor_negative_passwordWithoutUppercase() {
    // Arrange
    char[] password = "invalidpassword1!".toCharArray();

    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(password, passwordEncoder))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must contain at least one uppercase letter");
  }

  @Test
  void constructor_negative_passwordWithoutSpecialCharacter() {
    // Arrange
    char[] password = "InvalidPassword1".toCharArray();

    // Act & Assert
    assertThatThrownBy(() -> new HashedPassword(password, passwordEncoder))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must contain at least one special character");
  }

  @Test
  void toString_always_returnsAsterisks() {
    // Arrange
    char[] password = "ValidPassword1!".toCharArray();
    HashedPassword hashedPassword = new HashedPassword(password, passwordEncoder);

    // Act
    String result = hashedPassword.toString();

    // Assert
    assertThat(result).isEqualTo("********");
  }
}