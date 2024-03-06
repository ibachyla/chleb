package com.github.ibachyla.chleb.recipes.data.mappers;

import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import com.github.ibachyla.chleb.recipes.mappers.AbstractMapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import org.springframework.stereotype.Component;

/**
 * Maps a {@link Recipe} to a {@link RecipeEntity}.
 */
@Component
public class RecipeToPersistenceEntityMapper extends AbstractMapper<Recipe, RecipeEntity> {
  @Override
  protected RecipeEntity doMap(Recipe source) {
    RecipeEntity recipeEntity = new RecipeEntity();
    recipeEntity.id(source.id());
    recipeEntity.name(source.name().value());
    recipeEntity.slug(source.slug().value());
    recipeEntity.createdAt(source.createdAt().getEpochSecond());
    recipeEntity.updatedAt(source.updatedAt().getEpochSecond());

    return recipeEntity;
  }
}
