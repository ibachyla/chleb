package com.github.ibachyla.chleb.recipes.models.validators;

import com.github.ibachyla.chleb.models.validators.AbstractEntityValidator;
import com.github.ibachyla.chleb.models.validators.ValidationErrorsHandler;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.models.values.Slug;
import org.springframework.stereotype.Component;

/**
 * Recipe validator.
 */
@Component
public class RecipeValidator extends AbstractEntityValidator<Recipe> {

  @Override
  protected void performValidation(Recipe entity, ValidationErrorsHandler errorsHandler) {
    if (!nameMatchesSlug(entity)) {
      errorsHandler.addError("Recipe's name and readable part of slug must match");
    }
    if (!createdAtIsBeforeOrEqualToUpdatedAt(entity)) {
      errorsHandler.addError("Recipe's createdAt must be before or equal to updatedAt");
    }
  }

  private boolean nameMatchesSlug(Recipe entity) {
    return Slug.from(entity.name().value()).readablePart().equals(entity.slug().readablePart());
  }

  private boolean createdAtIsBeforeOrEqualToUpdatedAt(Recipe entity) {
    return entity.createdAt().isBefore(entity.updatedAt())
        || entity.createdAt().equals(entity.updatedAt());
  }
}
