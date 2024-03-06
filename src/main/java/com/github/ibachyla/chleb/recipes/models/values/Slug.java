package com.github.ibachyla.chleb.recipes.models.values;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Slug value object.
 *
 * @param value Slug value.
 */
public record Slug(String value) {

  private static final int MAX_LENGTH = 255;
  private static final Pattern NONLATIN = Pattern.compile("[^\\w_-]");
  private static final Pattern SEPARATORS = Pattern.compile("[\\s\\p{Punct}&&[^-]]");

  public Slug {
    value = validate(value);
  }

  private static String validate(String value) {
    notNull(value, "value cannot be null");
    notBlank(value, "value cannot be blank");
    isTrue(value.length() <= MAX_LENGTH,
        "value cannot be longer than %d characters", MAX_LENGTH);
    matchesPattern(value,
        "^[a-z0-9]+(-[a-z0-9]+)*$",
        "value must contain only lowercase alphanumeric characters and hyphens, and cannot"
            + " start or end with a hyphen");

    return value;
  }

  /**
   * Create a slug from a raw value.
   * <p>Example: "Hello, World!" -> "{@code hello-world-<uuid>}"</p>
   *
   * @param rawValue Raw value.
   * @return Slug.
   */
  public static Slug from(String rawValue) {
    notNull(rawValue, "value cannot be null");

    String value = SEPARATORS.matcher(rawValue).replaceAll("-");
    value = Normalizer.normalize(value, Normalizer.Form.NFD);
    value = NONLATIN.matcher(value).replaceAll("");
    value = value.toLowerCase(Locale.ENGLISH)
        .replaceAll("-{2,}", "-")
        .replaceAll("^-|-$", "");

    if (value.length() > 218) {
      value = value.substring(0, 218);
    }

    isTrue(!value.isBlank(), "value cannot be blank");

    value = value + "-" + randomUUID();

    return new Slug(value);
  }

  public String readablePart() {
    return value.substring(0, value.length() - 37);
  }

  @Override
  public String toString() {
    return value;
  }
}
