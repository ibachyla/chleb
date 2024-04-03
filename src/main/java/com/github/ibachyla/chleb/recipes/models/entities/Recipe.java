package com.github.ibachyla.chleb.recipes.models.entities;

import static org.apache.commons.lang3.Validate.notNull;

import com.github.ibachyla.chleb.models.entities.Entity;
import com.github.ibachyla.chleb.recipes.models.values.RecipeName;
import com.github.ibachyla.chleb.recipes.models.values.Slug;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Recipe entity.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Recipe extends Entity {

  private RecipeName name;
  private Slug slug;
  private Instant createdAt;
  private Instant updatedAt;

  /**
   * Constructor.
   *
   * @param name      Recipe name.
   * @param slug      Recipe slug. Should match the name.
   * @param createdAt Creation date.
   */
  public Recipe(RecipeName name, Slug slug, Instant createdAt) {
    name(name);
    slug(slug);
    createdAt(createdAt);
    updatedAt(createdAt);
  }

  /**
   * Constructor.
   *
   * @param id        Recipe id.
   * @param name      Recipe name.
   * @param slug      Recipe slug. Should match the name.
   * @param createdAt Creation date.
   * @param updatedAt Last update date.
   */
  public Recipe(UUID id,
                RecipeName name,
                Slug slug,
                Instant createdAt,
                Instant updatedAt) {
    super(id);
    name(name);
    slug(slug);
    createdAt(createdAt);
    updatedAt(updatedAt);
  }

  /**
   * Set name.
   *
   * @param name Recipe name.
   */
  public void name(RecipeName name) {
    notNull(name, "name cannot be null");

    this.name = name;
  }

  /**
   * Set slug.
   *
   * @param slug Recipe slug. Should match the name.
   */
  public void slug(Slug slug) {
    notNull(slug, "slug cannot be null");

    this.slug = slug;
  }

  /**
   * Set creation date.
   *
   * @param createdAt Creation date.
   */
  public void createdAt(Instant createdAt) {
    notNull(createdAt, "createdAt cannot be null");

    this.createdAt = Instant.ofEpochSecond(createdAt.getEpochSecond());
  }

  /**
   * Set the last update date.
   *
   * @param updatedAt Last update date.
   */
  public void updatedAt(Instant updatedAt) {
    notNull(updatedAt, "updatedAt cannot be null");

    this.updatedAt = Instant.ofEpochSecond(updatedAt.getEpochSecond());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Recipe.class.getSimpleName() + "[", "]")
        .add("id=" + id())
        .add("name=" + name)
        .add("slug=" + slug)
        .add("createdAt=" + createdAt)
        .add("updatedAt=" + updatedAt)
        .toString();
  }
}
