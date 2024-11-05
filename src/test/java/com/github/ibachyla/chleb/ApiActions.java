package com.github.ibachyla.chleb;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.mockmvc.MockMvcRequest;
import com.atlassian.oai.validator.report.ValidationReport;
import com.github.ibachyla.chleb.app.rest.dto.GetAboutResponse;
import com.github.ibachyla.chleb.app.rest.dto.GetStartupInfoResponse;
import com.github.ibachyla.chleb.app.rest.dto.GetThemeResponse;
import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import com.github.ibachyla.chleb.recipes.rest.dto.GetRecipeResponse;
import com.github.ibachyla.chleb.users.rest.dto.GetTokenResponse;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequest;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponse;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.apache.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Aggregates actions that can be performed on the API.
 */
public class ApiActions {

  private static final String API = "/api";

  private final MockMvcRequestSpecification reqSpec;
  private final TokenProvider tokenProvider;
  private final OpenApiInteractionValidator defaultOpenApiValidator;
  private final OpenApiInteractionValidator ignoringRequestOpenApiValidator;

  /**
   * Constructor.
   *
   * @param reqSpec                         RestAssured request specification
   * @param tokenProvider                   token provider
   * @param defaultOpenApiValidator         default OpenAPI validator
   * @param ignoringRequestOpenApiValidator OpenAPI validator that ignores request validation
   */
  public ApiActions(MockMvcRequestSpecification reqSpec,
                    TokenProvider tokenProvider,
                    OpenApiInteractionValidator defaultOpenApiValidator,
                    OpenApiInteractionValidator ignoringRequestOpenApiValidator) {
    this.reqSpec = reqSpec;
    this.tokenProvider = tokenProvider;
    this.defaultOpenApiValidator = defaultOpenApiValidator;
    this.ignoringRequestOpenApiValidator = ignoringRequestOpenApiValidator;
  }

  /**
   * Get raw API actions.
   * <p>These methods do not perform any assertions on the response and return it as is.
   * Could be used in negative test cases where the response is expected to be an error.</p>
   *
   * @return raw API actions
   */
  public Raw raw() {
    return new Raw();
  }

  public Raw raw(boolean validateRequest) {
    return new Raw(validateRequest);
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
    return new ApiActions(reqSpecWithAuth,
        tokenProvider,
        defaultOpenApiValidator,
        ignoringRequestOpenApiValidator);
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
    return new ApiActions(reqSpecWithAuth,
        tokenProvider,
        defaultOpenApiValidator,
        ignoringRequestOpenApiValidator);
  }

  /**
   * Get general application information.
   *
   * @return response with application information
   */
  public GetAboutResponse getAbout() {
    return raw().getAbout()
        .then()
        .statusCode(HttpStatus.SC_OK)
        .contentType(APPLICATION_JSON.getType())
        .extract()
        .as(GetAboutResponse.class);
  }

  /**
   * Get startup information.
   *
   * @return response with startup information
   */
  public GetStartupInfoResponse getStartupInfo() {
    return raw().getStartupInfo()
        .then()
        .statusCode(HttpStatus.SC_OK)
        .contentType(APPLICATION_JSON.getType())
        .extract()
        .as(GetStartupInfoResponse.class);
  }

  /**
   * Get the current theme settings.
   *
   * @return response with theme settings
   */
  public GetThemeResponse getTheme() {
    return raw().getTheme()
        .then()
        .statusCode(HttpStatus.SC_OK)
        .contentType(APPLICATION_JSON.getType())
        .extract()
        .as(GetThemeResponse.class);
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

    private final boolean validateRequest;

    public Raw() {
      this.validateRequest = true;
    }

    public Raw(boolean validateRequest) {
      this.validateRequest = validateRequest;
    }

    /**
     * Get general application information.
     *
     * @return response
     */
    public MockMvcResponse getAbout() {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .when()
              .get(API + "/app/about")
      );
    }

    /**
     * Get startup information.
     *
     * @return response
     */
    public MockMvcResponse getStartupInfo() {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .when()
              .get(API + "/app/about/startup-info")
      );
    }

    /**
     * Get the current theme settings.
     *
     * @return response
     */
    public MockMvcResponse getTheme() {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .when()
              .get(API + "/app/about/theme")
      );
    }

    /**
     * Register a user.
     *
     * @param body user details
     * @return response
     */
    public MockMvcResponse registerUser(RegisterUserRequest body) {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .contentType(APPLICATION_JSON)
              .body(body)
              .when()
              .post(API + "/users/register")
      );
    }

    /**
     * Get a token.
     *
     * @param username username
     * @param password password
     * @return response
     */
    public MockMvcResponse getToken(String username, char[] password) {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .contentType(MULTIPART_FORM_DATA)
              .formParam("username", username)
              .formParam("password", new String(password))
              .when()
              .post(API + "/auth/token")
      );
    }

    /**
     * Refresh the JWT token.
     *
     * <p>Requires a valid token to be present in the Authorization header.</p>
     *
     * @return response with new token
     */
    public MockMvcResponse refreshToken() {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .when()
              .get(API + "/auth/refresh")
      );
    }

    /**
     * Create a recipe.
     *
     * @param body recipe details
     * @return response
     */
    public MockMvcResponse createRecipe(CreateRecipeRequest body) {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .contentType(APPLICATION_JSON)
              .body(body)
              .when()
              .post(API + "/recipes")
      );
    }

    /**
     * Get a recipe.
     *
     * @param slugOrId recipe slug or ID
     * @return response
     */
    public MockMvcResponse getRecipe(String slugOrId) {
      return validateSpecCompliance(
          given()
              .spec(reqSpec)
              .when()
              .get(API + "/recipes/" + slugOrId)
      );
    }

    private MockMvcResponse validateSpecCompliance(MockMvcResponse response) {
      MvcResult mvcResult = response.mvcResult();
      OpenApiInteractionValidator validator = validateRequest
          ? defaultOpenApiValidator
          : ignoringRequestOpenApiValidator;

      ValidationReport report = validator.validate(
          MockMvcRequest.of(mvcResult.getRequest()),
          com.atlassian.oai.validator.mockmvc.MockMvcResponse.of(mvcResult.getResponse())
      );

      if (report.hasErrors()) {
        String errorMessage = report.getMessages().stream()
            .map(ValidationReport.Message::getMessage)
            .reduce("", (acc, message) -> acc + message + "\n");
        throw new AssertionError(errorMessage);
      }

      return response;
    }
  }
}
