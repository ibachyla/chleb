package com.github.ibachyla.chleb.recipes.rest.mappers;

import com.github.ibachyla.chleb.recipes.mappers.AbstractMapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

/**
 * Maps a {@link Recipe} to a {@link GetRecipeResponse}.
 */
@Component
public class RecipeToGetDtoMapper extends AbstractMapper<Recipe, GetRecipeResponse> {

  private final DateTimeFormatter dateFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
  private final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
          .withZone(ZoneId.systemDefault());

  @Override
  protected GetRecipeResponse doMap(Recipe source) {
    return new GetRecipeResponse(
        source.id().toString(),
        source.name().value(),
        source.slug().value(),
        dateFormatter.format(source.createdAt()),
        dateTimeFormatter.format(source.updatedAt()),
        dateTimeFormatter.format(source.createdAt()),
        dateTimeFormatter.format(source.updatedAt())
    );
  }
}
