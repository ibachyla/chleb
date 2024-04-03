package com.github.ibachyla.chleb.recipes.models.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ibachyla.chleb.models.validators.ValidationException;
import com.github.ibachyla.chleb.recipes.TestValues;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.models.values.RecipeName;
import org.junit.jupiter.api.Test;

final class RecipeValidatorTest {

  @Test
  void validate_noErrors() {
    // Arrange
    RecipeValidator validator = new RecipeValidator();
    Recipe recipe = TestValues.recipe();

    // Act & Assert
    assertThatCode(() -> validator.validate(recipe)).doesNotThrowAnyException();
  }

  @Test
  void validate_nameDoesNotMatchSlug() {
    // Arrange
    RecipeValidator validator = new RecipeValidator();
    Recipe recipe = TestValues.recipe();
    recipe.name(new RecipeName("Potato Bread 2"));

    // Act & Assert
    Exception ex = assertThrows(ValidationException.class, () -> validator.validate(recipe));
    assertThat(ex.getMessage()).contains("Recipe's name and readable part of slug must match");
  }

  @Test
  void validate_createdAtIsAfterUpdatedAt() {
    // Arrange
    RecipeValidator validator = new RecipeValidator();
    Recipe recipe = TestValues.recipe();
    recipe.createdAt(recipe.updatedAt().plusSeconds(1));

    // Act & Assert
    Exception ex = assertThrows(ValidationException.class, () -> validator.validate(recipe));
    assertThat(ex.getMessage()).contains("Recipe's createdAt must be before or equal to updatedAt");
  }

  @Test
  void validate_createdAtIsBeforeUpdatedAt() {
    // Arrange
    RecipeValidator validator = new RecipeValidator();
    Recipe recipe = TestValues.recipe();
    recipe.createdAt(recipe.updatedAt().minusSeconds(1));

    // Act & Assert
    assertThatCode(() -> validator.validate(recipe)).doesNotThrowAnyException();
  }
}
