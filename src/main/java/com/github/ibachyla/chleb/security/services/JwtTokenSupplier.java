package com.github.ibachyla.chleb.security.services;

import com.github.ibachyla.chleb.users.models.entities.User;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Implementations of this interface are responsible for supplying JWT tokens.
 */
public interface JwtTokenSupplier {

  /**
   * Provides a JWT token for the given user.
   *
   * @param user the user to create the token for
   * @return the JWT token
   */
  String supply(User user);

  String refresh(Jwt token);
}
