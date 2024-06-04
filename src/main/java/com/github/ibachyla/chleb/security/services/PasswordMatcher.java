package com.github.ibachyla.chleb.security.services;

/**
 * Implementations of this interface are responsible for matching passwords.
 */
public interface PasswordMatcher {

  /**
   * Matches the given password with the encoded password.
   *
   * @param password        the password to match
   * @param encodedPassword the encoded password
   * @return {@code true} if the password matches the encoded password, {@code false} otherwise
   */
  boolean matches(char[] password, String encodedPassword);
}
