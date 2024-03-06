package com.github.ibachyla.chleb.recipes.rest.dto;

/**
 * Data transfer object for getting a recipe.
 *
 * @param id          the id of the recipe
 * @param name        the name of the recipe
 * @param slug        the slug of the recipe
 * @param dateAdded   the date the recipe was added
 * @param dateUpdated the date the recipe was updated
 * @param createdAt   the date the recipe was created
 * @param updatedAt   the date the recipe was updated
 */
public record GetRecipeResponse(String id,
                                String name,
                                String slug,
                                String dateAdded,
                                String dateUpdated,
                                String createdAt,
                                String updatedAt) {
}
