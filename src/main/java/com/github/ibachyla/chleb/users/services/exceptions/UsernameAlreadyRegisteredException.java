package com.github.ibachyla.chleb.users.services.exceptions;

/**
 * Exception thrown when a user with the same email already exists.
 */
public class UsernameAlreadyRegisteredException extends UserAlreadyExistsException {

  public UsernameAlreadyRegisteredException() {
    super("User with this username already exists.");
  }
}
