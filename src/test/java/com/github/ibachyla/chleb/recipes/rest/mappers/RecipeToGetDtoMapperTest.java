package com.github.ibachyla.chleb.recipes.rest.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.recipes.TestValues;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

final class RecipeToGetDtoMapperTest {

  @Test
  void map_recipeToGetRecipeResponse_null() {
    // Arrange
    Mapper<Recipe, GetRecipeResponse> mapper = new RecipeToGetDtoMapper();

    // Act & Assert
    Exception ex = assertThrows(NullPointerException.class, () -> mapper.map(null));
    assertEquals("source cannot be null", ex.getMessage());
  }

  @Test
  void map_recipeToGetRecipeResponse() {
    // Arrange
    Mapper<Recipe, GetRecipeResponse> mapper = new RecipeToGetDtoMapper();
    Recipe recipe = TestValues.recipe();

    final DateTimeFormatter dateFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        .withZone(ZoneId.systemDefault());

    // Act
    GetRecipeResponse getRecipeResponse = mapper.map(recipe);

    // Assert
    assertEquals(recipe.id().toString(), getRecipeResponse.id());
    assertEquals(recipe.name().value(), getRecipeResponse.name());
    assertEquals(recipe.slug().value(), getRecipeResponse.slug());
    assertEquals(dateFormatter.format(recipe.createdAt()), getRecipeResponse.dateAdded());
    assertEquals(dateTimeFormatter.format(recipe.updatedAt()), getRecipeResponse.dateUpdated());
    assertEquals(dateTimeFormatter.format(recipe.createdAt()), getRecipeResponse.createdAt());
    assertEquals(dateTimeFormatter.format(recipe.updatedAt()), getRecipeResponse.updatedAt());
  }
}
