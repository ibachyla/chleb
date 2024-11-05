package com.github.ibachyla.chleb.users;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Users properties.
 */
@Validated
@ConfigurationProperties(prefix = "chleb.users")
public record UsersProperties(String defaultEmail, String defaultPassword) {

  private static final String DEFAULT_USERNAME = "admin";
  private static final String DEFAULT_FULL_NAME = "Change Me";

  public String defaultUsername() {
    return DEFAULT_USERNAME;
  }

  public String defaultFullName() {
    return DEFAULT_FULL_NAME;
  }
}
