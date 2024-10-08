package com.github.ibachyla.chleb.recipes.data.entities;

import com.github.ibachyla.chleb.data.entities.IdentifiedEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Recipe persistence entity.
 */
@Getter
@Setter
@Entity
@Table(name = "recipes")
@SuppressFBWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class RecipeEntity extends IdentifiedEntity {

  @Column(nullable = false, columnDefinition = "text")
  private String name;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(nullable = false)
  private Long createdAt;

  @Column(nullable = false)
  private Long updatedAt;
}
