package com.github.ibachyla.chleb.models.validators;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Password format validator.
 */
public class PasswordFormatValidator {

  /**
   * Validates the password format.
   * <p>Throws an {@link IllegalArgumentException} if the password does not meet the requirements.
   * </p>
   *
   * @param password the password to validate
   */
  public static void validate(char[] password) {
    isTrue(containsDigit(password), "value must contain at least one digit");
    isTrue(containsLowercase(password), "value must contain at least one lowercase letter");
    isTrue(containsUppercase(password), "value must contain at least one uppercase letter");
    isTrue(containsSpecialCharacter(password), "value must contain at least one special character");
    isTrue(noWhitespace(password), "value must not contain any whitespace characters");
  }

  private static boolean containsDigit(char[] value) {
    for (char c : value) {
      if (Character.isDigit(c)) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsLowercase(char[] value) {
    for (char c : value) {
      if (Character.isLowerCase(c)) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsUppercase(char[] value) {
    for (char c : value) {
      if (Character.isUpperCase(c)) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsSpecialCharacter(char[] value) {
    for (char c : value) {
      if (!Character.isLetterOrDigit(c)) {
        return true;
      }
    }
    return false;
  }

  private static boolean noWhitespace(char[] value) {
    for (char c : value) {
      if (Character.isWhitespace(c)) {
        return false;
      }
    }
    return true;
  }
}
