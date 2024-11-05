package com.github.ibachyla.chleb.recipes.models.entities;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ibachyla.chleb.recipes.TestValues;
import com.github.ibachyla.chleb.recipes.models.values.RecipeName;
import com.github.ibachyla.chleb.recipes.models.values.Slug;
import java.time.Instant;
import org.junit.jupiter.api.Test;

final class RecipeTest {

  private static final String RECIPE_NAME = "Potato Bread";

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void constructor_positive() {
    // Act
    new Recipe(randomUUID(),
        new RecipeName(RECIPE_NAME),
        Slug.from(RECIPE_NAME),
        Instant.now(),
        Instant.now());
  }

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void constructor_positive_nullId() {
    // Act & Assert
    Recipe recipe = new Recipe(null,
        new RecipeName(RECIPE_NAME),
        Slug.from(RECIPE_NAME),
        Instant.now(),
        Instant.now());
    assertThat(recipe.id()).isNotNull();
  }

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void constructor_negative_nullName() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> new Recipe(randomUUID(),
        null,
        Slug.from(RECIPE_NAME),
        Instant.now(),
        Instant.now()));
    assertEquals("name cannot be null", ex.getMessage());
  }

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void constructor_negative_nullSlug() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> new Recipe(randomUUID(),
        new RecipeName(RECIPE_NAME),
        null,
        Instant.now(),
        Instant.now()));
    assertEquals("slug cannot be null", ex.getMessage());
  }

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void constructor_negative_nullCreatedAt() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> new Recipe(randomUUID(),
        new RecipeName(RECIPE_NAME),
        Slug.from(RECIPE_NAME),
        null,
        Instant.now()));
    assertEquals("createdAt cannot be null", ex.getMessage());
  }

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void constructor_negative_nullUpdatedAt() {
    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> new Recipe(randomUUID(),
        new RecipeName(RECIPE_NAME),
        Slug.from(RECIPE_NAME),
        Instant.now(),
        null));
    assertEquals("updatedAt cannot be null", ex.getMessage());
  }

  @Test
  void setName_positive() {
    // Arrange
    Recipe recipe = TestValues.recipe();
    RecipeName newName = new RecipeName("Banana Bread");

    // Act
    recipe.name(newName);

    // Assert
    assertEquals(newName, recipe.name());
  }

  @Test
  void setName_negative_nullName() {
    // Arrange
    Recipe recipe = TestValues.recipe();

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> recipe.name(null));
    assertEquals("name cannot be null", ex.getMessage());
  }

  @Test
  void setSlug_positive() {
    // Arrange
    Recipe recipe = TestValues.recipe();
    Slug newSlug = Slug.from("Banana Bread");

    // Act
    recipe.slug(newSlug);

    // Assert
    assertEquals(newSlug, recipe.slug());
  }

  @Test
  void setSlug_negative_nullSlug() {
    // Arrange
    Recipe recipe = TestValues.recipe();

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> recipe.slug(null));
    assertEquals("slug cannot be null", ex.getMessage());
  }

  @Test
  void setCreatedAt_positive() {
    // Arrange
    Recipe recipe = TestValues.recipe();
    Instant newCreatedAt = Instant.EPOCH;

    // Act
    recipe.createdAt(newCreatedAt);

    // Assert
    assertEquals(newCreatedAt, recipe.createdAt());
  }

  @Test
  void setCreatedAt_negative_nullCreatedAt() {
    // Arrange
    Recipe recipe = TestValues.recipe();

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> recipe.createdAt(null));
    assertEquals("createdAt cannot be null", ex.getMessage());
  }

  @Test
  void setUpdatedAt_positive() {
    // Arrange
    Recipe recipe = TestValues.recipe();
    Instant newUpdatedAt = Instant.EPOCH;

    // Act
    recipe.updatedAt(newUpdatedAt);

    // Assert
    assertEquals(newUpdatedAt, recipe.updatedAt());
  }

  @Test
  void setUpdatedAt_negative_nullUpdatedAt() {
    // Arrange
    Recipe recipe = TestValues.recipe();

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> recipe.updatedAt(null));
    assertEquals("updatedAt cannot be null", ex.getMessage());
  }
}
