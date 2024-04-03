package com.github.ibachyla.chleb.models.validators.constraints;

import com.github.ibachyla.chleb.models.validators.PasswordFormatValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates password constraints.
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, char[]> {

  @Override
  public void initialize(ValidPassword constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(char[] value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    try {
      PasswordFormatValidator.validate(value);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }
}
