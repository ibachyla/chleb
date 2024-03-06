package com.github.ibachyla.chleb.recipes.services;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import com.github.ibachyla.chleb.recipes.data.repositories.RecipeRepository;
import com.github.ibachyla.chleb.recipes.mappers.Mapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.models.validators.EntityValidator;
import com.github.ibachyla.chleb.recipes.services.exceptions.RecipeNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Recipe service implementation.
 */
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

  private final RecipeRepository recipeRepository;
  private final EntityValidator<Recipe> recipeValidator;
  private final Mapper<RecipeEntity, Recipe> recipeFromPersistenceEntityMapper;
  private final Mapper<Recipe, RecipeEntity> recipeToPersistenceEntityMapper;

  @Override
  public Recipe createRecipe(Recipe recipe) {
    notNull(recipe, "recipe cannot be null");
    recipeValidator.validate(recipe);

    RecipeEntity recipeEntity =
        recipeRepository.save(recipeToPersistenceEntityMapper.map(recipe));
    return recipeFromPersistenceEntityMapper.map(recipeEntity);
  }

  @Override
  public Recipe getRecipe(String slugOrId) {
    notBlank(slugOrId, "slug or id cannot be blank");

    RecipeEntity recipeEntity = recipeRepository.findBySlugOrId(slugOrId, toUuidOrNull(slugOrId))
        .orElseThrow(() -> new RecipeNotFoundException(slugOrId));

    return recipeFromPersistenceEntityMapper.map(recipeEntity);
  }

  private static UUID toUuidOrNull(String value) {
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
