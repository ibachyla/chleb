package com.github.ibachyla.chleb.users.models.entities.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ibachyla.chleb.users.models.values.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class EmailTest {

  @ParameterizedTest
  @ValueSource(strings = {"test@example.com",
      "test.test@example.com",
      "test.test.test@example.co.uk",
      " test@example.com "})
  void constructor_positive_validEmail(String email) {
    // Act
    Email emailObj = new Email(email);

    // Assert
    assertThat(emailObj.value()).isEqualTo(email.trim());
  }

  @Test
  void constructor_negative_nullEmail() {
    // Act & Assert
    assertThatThrownBy(() -> new Email(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("value cannot be blank");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t", "\n", "\r", " \t\n\r"})
  void constructor_negative_blankEmail(String email) {
    // Act & Assert
    assertThatThrownBy(() -> new Email(email))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value cannot be blank");
  }

  @ParameterizedTest
  @ValueSource(strings = {"test", "test@", "@example.com", "test.example.com", "test@.com"})
  void constructor_negative_invalidEmail(String email) {
    // Act & Assert
    assertThatThrownBy(() -> new Email(email))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("value must be a valid email address");
  }
}