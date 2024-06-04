package com.github.ibachyla.chleb.users.rest;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.ibachyla.chleb.ApiActions;
import com.github.ibachyla.chleb.ChlebTestConfiguration;
import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequest;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponse;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
final class UserRegistrationControllerTest {

  @InjectSoftAssertions
  SoftAssertions softly;

  @Autowired
  ApiActions apiActions;

  @Test
  void registerUser() {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);

    // Act
    RegisterUserResponse responseBody = apiActions.registerUser(body);

    // Assert
    softly.assertThat(responseBody.email()).isEqualTo(body.email());
    softly.assertThat(responseBody.fullName()).isEqualTo(body.fullName());
    softly.assertThat(responseBody.username()).isEqualTo(body.username());
  }

  @ParameterizedTest
  @NullAndEmptySource
  void registerUser_withoutEmail(String email) {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        email,
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `email` failed. Reason(s): email is required.");
  }

  @Test
  void registerUser_invalidEmail() {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        "test",
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `email` failed."
            + " Reason(s): must be a well-formed email address.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void registerUser_withoutUsername(String username) {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        username,
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .contains("Validation for field `username` failed.")
        .contains("username is required");
  }

  @ParameterizedTest
  @ValueSource(strings = {"aa", "abcdefghijklmnopqrstu"})
  void registerUser_usernameOfInvalidLength(String username) {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        username,
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `username` failed."
            + " Reason(s): must be between 3 and 20 characters long.");
  }

  @Test
  void registerUser_invalidUsername() {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        "username@",
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `username` failed. Reason(s):"
            + " can only contain alphanumeric characters, underscores, hyphens and dots.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void registerUser_withoutFullName(String fullName) {
    // Arrange
    char[] password = TestValues.password();
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        fullName,
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `fullName` failed. Reason(s): fullName is required.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void registerUser_withoutPassword(char[] password) {
    // Arrange
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .contains("Validation for field `password` failed.")
        .contains("password is required");
  }

  @ParameterizedTest
  @ValueSource(strings = {"aA!1234",
      "thisPasswordIsDefinitelyWayTooLongForTheValidation111!!!!!!!!!!!!"})
  void registerUser_passwordTooShort(String password) {
    // Arrange
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        TestValues.fullName(),
        password.toCharArray(),
        password.toCharArray());

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `password` failed. "
            + "Reason(s): must be between 8 and 64 characters long.");
  }

  @Test
  void registerUser_passwordInvalidFormat() {
    // Arrange
    String invalidPassword = "password";
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        TestValues.fullName(),
        invalidPassword.toCharArray(),
        invalidPassword.toCharArray());

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation for field `password` failed. "
            + "Reason(s): must contain at least one digit, one uppercase letter,"
            + " one lowercase letter and one special character.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void registerUser_withoutPasswordConfirm(char[] passwordConfirm) {
    // Arrange
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        TestValues.fullName(),
        TestValues.password(),
        passwordConfirm);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .contains("Validation for field `passwordConfirm` failed.")
        .contains("password confirmation is required");
  }

  @Test
  void registerUser_passwordMismatch() {
    // Arrange
    RegisterUserRequest body = new RegisterUserRequest(
        TestValues.email().value(),
        TestValues.username().value(),
        TestValues.fullName(),
        TestValues.password(),
        "NotTheSamePassword1!".toCharArray());

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(body);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    assertThat(responseBody.message())
        .isEqualTo("Validation failed. Reason(s): password and password confirmation must match.");
  }

  @Test
  void registerUser_emailAlreadyExists() {
    // Arrange
    String email = TestValues.email().value();

    char[] password = TestValues.password();
    RegisterUserRequest initialUser = new RegisterUserRequest(
        email,
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);
    apiActions.registerUser(initialUser);

    RegisterUserRequest duplicateUser = new RegisterUserRequest(
        email,
        TestValues.username().value(),
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(duplicateUser);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_CONFLICT)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_CONFLICT);
    assertThat(responseBody.message()).isEqualTo("User with this email already exists.");
  }

  @Test
  void registerUser_usernameAlreadyExists() {
    // Arrange
    String username = TestValues.username().value();

    char[] password = TestValues.password();
    RegisterUserRequest initialUser = new RegisterUserRequest(
        TestValues.email().value(),
        username,
        TestValues.fullName(),
        password,
        password);
    apiActions.registerUser(initialUser);

    RegisterUserRequest duplicateUser = new RegisterUserRequest(
        TestValues.email().value(),
        username,
        TestValues.fullName(),
        password,
        password);

    // Act
    MockMvcResponse response = apiActions.raw().registerUser(duplicateUser);

    // Assert
    ErrorResponse responseBody = response.then()
        .statusCode(HttpStatus.SC_CONFLICT)
        .extract()
        .body()
        .as(ErrorResponse.class);

    assertThat(responseBody.statusCode()).isEqualTo(HttpStatus.SC_CONFLICT);
    assertThat(responseBody.message()).isEqualTo("User with this username already exists.");
  }
}
