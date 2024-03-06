package com.github.ibachyla.chleb.recipes.models.validators;

/**
 * Exception thrown when entity validation fails.
 */
public class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(message);
  }
}
