package com.github.ibachyla.chleb.recipes.rest.mappers;

import com.github.ibachyla.chleb.mappers.AbstractMapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.models.values.RecipeName;
import com.github.ibachyla.chleb.recipes.models.values.Slug;
import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Maps a {@link CreateRecipeRequest} to a {@link Recipe}.
 */
@Component
@RequiredArgsConstructor
public class RecipeFromCreateDtoMapper extends AbstractMapper<CreateRecipeRequest, Recipe> {

  private final Clock clock;

  @Override
  protected Recipe doMap(CreateRecipeRequest source) {
    return new Recipe(
        new RecipeName(source.name()),
        Slug.from(source.name()),
        clock.instant()
    );
  }
}
