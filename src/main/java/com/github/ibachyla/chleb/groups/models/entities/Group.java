package com.github.ibachyla.chleb.groups.models.entities;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

import com.github.ibachyla.chleb.models.entities.Entity;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Group model entity.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Group extends Entity {

  private static final int MAX_NAME_LENGTH = 32;
  private static final int MIN_NAME_LENGTH = 3;

  private String name;

  public Group(String name) {
    name(name);
  }

  @Builder
  public Group(UUID id, String name) {
    super(id);
    name(name);
  }

  private void name(String name) {
    notBlank(name, "name cannot be blank");
    isTrue(name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH,
        "name length must be between %d and %d characters",
        MIN_NAME_LENGTH, MAX_NAME_LENGTH);

    this.name = name;
  }
}
