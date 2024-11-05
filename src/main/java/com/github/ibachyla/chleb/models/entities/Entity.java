package com.github.ibachyla.chleb.models.entities;

import static java.util.UUID.randomUUID;

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
    this(null);
  }

  public Entity(UUID id) {
    id(id);
  }

  private void id(UUID id) {
    this.id = id != null ? id : randomUUID();
  }
}
