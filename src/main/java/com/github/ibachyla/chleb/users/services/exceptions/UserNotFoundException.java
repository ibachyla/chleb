package com.github.ibachyla.chleb.users.services.exceptions;

/**
 * Exception thrown when user with given username or email is not found.
 */
public class UserNotFoundException extends AuthenticationException {

  public UserNotFoundException() {
    super("User not found");
  }
}
