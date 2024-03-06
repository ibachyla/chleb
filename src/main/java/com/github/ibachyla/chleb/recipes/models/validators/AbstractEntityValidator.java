package com.github.ibachyla.chleb.recipes.models.validators;

/**
 * Abstract entity validator.
 *
 * @param <T> Entity type.
 */
public abstract class AbstractEntityValidator<T> implements EntityValidator<T> {

  @Override
  public void validate(T entity) {
    validate(entity, new ValidationErrorsHandlerImpl());
  }

  protected void validate(T entity, ValidationErrorsHandler errorsHandler) {
    performValidation(entity, errorsHandler);
    errorsHandler.throwIfHasErrors();
  }

  protected abstract void performValidation(T entity, ValidationErrorsHandler errorsHandler);
}
