package com.github.ibachyla.chleb.recipes.rest;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.github.ibachyla.chleb.ChlebTestConfiguration;
import com.github.ibachyla.chleb.recipes.TestEntitiesFactory;
import com.github.ibachyla.chleb.recipes.models.entities.Recipe;
import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import com.github.ibachyla.chleb.recipes.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import com.github.ibachyla.chleb.recipes.services.RecipeService;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@Import(ChlebTestConfiguration.class)
final class RecipeControllerTest {

  static final String ENDPOINT = "/api/recipes";

  static final String RECIPE_NAME = TestEntitiesFactory.recipeName().value();
  static final CreateRecipeRequest BODY = new CreateRecipeRequest(RECIPE_NAME);

  @Autowired
  MockMvcRequestSpecification reqSpec;

  @SpyBean
  RecipeService recipeService;

  @Test
  void createRecipe() {
    // Act
    MockMvcResponse response = reqSpec
        .contentType(APPLICATION_JSON)
        .body(BODY)
        .post(ENDPOINT);

    // Assert
    response.then()
        .statusCode(HttpStatus.CREATED.value())
        .body(startsWith("potato-bread-"));
  }

  @Test
  void createRecipe_internalServerError() {
    // Arrange
    doThrow(new RuntimeException("Unexpected error")).when(recipeService)
        .createRecipe(any(Recipe.class));

    // Act
    MockMvcResponse response = reqSpec
        .contentType(APPLICATION_JSON)
        .body(BODY)
        .post(ENDPOINT);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .extract()
        .body()
        .as(ErrorResponse.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.statusCode());
    assertEquals("Unexpected error", responseBody.message());
  }

  @Test
  void createRecipe_withEmptyName() {
    // Arrange
    CreateRecipeRequest body = new CreateRecipeRequest("");

    // Act
    MockMvcResponse response = reqSpec
        .contentType(APPLICATION_JSON)
        .body(body)
        .post(ENDPOINT);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
        .extract()
        .body()
        .as(ErrorResponse.class);
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseBody.statusCode());
    assertEquals("Validation for field `name` failed. Reason: name is required.",
        responseBody.message());
  }

  @Test
  void getRecipe_bySlug() {
    // Arrange
    Recipe recipe = TestEntitiesFactory.recipe();
    recipeService.createRecipe(recipe);

    // Act
    MockMvcResponse response = reqSpec
        .get(ENDPOINT + "/" + recipe.slug().value());

    // Assert
    GetRecipeResponse responseBody = response.then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body()
        .as(GetRecipeResponse.class);
    assertEquals(recipe.slug().value(), responseBody.slug());
    assertEquals(recipe.id().toString(), responseBody.id());
  }

  @Test
  void getRecipe_byId() {
    // Arrange
    Recipe recipe = TestEntitiesFactory.recipe();
    recipeService.createRecipe(recipe);

    // Act
    MockMvcResponse response = reqSpec
        .get(ENDPOINT + "/" + recipe.id());

    // Assert
    GetRecipeResponse responseBody = response.then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body()
        .as(GetRecipeResponse.class);
    assertEquals(recipe.slug().value(), responseBody.slug());
    assertEquals(recipe.id().toString(), responseBody.id());
  }

  @Test
  void getRecipe_notFound() {
    // Act
    MockMvcResponse response = reqSpec
        .get(ENDPOINT + "/not-found");

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .extract()
        .body()
        .as(ErrorResponse.class);
    assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.statusCode());
    assertEquals("Recipe with slug or id not-found not found", responseBody.message());
  }
}
