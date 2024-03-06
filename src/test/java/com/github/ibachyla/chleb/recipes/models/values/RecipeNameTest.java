package com.github.ibachyla.chleb.recipes.models.values;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class RecipeNameTest {

  @ParameterizedTest
  @ValueSource(strings = {"potato", "potato ", " potato", " potato ", "potato potato"})
  void constructor_positive_validName(String name) {
    // Act
    RecipeName recipeName = new RecipeName(name);

    // Assert
    assertEquals(name.trim(), recipeName.value());
  }

  @Test
  void constructor_negative_nullName() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> new RecipeName(null));
    assertEquals("value cannot be null", ex.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\t", "\n", "\r", " \t\n\r"})
  void constructor_negative_blankName(String name) {
    // Act & Assert
    Exception ex = assertThrows(IllegalArgumentException.class, () -> new RecipeName(name));
    assertEquals("value cannot be blank", ex.getMessage());
  }
}
