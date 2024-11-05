package com.github.ibachyla.chleb.users.models.values;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Email value object.
 *
 * @param value email
 */
public record Email(String value) {

  private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

  public Email {
    value = validate(value);
  }

  private static String validate(String value) {
    notBlank(value, "value cannot be blank");

    value = value.trim();

    isTrue(EMAIL_VALIDATOR.isValid(value), "value must be a valid email address");

    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  public boolean matches(String value) {
    return this.value.equals(value);
  }
}
