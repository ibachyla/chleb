package com.github.ibachyla.chleb.recipes.models.values;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Recipe name value object.
 *
 * @param value Name value.
 */
public record RecipeName(String value) {

  public RecipeName {
    value = validate(value);
  }

  private static String validate(String value) {
    notNull(value, "value cannot be null");
    notBlank(value, "value cannot be blank");

    return value.trim();
  }
}
