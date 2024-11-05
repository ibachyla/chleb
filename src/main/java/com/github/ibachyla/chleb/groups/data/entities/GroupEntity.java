package com.github.ibachyla.chleb.groups.data.entities;

import com.github.ibachyla.chleb.data.entities.IdentifiedEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Group entity.
 */
@Getter
@Setter
@Entity
@Table(name = "groups")
@SuppressFBWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class GroupEntity extends IdentifiedEntity {

  @Column(nullable = false, unique = true, length = 32)
  private String name;
}
