package com.github.ibachyla.chleb.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.util.Objects;
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

  @Version
  @Column(nullable = false)
  private Long version;

  @SuppressWarnings("EqualsGetClass")
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    IdentifiedEntity that = (IdentifiedEntity) o;
    if (id == null) {
      return false;
    }
    return Objects.equals(id, that.id) && Objects.equals(version, that.version);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(id);
    return 31 * result + Objects.hashCode(version);
  }
}
