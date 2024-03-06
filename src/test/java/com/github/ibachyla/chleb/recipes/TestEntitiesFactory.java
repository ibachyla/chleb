package com.github.ibachyla.chleb.recipes;

import static java.util.UUID.randomUUID;

import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.models.values.RecipeName;
import com.github.ibachyla.chleb.recipes.models.values.Slug;
import java.time.Instant;

/**
 * Test entities factory.
 */
public final class TestEntitiesFactory {

  public static RecipeName recipeName() {
    return new RecipeName("Potato Bread");
  }

  public static Slug recipeSlug() {
    return Slug.from(recipeName().value());
  }

  @SuppressWarnings("TimeZoneUsage")
  public static Recipe recipe() {
    return new Recipe(recipeName(), recipeSlug(), Instant.now());
  }

  /**
   * Create a persistence recipe entity with default values.
   *
   * @return a persistence recipe entity
   */
  @SuppressWarnings("TimeZoneUsage")
  public static RecipeEntity recipeEntity() {
    RecipeEntity recipeEntity = new RecipeEntity();
    recipeEntity.id(randomUUID());
    recipeEntity.name("Potato Bread");
    recipeEntity.slug("potato-bread");
    recipeEntity.createdAt(Instant.now().getEpochSecond());
    recipeEntity.updatedAt(Instant.now().getEpochSecond());
    return recipeEntity;
  }
}
