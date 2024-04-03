package com.github.ibachyla.chleb.recipes.data.entities;

import com.github.ibachyla.chleb.data.entities.IdentifiedEntity;
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
public class RecipeEntity extends IdentifiedEntity {

  @Column(nullable = false, columnDefinition = "text")
  private String name;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(nullable = false)
  private long createdAt;

  @Column(nullable = false)
  private long updatedAt;
}
