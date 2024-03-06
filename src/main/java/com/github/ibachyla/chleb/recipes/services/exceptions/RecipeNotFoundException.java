package com.github.ibachyla.chleb.recipes.services.exceptions;

/**
 * Exception thrown when a recipe is not found.
 */
public class RecipeNotFoundException extends RuntimeException {

  public RecipeNotFoundException(String slugOrId) {
    super("Recipe with slug or id " + slugOrId + " not found");
  }
}
