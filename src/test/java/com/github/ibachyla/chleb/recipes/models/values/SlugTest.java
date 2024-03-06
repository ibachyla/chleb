package com.github.ibachyla.chleb.recipes.models.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

final class SlugTest {

  private static final int MAX_VALUE_LENGTH = 255;
  private static final int MAX_RAW_VALUE_LENGTH = 218;

  @MethodSource("validValues")
  @ParameterizedTest
  void constructor_positive_validValue(String value) {
    // Act
    Slug slug = new Slug(value);

    // Assert
    assertEquals(value, slug.value());
  }

  @Test
  void constructor_negative_nullValue() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> new Slug(null));
    assertEquals("value cannot be null", ex.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t", "\n", "\r", " \t\n\r"})
  void constructor_negative_blankValue(String value) {
    // Act & Assert
    Exception ex = assertThrows(IllegalArgumentException.class, () -> new Slug(value));
    assertEquals("value cannot be blank", ex.getMessage());
  }

  @Test
  void constructor_negative_tooLongValue() {
    // Arrange
    String value = "a".repeat(MAX_VALUE_LENGTH + 1);

    // Act & Assert
    Exception ex = assertThrows(IllegalArgumentException.class, () -> new Slug(value));
    assertEquals("value cannot be longer than 255 characters", ex.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"potato-", "-potato", "potato!"})
  void constructor_negative_valueWithUnexpectedFormat(String value) {
    // Act & Assert
    Exception ex = assertThrows(IllegalArgumentException.class, () -> new Slug(value));
    assertEquals(
        "value must contain only lowercase alphanumeric characters and hyphens, and cannot start"
            + " or end with a hyphen",
        ex.getMessage());
  }

  @MethodSource("validRawValues")
  @ParameterizedTest
  void from_positive_validRawValue(String rawValue, String expectedValue) {
    // Act
    Slug slug = Slug.from(rawValue);

    // Assert
    assertThat(slug.value()).startsWith(expectedValue);
  }

  @Test
  void from_negative_nullRawValue() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> Slug.from(null));
    assertEquals("value cannot be null", ex.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "!@#$%^&*()_+", "---"})
  void from_negative_blankRawValue(String rawValue) {
    // Act & Assert
    Exception ex = assertThrows(IllegalArgumentException.class, () -> Slug.from(rawValue));
    assertEquals("value cannot be blank", ex.getMessage());
  }

  @Test
  void from_negative_tooLongRawValue() {
    // Arrange
    String rawValue = "a".repeat(MAX_RAW_VALUE_LENGTH + 1);
    String expectedValue = rawValue.substring(0, MAX_RAW_VALUE_LENGTH);

    // Act
    Slug slug = Slug.from(rawValue);

    // Assert
    assertThat(slug.value()).startsWith(expectedValue);
  }

  @Test
  void readablePart() {
    // Arrange
    String rawValue = "Potato Bread";
    Slug slug = Slug.from(rawValue);

    // Act
    String readablePart = slug.readablePart();

    // Assert
    assertEquals("potato-bread", readablePart);
  }

  static Stream<String> validValues() {
    return Stream.of("potato",
        "potato-potato",
        "potato-potato-potato",
        "123",
        "123-potato",
        "potato-123",
        "123potato",
        "potato123",
        "p12o3tato",
        "p12o3tato-p12o3tato",
        "a".repeat(MAX_VALUE_LENGTH));
  }

  static Stream<Arguments> validRawValues() {
    return Stream.of(
        arguments("potato", "potato"),
        arguments(" potato ", "potato"),
        arguments("\tpotato\t", "potato"),
        arguments("\npotato\n", "potato"),
        arguments("\rpotato\r", "potato"),
        arguments("potato\r\n", "potato"),
        arguments("POTATO", "potato"),
        arguments("potato potato", "potato-potato"),
        arguments("potato  \t\n\r potato", "potato-potato"),
        arguments("potato!@#$%^&*()_+", "potato"),
        arguments("potato-", "potato"),
        arguments("potato--", "potato"),
        arguments("-potato", "potato"),
        arguments("--potato", "potato"),
        arguments("a".repeat(MAX_RAW_VALUE_LENGTH + 1), "a".repeat(MAX_RAW_VALUE_LENGTH)),
        arguments("żurek", "zurek")
    );
  }
}
