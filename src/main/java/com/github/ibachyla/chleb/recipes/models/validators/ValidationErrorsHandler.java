package com.github.ibachyla.chleb.recipes.models.validators;

/**
 * Validation errors handler.
 */
public interface ValidationErrorsHandler {

  void addError(String message);

  void throwIfHasErrors();
}
