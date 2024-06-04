package com.github.ibachyla.chleb.users.services.exceptions;

/**
 * Exception thrown when the password is invalid.
 */
public class PasswordInvalidException extends AuthenticationException {

  public PasswordInvalidException() {
    super("Password is invalid");
  }
}
