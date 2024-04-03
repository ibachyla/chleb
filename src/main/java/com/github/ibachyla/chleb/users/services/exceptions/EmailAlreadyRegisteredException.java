package com.github.ibachyla.chleb.users.services.exceptions;

/**
 * Exception thrown while trying to register a user with an email that is already registered.
 */
public class EmailAlreadyRegisteredException extends UserAlreadyExistsException {

  public EmailAlreadyRegisteredException() {
    super("User with this email already exists.");
  }
}
