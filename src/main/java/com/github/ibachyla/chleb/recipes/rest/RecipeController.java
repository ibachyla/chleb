package com.github.ibachyla.chleb.recipes.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.ibachyla.chleb.recipes.mappers.Mapper;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import com.github.ibachyla.chleb.recipes.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import com.github.ibachyla.chleb.recipes.services.RecipeService;
import com.github.ibachyla.chleb.recipes.services.exceptions.RecipeNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recipe controller.
 */
@Slf4j
@Tag(name = "Recipe: CRUD")
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

  private final RecipeService recipeService;
  private final Mapper<CreateRecipeRequest, Recipe> recipeFromCreateDtoMapper;
  private final Mapper<Recipe, GetRecipeResponse> recipeToGetDtoMapper;

  /**
   * Create a recipe.
   *
   * @param body request body
   * @return slug of the created recipe
   */
  @ApiResponse(responseCode = "201", description = "Successful Response")
  @Operation(summary = "Create One",
      description = "Takes in a JSON string and loads data into the database as a new entry")
  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public String createRecipe(@Valid @RequestBody CreateRecipeRequest body) {
    log.info("Creating recipe with name: {}", body.name());

    Recipe recipe = recipeService.createRecipe(recipeFromCreateDtoMapper.map(body));

    log.info("Created recipe: {}", recipe);

    return String.valueOf(recipe.slug());
  }

  /**
   * Get a recipe.
   *
   * @param slugOrId recipe's slug or id
   * @return recipe data
   */
  @ApiResponse(responseCode = "200", description = "Successful Response")
  @GetMapping(path = "/{slugOrId}", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get One",
      description = "Takes in a recipe's slug or id and returns all data for a recipe")
  public GetRecipeResponse getRecipe(
      @Parameter(description = "A recipe's slug or id") @PathVariable String slugOrId) {
    log.info("Getting recipe with slug or id: {}", slugOrId);

    Recipe recipe = recipeService.getRecipe(slugOrId);

    log.info("Got recipe: {}", recipe);

    return recipeToGetDtoMapper.map(recipe);
  }

  @ApiResponse(responseCode = "404",
      description = "Not Found",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class))
  )
  @ExceptionHandler(RecipeNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleRecipeNotFoundException(RecipeNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
  }
}
