package com.github.ibachyla.chleb.models.validators.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import lombok.SneakyThrows;

/**
 * Validates that the password and password confirmation match.
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

  private String passwordGetter;
  private String passwordConfirmGetter;

  @Override
  public void initialize(PasswordMatch constraintAnnotation) {
    passwordGetter = constraintAnnotation.passwordGetter();
    passwordConfirmGetter = constraintAnnotation.passwordConfirmGetter();
  }

  @SneakyThrows
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    char[] password = (char[]) value.getClass()
        .getMethod(passwordGetter)
        .invoke(value);
    char[] passwordConfirm = (char[]) value.getClass()
        .getMethod(passwordConfirmGetter)
        .invoke(value);

    return Arrays.equals(password, passwordConfirm);
  }
}
