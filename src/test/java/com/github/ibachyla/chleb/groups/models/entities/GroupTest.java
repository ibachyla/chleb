package com.github.ibachyla.chleb.groups.models.entities;

import com.github.ibachyla.chleb.groups.TestValues;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(SoftAssertionsExtension.class)
final class GroupTest {

  @InjectSoftAssertions
  SoftAssertions softly;

  @Test
  void constructor_positive() {
    // Arrange
    String name = TestValues.groupName();

    // Act
    var group = new Group(name);

    // Assert
    softly.assertThat(group.id()).isNotNull();
    softly.assertThat(group.name()).isEqualTo(name);
  }

  @Test
  void constructor_full_positive() {
    // Arrange
    var id = TestValues.groupId();
    var name = TestValues.groupName();

    // Act
    var group = new Group(id, name);

    // Assert
    softly.assertThat(group.id()).isEqualTo(id);
    softly.assertThat(group.name()).isEqualTo(name);
  }

  @Test
  void constructor_negative_blankName() {
    // Arrange
    String name = "";

    // Act & Assert
    softly.assertThatThrownBy(() -> new Group(name))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("name cannot be blank");
  }

  @ParameterizedTest
  @MethodSource("namesOfWrongLength")
  void constructor_negative_nameOfWrongLength(String name) {
    // Act & Assert
    softly.assertThatThrownBy(() -> new Group(name))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("name length must be between 3 and 32 characters");
  }

  static Stream<String> namesOfWrongLength() {
    return Stream.of("ab", "a".repeat(33));
  }
}