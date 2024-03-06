package com.github.ibachyla.chleb.recipes.services;

import com.github.ibachyla.chleb.recipes.models.entities.Recipe;

/**
 * Recipe service.
 */
public interface RecipeService {

  Recipe createRecipe(Recipe recipe);

  Recipe getRecipe(String slug);
}
