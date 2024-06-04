package com.github.ibachyla.chleb;

import static com.github.ibachyla.chleb.users.TestValues.fullName;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequest;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides credentials that can be used to authenticate in the API.
 */
public class CredentialsProvider {

  private final Lock lock = new ReentrantLock();

  private final MockMvcRequestSpecification reqSpec;

  private Credentials user;

  public CredentialsProvider(MockMvcRequestSpecification reqSpec) {
    this.reqSpec = reqSpec;
  }

  /**
   * Get credentials.
   *
   * @return credentials
   */
  public Credentials get() {
    if (user != null) {
      return user;
    }

    lock.lock();
    try {
      if (user == null) {
        user = registerUser();
      }
      return user;
    } finally {
      lock.unlock();
    }
  }

  private Credentials registerUser() {
    char[] password = TestValues.password();
    String username = TestValues.username().value();
    String email = TestValues.email().value();
    RegisterUserRequest body = new RegisterUserRequest(email,
        username,
        fullName(),
        password,
        password);

    given()
        .spec(reqSpec)
        .contentType(APPLICATION_JSON)
        .body(body)
        .when()
        .post("/api/users/register")
        .then()
        .statusCode(201);

    return new Credentials(username, email, new String(password));
  }

  /**
   * Credentials.
   *
   * @param username username
   * @param email    email
   * @param password password
   */
  public record Credentials(String username, String email, String password) {
  }
}
