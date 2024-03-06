package com.github.ibachyla.chleb.recipes.models.validators;

/**
 * Entity validator.
 *
 * @param <T> Entity type.
 */
public interface EntityValidator<T> {

  void validate(T entity);
}
