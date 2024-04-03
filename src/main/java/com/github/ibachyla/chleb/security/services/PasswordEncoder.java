package com.github.ibachyla.chleb.security.services;

/**
 * Encodes a password.
 */
public interface PasswordEncoder {

  String encode(char[] password);
}
