package com.github.ibachyla.chleb.models.validators;

/**
 * Validation errors handler implementation.
 */
public class ValidationErrorsHandlerImpl implements ValidationErrorsHandler {

  private final StringBuilder errors = new StringBuilder();

  @Override
  public void addError(String message) {
    errors.append(message).append("\n");
  }

  @Override
  public void throwIfHasErrors() {
    if (!errors.isEmpty()) {
      throw new ValidationException(errors.toString());
    }
  }
}
