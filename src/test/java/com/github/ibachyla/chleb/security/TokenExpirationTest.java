package com.github.ibachyla.chleb.security;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;
import static java.time.Duration.ZERO;

import com.github.ibachyla.chleb.ApiActions;
import com.github.ibachyla.chleb.ChlebTestConfiguration;
import com.github.ibachyla.chleb.recipes.rest.dto.CreateRecipeRequest;
import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequest;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import java.time.Duration;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@Import(ChlebTestConfiguration.class)
@ExtendWith(SoftAssertionsExtension.class)
@TestPropertySource(properties = {
    "chleb.security.token-expiration-time=1",
    "chleb.security.token-expiration-unit=micros"
})
final class TokenExpirationTest {

  @InjectSoftAssertions
  SoftAssertions softly;

  @Autowired
  ApiActions apiActions;

  @TestConfiguration
  static class TokenExpirationTestConfig {

    @Bean
    public Duration clockSkew() {
      return ZERO;
    }
  }

  @Test
  void expiredToken() {
    // Arrange
    String email = TestValues.email().value();
    char[] password = TestValues.password();
    RegisterUserRequest registerUserBody = new RegisterUserRequest(
        email,
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);
    apiActions.registerUser(registerUserBody);

    String token = apiActions.getToken(email, password).accessToken();

    CreateRecipeRequest createRecipeBody = new CreateRecipeRequest("test");

    //Act
    MockMvcResponse response = apiActions.auth(token).raw().createRecipe(createRecipeBody);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .contentType(ContentType.JSON)
        .extract()
        .body()
        .as(ErrorResponse.class);
    softly.assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    softly.assertThat(responseBody.message()).isEqualTo("Unauthorized");
  }
}