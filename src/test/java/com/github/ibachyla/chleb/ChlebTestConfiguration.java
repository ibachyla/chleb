package com.github.ibachyla.chleb;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.filter.log.LogDetail;
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
        .log(LogDetail.ALL)
        .build();
  }

  /**
   * Creates an API actions object.
   *
   * @param reqSpec RestAssured request specification
   * @return API actions
   */
  @Bean
  public ApiActions apiActions(MockMvcRequestSpecification reqSpec, TokenProvider tokenProvider) {
    return new ApiActions(reqSpec, tokenProvider);
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
