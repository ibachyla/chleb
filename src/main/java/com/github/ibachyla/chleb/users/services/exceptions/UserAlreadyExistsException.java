package com.github.ibachyla.chleb.users.services.exceptions;

/**
 * Exception thrown while trying to register a user that is already registered.
 */
public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
