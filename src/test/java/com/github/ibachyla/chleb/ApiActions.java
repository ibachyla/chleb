package com.github.ibachyla.chleb;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import com.github.ibachyla.chleb.users.rest.dto.GetTokenResponse;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequest;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponse;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.apache.http.HttpStatus;

/**
 * Aggregates actions that can be performed on the API.
 */
public class ApiActions {

  private static final String API = "/api";

  private final MockMvcRequestSpecification reqSpec;
  private final TokenProvider tokenProvider;

  private final Raw raw = new Raw();

  public ApiActions(MockMvcRequestSpecification reqSpec, TokenProvider tokenProvider) {
    this.reqSpec = reqSpec;
    this.tokenProvider = tokenProvider;
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
   * Get an instance of API actions with authentication.
   * <p>Each request performed using returned instance will be extended with the Authorization
   * header specifying valid token for a default user.</p>
   *
   * @return API actions with authentication
   */
  public ApiActions auth() {
    MockMvcRequestSpecification reqSpecWithAuth = new MockMvcRequestSpecBuilder()
        .addMockMvcRequestSpecification(reqSpec)
        .addHeader(AUTHORIZATION, tokenProvider.get())
        .build();
    return new ApiActions(reqSpecWithAuth, tokenProvider);
  }

  /**
   * Get an instance of API actions with authentication.
   * <p>Each request performed using returned instance will be extended with the Authorization
   * header specifying the provided token.</p>
   *
   * @param token token
   * @return API actions with authentication
   */
  public ApiActions auth(String token) {
    MockMvcRequestSpecification reqSpecWithAuth = new MockMvcRequestSpecBuilder()
        .addMockMvcRequestSpecification(reqSpec)
        .addHeader(AUTHORIZATION, "Bearer " + token)
        .build();
    return new ApiActions(reqSpecWithAuth, tokenProvider);
  }

  /**
   * Register a user.
   *
   * @param body user details
   * @return response with user details
   */
  public RegisterUserResponse registerUser(RegisterUserRequest body) {
    return raw().registerUser(body)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .contentType(APPLICATION_JSON.getType())
        .extract()
        .as(RegisterUserResponse.class);
  }

  /**
   * Get a token.
   *
   * @param username username
   * @param password password
   * @return response with token
   */
  public GetTokenResponse getToken(String username, char[] password) {
    return raw().getToken(username, password)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .contentType(APPLICATION_JSON.getType())
        .extract()
        .as(GetTokenResponse.class);
  }

  /**
   * Refresh the JWT token.
   *
   * <p>Requires a valid token to be present in the Authorization header.</p>
   *
   * @return response with new token
   */
  public GetTokenResponse refreshToken() {
    return raw().refreshToken()
        .then()
        .statusCode(HttpStatus.SC_OK)
        .contentType(APPLICATION_JSON.getType())
        .extract()
        .as(GetTokenResponse.class);
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
    public MockMvcResponse registerUser(RegisterUserRequest body) {
      return given()
          .spec(reqSpec)
          .contentType(APPLICATION_JSON)
          .body(body)
          .when()
          .post(API + "/users/register");
    }

    /**
     * Get a token.
     *
     * @param username username
     * @param password password
     * @return response
     */
    public MockMvcResponse getToken(String username, char[] password) {
      return given()
          .spec(reqSpec)
          .contentType(APPLICATION_FORM_URLENCODED)
          .formParam("username", username)
          .formParam("password", new String(password))
          .when()
          .post(API + "/auth/token");
    }

    /**
     * Refresh the JWT token.
     *
     * <p>Requires a valid token to be present in the Authorization header.</p>
     *
     * @return response with new token
     */
    public MockMvcResponse refreshToken() {
      return given()
          .spec(reqSpec)
          .when()
          .get(API + "/auth/refresh");
    }

    /**
     * Create a recipe.
     *
     * @param body recipe details
     * @return response
     */
    public MockMvcResponse createRecipe(CreateRecipeRequest body) {
      return given()
          .spec(reqSpec)
          .contentType(APPLICATION_JSON)
          .body(body)
          .when()
          .post(API + "/recipes");
    }

    /**
     * Get a recipe.
     *
     * @param slugOrId recipe slug or ID
     * @return response
     */
    public MockMvcResponse getRecipe(String slugOrId) {
      return given()
          .spec(reqSpec)
          .when()
          .get(API + "/recipes/" + slugOrId);
    }
  }
}
