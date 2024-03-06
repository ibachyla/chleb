package com.github.ibachyla.chleb.recipes.data.mappers;

import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import com.github.ibachyla.chleb.recipes.mappers.AbstractMapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.models.values.RecipeName;
import com.github.ibachyla.chleb.recipes.models.values.Slug;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Maps a {@link RecipeEntity} to a {@link Recipe}.
 */
@Component
public class RecipeFromPersistenceEntityMapper extends AbstractMapper<RecipeEntity, Recipe> {

  @Override
  protected Recipe doMap(RecipeEntity source) {
    return new Recipe(
        source.id(),
        new RecipeName(source.name()),
        new Slug(source.slug()),
        Instant.ofEpochSecond(source.createdAt()),
        Instant.ofEpochSecond(source.updatedAt())
    );
  }
}
