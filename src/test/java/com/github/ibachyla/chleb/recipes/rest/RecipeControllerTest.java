package com.github.ibachyla.chleb.recipes.rest;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.ibachyla.chleb.ApiActions;
import com.github.ibachyla.chleb.ChlebTestConfiguration;
import com.github.ibachyla.chleb.recipes.TestValues;
import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import com.github.ibachyla.chleb.recipes.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@Import(ChlebTestConfiguration.class)
final class RecipeControllerTest {

  static final String RECIPE_NAME = TestValues.recipeName().value();
  static final CreateRecipeRequest BODY = new CreateRecipeRequest(RECIPE_NAME);

  @Autowired
  ApiActions apiActions;

  @Test
  void createRecipe() {
    // Act
    String response = apiActions.createRecipe(BODY);

    // Assert
    assertThat(response).startsWith("potato-bread-");
  }

  @Test
  void createRecipe_withEmptyName() {
    // Arrange
    CreateRecipeRequest body = new CreateRecipeRequest("");

    // Act
    MockMvcResponse response = apiActions.raw().createRecipe(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
        .extract()
        .body()
        .as(ErrorResponse.class);
    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `name` failed. Reason(s): name is required.");
  }

  @Test
  void getRecipe_bySlug() {
    // Arrange
    String slug = apiActions.createRecipe(BODY);

    // Act
    GetRecipeResponse response = apiActions.getRecipe(slug);

    // Assert
    assertThat(response.slug()).isEqualTo(slug);
  }

  @Test
  void getRecipe_byId() {
    // Arrange
    String slug = apiActions.createRecipe(BODY);
    String id = apiActions.getRecipe(slug).id();

    // Act
    GetRecipeResponse response = apiActions.getRecipe(id);

    // Assert
    assertThat(response.slug()).isEqualTo(slug);
    assertThat(response.id()).isEqualTo(id);
  }

  @Test
  void getRecipe_notFound() {
    // Act
    MockMvcResponse response = apiActions.raw().getRecipe("not-found");

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .extract()
        .body()
        .as(ErrorResponse.class);
    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(responseBody.message()).isEqualTo("Recipe with slug or id not-found not found");
  }
}
