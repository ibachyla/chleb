package com.github.ibachyla.chleb.models.validators.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that the password and password confirmation match.
 * As an addition to the standard Jakarta Bean Validation annotation attributes, this annotation
 * requires two additional attributes:
 * <ul>
 * <li>passwordGetter: the name of the getter method for the password field</li>
 * <li>passwordConfirmGetter: the name of the getter method for the password confirmation field</li>
 * </ul>
 * <p>The getter methods must be public and have no parameters.<br>
 * The getter methods must be present in the class that is being validated.<br>
 * The getter methods must return char arrays.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

  /**
   * The message to be shown if the validation fails.
   *
   * @return the message
   */
  String message() default "The passwords must match";

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

  /**
   * The name of the getter method for the password field.
   *
   * @return the getter method name
   */
  String passwordGetter();

  /**
   * The name of the getter method for the password confirmation field.
   *
   * @return the getter method name
   */
  String passwordConfirmGetter();
}
