package com.github.ibachyla.chleb.users.services.exceptions;

/**
 * Generic exception for authentication errors.
 */
public class AuthenticationException extends RuntimeException {

  public AuthenticationException(String message) {
    super(message);
  }
}
