package com.github.ibachyla.chleb.security;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;

import com.github.ibachyla.chleb.ChlebTestConfiguration;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

final class CorsTest {

  static final String ALLOWED_ORIGIN = "http://localhost:3333";
  static final String ENDPOINT = "/api/app/about";

  @Nested
  @SpringBootTest
  @AutoConfigureMockMvc
  @ActiveProfiles(TEST_PROFILE)
  @Import(ChlebTestConfiguration.class)
  @TestPropertySource(properties = "chleb.security.allowed-origins=" + ALLOWED_ORIGIN)
  final class AllowedOrigins {

    @Autowired
    MockMvcRequestSpecification reqSpec;

    @Test
    void allowedOrigins() {
      // Act & Assert
      RestAssuredMockMvc.given()
          .spec(reqSpec)
          .header(ORIGIN, ALLOWED_ORIGIN)
          .header(ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name())
          .header(ACCESS_CONTROL_REQUEST_HEADERS, CONTENT_TYPE)
          .when()
          .options(ENDPOINT)
          .then()
          .statusCode(HttpStatus.OK.value())
          .header(ACCESS_CONTROL_ALLOW_ORIGIN, ALLOWED_ORIGIN)
          .header(ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.GET.name())
          .header(ACCESS_CONTROL_ALLOW_HEADERS, CONTENT_TYPE)
          .header(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
          .header(ACCESS_CONTROL_MAX_AGE, "3600");
    }
  }

  @Nested
  @SpringBootTest
  @AutoConfigureMockMvc
  @ActiveProfiles(TEST_PROFILE)
  @Import(ChlebTestConfiguration.class)
  @TestPropertySource(properties = "chleb.security.allowed-origins=")
  final class AllowedOriginsNotSet {

    @Autowired
    MockMvcRequestSpecification reqSpec;

    @Test
    void allowedOriginsNotSet() {
      // Act & Assert
      RestAssuredMockMvc.given()
          .spec(reqSpec)
          .header(ORIGIN, ALLOWED_ORIGIN)
          .header(ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name())
          .header(ACCESS_CONTROL_REQUEST_HEADERS, CONTENT_TYPE)
          .when()
          .options(ENDPOINT)
          .then()
          .statusCode(HttpStatus.FORBIDDEN.value());
    }
  }
}
