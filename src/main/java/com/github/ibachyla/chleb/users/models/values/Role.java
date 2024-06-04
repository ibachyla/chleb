package com.github.ibachyla.chleb.users.models.values;

import lombok.Getter;

/**
 * The role a user can have.
 */
@Getter
public enum Role {
  USER("USER");

  private final String value;

  Role(String value) {
    this.value = value;
  }
}
