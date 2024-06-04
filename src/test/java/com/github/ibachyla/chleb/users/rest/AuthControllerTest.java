package com.github.ibachyla.chleb.users.rest;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;

import com.github.ibachyla.chleb.ApiActions;
import com.github.ibachyla.chleb.ChlebTestConfiguration;
import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.rest.dto.GetTokenResponse;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequest;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@Import(ChlebTestConfiguration.class)
@ExtendWith(SoftAssertionsExtension.class)
final class AuthControllerTest {

  private static final String TOKEN_TYPE = "bearer";

  @InjectSoftAssertions
  SoftAssertions softly;

  @Autowired
  ApiActions apiActions;

  @Test
  void getToken_byEmail() {
    // Arrange
    String email = TestValues.email().value();
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        email,
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);
    apiActions.registerUser(body);

    // Act
    GetTokenResponse response = apiActions.getToken(email, password);

    // Assert
    softly.assertThat(response.accessToken()).isNotBlank();
    softly.assertThat(response.tokenType()).isEqualTo(TOKEN_TYPE);
  }

  @Test
  void getToken_byUsername() {
    // Arrange
    String username = TestValues.username().value();
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        username,
        TestValues.fullName(),
        password,
        password);
    apiActions.registerUser(body);

    // Act
    GetTokenResponse response = apiActions.getToken(username, password);

    // Assert
    softly.assertThat(response.accessToken()).isNotBlank();
    softly.assertThat(response.tokenType()).isEqualTo(TOKEN_TYPE);
  }

  @Test
  void getToken_invalidEmail() {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);
    apiActions.registerUser(body);

    // Act
    MockMvcResponse response = apiActions.raw().getToken("invalid@example.com", password);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(401)
        .extract()
        .as(ErrorResponse.class);
    softly.assertThat(responseBody.statusCode()).isEqualTo(401);
    softly.assertThat(responseBody.message()).isEqualTo("Invalid credentials");
  }

  @Test
  void getToken_invalidPassword() {
    // Arrange
    String email = TestValues.email().value();
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        email,
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);
    apiActions.registerUser(body);

    // Act
    MockMvcResponse response = apiActions.raw().getToken(email, "invalid".toCharArray());

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(401)
        .extract()
        .as(ErrorResponse.class);
    softly.assertThat(responseBody.statusCode()).isEqualTo(401);
    softly.assertThat(responseBody.message()).isEqualTo("Invalid credentials");
  }
}