package com.github.ibachyla.chleb;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequestDto;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponseDto;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.apache.http.HttpStatus;

/**
 * Aggregates actions that can be performed on the API.
 */
public class ApiActions {

  private static final String API = "/api";

  private final MockMvcRequestSpecification reqSpec;

  private final Raw raw = new Raw();

  public ApiActions(MockMvcRequestSpecification reqSpec) {
    this.reqSpec = reqSpec;
  }

  /**
   * Get raw API actions.
   * <p>These methods do not perform any assertions on the response and return it as is.
   * Could be used in negative test cases where the response is expected to be an error.</p>
   *
   * @return raw API actions
   */
  public Raw raw() {
    return raw;
  }

  /**
   * Register a user.
   *
   * @param body user details
   * @return response with user details
   */
  public RegisterUserResponseDto registerUser(RegisterUserRequestDto body) {
    return raw().registerUser(body)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .contentType(APPLICATION_JSON.getType())
        .extract()
        .as(RegisterUserResponseDto.class);
  }

  /**
   * Create a recipe.
   *
   * @param body recipe details
   * @return response with recipe details
   */
  public String createRecipe(CreateRecipeRequest body) {
    return raw().createRecipe(body)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract()
        .asString();
  }

  /**
   * Get a recipe.
   *
   * @param slugOrId recipe slug or ID
   * @return response with recipe details
   */
  public GetRecipeResponse getRecipe(String slugOrId) {
    return raw().getRecipe(slugOrId)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .as(GetRecipeResponse.class);
  }

  /**
   * Raw API actions.
   * <p>Contains methods that do not perform any assertions on the response and return it as is.</p>
   */
  public class Raw {

    /**
     * Register a user.
     *
     * @param body user details
     * @return response
     */
    public MockMvcResponse registerUser(RegisterUserRequestDto body) {
      return reqSpec
          .body(body)
          .post(API + "/users/register");
    }

    /**
     * Create a recipe.
     *
     * @param body recipe details
     * @return response
     */
    public MockMvcResponse createRecipe(CreateRecipeRequest body) {
      return reqSpec
          .contentType(APPLICATION_JSON)
          .body(body)
          .post(API + "/recipes");
    }

    /**
     * Get a recipe.
     *
     * @param slugOrId recipe slug or ID
     * @return response
     */
    public MockMvcResponse getRecipe(String slugOrId) {
      return reqSpec.get(API + "/recipes/" + slugOrId);
    }
  }
}
