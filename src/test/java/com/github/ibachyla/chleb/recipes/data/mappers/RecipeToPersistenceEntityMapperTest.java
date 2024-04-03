package com.github.ibachyla.chleb.recipes.data.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.recipes.TestValues;
import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import org.junit.jupiter.api.Test;

final class RecipeToPersistenceEntityMapperTest {

  @Test
  void map_recipeToRecipeEntity_null() {
    // Arrange
    Mapper<Recipe, RecipeEntity> mapper = new RecipeToPersistenceEntityMapper();

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> mapper.map(null));
    assertEquals("source cannot be null", ex.getMessage());
  }

  @Test
  void map_recipeToRecipeEntity() {
    // Arrange
    Mapper<Recipe, RecipeEntity> mapper = new RecipeToPersistenceEntityMapper();
    Recipe recipe = TestValues.recipe();

    // Act
    RecipeEntity recipeEntity = mapper.map(recipe);

    // Assert
    assertEquals(recipe.id(), recipeEntity.id());
    assertEquals(recipe.name().value(), recipeEntity.name());
    assertEquals(recipe.slug().value(), recipeEntity.slug());
    assertEquals(recipe.createdAt().getEpochSecond(), recipeEntity.createdAt());
    assertEquals(recipe.updatedAt().getEpochSecond(), recipeEntity.updatedAt());
  }
}
