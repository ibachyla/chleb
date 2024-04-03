package com.github.ibachyla.chleb.data.entities;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for entities that have an id.
 */
@Getter
@Setter
@MappedSuperclass
public class IdentifiedEntity {

  @Id
  private UUID id;
}
