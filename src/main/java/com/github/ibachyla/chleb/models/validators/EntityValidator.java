package com.github.ibachyla.chleb.models.validators;

import com.github.ibachyla.chleb.models.entities.Entity;

/**
 * Entity validator.
 *
 * @param <T> Entity type.
 */
public interface EntityValidator<T extends Entity> {

  void validate(T entity);
}
