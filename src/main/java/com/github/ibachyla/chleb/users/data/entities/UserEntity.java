package com.github.ibachyla.chleb.users.data.entities;

import com.github.ibachyla.chleb.data.entities.IdentifiedEntity;
import com.github.ibachyla.chleb.groups.data.entities.GroupEntity;
import com.github.ibachyla.chleb.users.models.values.Role;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Persisted user entity.
 */
@Getter
@Setter
@Entity
@Table(name = "users")
@SuppressFBWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class UserEntity extends IdentifiedEntity {

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true, length = 20)
  private String username;

  @Column(nullable = false, columnDefinition = "text")
  private String fullName;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Role role;

  @ManyToOne(fetch = FetchType.LAZY)
  private GroupEntity group;
}
