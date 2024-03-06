package com.github.ibachyla.chleb.recipes.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data transfer object for creating a recipe.
 *
 * @param name the name of the recipe
 */
public record CreateRecipeRequest(
    @NotBlank(message = "name is required")
    String name
) {
}
