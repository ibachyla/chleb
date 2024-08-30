package com.github.ibachyla.chleb;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test configuration for Chleb application.
 */
@TestConfiguration
public class ChlebTestConfiguration {

  /**
   * Retrieves the OpenAPI specification.
   *
   * @param reqSpec RestAssured request specification
   * @return OpenAPI specification
   */
  @Bean
  public String apiSpec(MockMvcRequestSpecification reqSpec) {
    return given()
        .spec(reqSpec)
        .when()
        .get("/api-docs.yaml")
        .then()
        .statusCode(200)
        .extract()
        .asString();
  }

  /**
   * Creates an OpenAPI validator.
   *
   * @param apiSpec OpenAPI specification
   * @return OpenAPI validator
   */
  @Bean
  public OpenApiInteractionValidator defaultOpenApiValidator(String apiSpec) {
    return OpenApiInteractionValidator
        .createForInlineApiSpecification(apiSpec)
        .build();
  }

  /**
   * Creates an OpenAPI validator that ignores request validation.
   *
   * @param apiSpec OpenAPI specification
   * @return OpenAPI validator
   */
  @Bean
  public OpenApiInteractionValidator ignoringRequestOpenApiValidator(String apiSpec) {
    return OpenApiInteractionValidator
        .createForInlineApiSpecification(apiSpec)
        .withLevelResolver(LevelResolver.create()
            .withLevel("validation.request", ValidationReport.Level.IGNORE)
            .build())
        .build();
  }

  /**
   * Creates a request specification for RestAssured.
   *
   * @param mockMvc Spring MockMvc object
   * @return request specification
   */
  @Bean
  public MockMvcRequestSpecification restAssuredReqSpec(MockMvc mockMvc) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(NON_NULL);

    RestAssuredMockMvcConfig config = RestAssuredMockMvcConfig.config()
        .objectMapperConfig(
            objectMapperConfig().jackson2ObjectMapperFactory((cls, s) -> mapper));

    return new MockMvcRequestSpecBuilder()
        .setConfig(config)
        .setContentType(APPLICATION_JSON.toString())
        .setMockMvc(mockMvc)
        .build();
  }

  /**
   * Creates an API actions object.
   *
   * @param reqSpec RestAssured request specification
   * @return API actions
   */
  @Bean
  public ApiActions apiActions(MockMvcRequestSpecification reqSpec,
                               TokenProvider tokenProvider,
                               OpenApiInteractionValidator defaultOpenApiValidator,
                               OpenApiInteractionValidator ignoringRequestOpenApiValidator) {
    return new ApiActions(reqSpec,
        tokenProvider,
        defaultOpenApiValidator,
        ignoringRequestOpenApiValidator);
  }

  @Bean
  public CredentialsProvider userPool(MockMvcRequestSpecification reqSpec) {
    return new CredentialsProvider(reqSpec);
  }

  @Bean
  public TokenProvider tokenProvider(CredentialsProvider credentialsProvider,
                                     MockMvcRequestSpecification reqSpec) {
    return new TokenProvider(credentialsProvider, reqSpec);
  }
}
