package com.github.ibachyla.chleb.models.entities;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Base class for entities.
 */
@Getter
@EqualsAndHashCode
public class Entity {

  private UUID id;

  public Entity() {
    this(randomUUID());
  }

  public Entity(UUID id) {
    id(id);
  }

  private void id(UUID id) {
    notNull(id, "id cannot be null");

    this.id = id;
  }
}
