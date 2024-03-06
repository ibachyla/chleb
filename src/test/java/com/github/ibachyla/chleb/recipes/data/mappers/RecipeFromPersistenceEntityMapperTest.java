package com.github.ibachyla.chleb.recipes.data.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ibachyla.chleb.recipes.TestEntitiesFactory;
import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import com.github.ibachyla.chleb.recipes.mappers.Mapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import java.time.Instant;
import org.junit.jupiter.api.Test;

final class RecipeFromPersistenceEntityMapperTest {

  @Test
  void map_recipeEntityToRecipe_null() {
    // Arrange
    Mapper<RecipeEntity, Recipe> mapper = new RecipeFromPersistenceEntityMapper();

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> mapper.map(null));
    assertEquals("source cannot be null", ex.getMessage());
  }

  @Test
  void map_recipeEntityToRecipe() {
    // Arrange
    Mapper<RecipeEntity, Recipe> mapper = new RecipeFromPersistenceEntityMapper();
    RecipeEntity recipeEntity = TestEntitiesFactory.recipeEntity();

    // Act
    Recipe recipe = mapper.map(recipeEntity);

    // Assert
    assertEquals(recipeEntity.id(), recipe.id());
    assertEquals(recipeEntity.name(), recipe.name().value());
    assertEquals(recipeEntity.slug(), recipe.slug().value());
    assertEquals(Instant.ofEpochSecond(recipeEntity.createdAt()), recipe.createdAt());
    assertEquals(Instant.ofEpochSecond(recipeEntity.updatedAt()), recipe.updatedAt());
  }
}
