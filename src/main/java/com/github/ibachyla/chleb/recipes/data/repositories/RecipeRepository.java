package com.github.ibachyla.chleb.recipes.data.repositories;

import com.github.ibachyla.chleb.recipes.data.entities.RecipeEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Recipe repository.
 */
@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, UUID> {

  Optional<RecipeEntity> findBySlugOrId(String slug, UUID id);
}
