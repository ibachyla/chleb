package com.github.ibachyla.chleb.users.models.values;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Username value object.
 *
 * @param value username
 */
public record Username(String value) {

  public static final int MAX_LENGTH = 20;
  public static final int MIN_LENGTH = 3;
  public static final String PATTERN = "[A-Za-z0-9_.-]+";

  public Username {
    value = validate(value);
  }

  private static String validate(String value) {
    notNull(value, "value cannot be null");
    notBlank(value, "value cannot be blank");

    value = value.trim();

    inclusiveBetween(MIN_LENGTH, MAX_LENGTH, value.length(),
        "value must be between %d and %d characters long", MIN_LENGTH, MAX_LENGTH);
    matchesPattern(value, PATTERN,
        "value can only contain alphanumeric characters, underscores, hyphens and dots");

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
