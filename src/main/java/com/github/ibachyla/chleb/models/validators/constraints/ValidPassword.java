package com.github.ibachyla.chleb.models.validators.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that the password has a valid format.
 */
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

  /**
   * The message to be shown if the validation fails.
   *
   * @return the message
   */
  String message() default "Password has invalid format";

  /**
   * The groups that the constraint belongs to.
   *
   * @return the groups
   */
  Class<?>[] groups() default {};

  /**
   * The payload associated to the constraint.
   *
   * @return the payload
   */
  Class<? extends Payload>[] payload() default {};
}
