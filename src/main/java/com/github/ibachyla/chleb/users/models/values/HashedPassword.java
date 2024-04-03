package com.github.ibachyla.chleb.users.models.values;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notNull;

import com.github.ibachyla.chleb.models.validators.PasswordFormatValidator;
import com.github.ibachyla.chleb.security.services.PasswordEncoder;

/**
 * Hashed password value object.
 *
 * @param value hashed password
 */
public record HashedPassword(String value) {

  public static final int MIN_RAW_LENGTH = 8;
  public static final int MAX_RAW_LENGTH = 64;

  public HashedPassword(char[] rawPassword, PasswordEncoder passwordEncoder) {
    this(passwordEncoder.encode(validate(rawPassword)));
  }

  @Override
  public String toString() {
    return "********";
  }

  private static char[] validate(char[] value) {
    notNull(value, "value cannot be null");
    inclusiveBetween(MIN_RAW_LENGTH, MAX_RAW_LENGTH, value.length,
        "value must be between %d and %d characters long", MIN_RAW_LENGTH, MAX_RAW_LENGTH);
    PasswordFormatValidator.validate(value);

    return value;
  }
}
