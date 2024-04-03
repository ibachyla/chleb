package com.github.ibachyla.chleb.recipes.rest.mappers;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.models.values.Slug;
import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import java.time.Clock;
import java.time.Instant;
import org.junit.jupiter.api.Test;

final class RecipeFromCreateDtoMapperTest {

  Clock clock = Clock.fixed(Instant.EPOCH, UTC);

  @Test
  void map_createRecipeRequestToRecipe_null() {
    // Arrange
    Mapper<CreateRecipeRequest, Recipe> mapper = new RecipeFromCreateDtoMapper(clock);

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> mapper.map(null));
    assertEquals("source cannot be null", ex.getMessage());
  }

  @Test
  void map_createRecipeRequestToRecipe() {
    // Arrange
    Mapper<CreateRecipeRequest, Recipe> mapper = new RecipeFromCreateDtoMapper(clock);
    CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest("Potato Bread");

    // Act
    Recipe recipe = mapper.map(createRecipeRequest);

    // Assert
    assertEquals(createRecipeRequest.name(), recipe.name().value());
    assertEquals(Slug.from(createRecipeRequest.name()).readablePart(),
        recipe.slug().readablePart());
    assertEquals(Instant.EPOCH, recipe.createdAt());
    assertEquals(Instant.EPOCH, recipe.updatedAt());
  }
}
