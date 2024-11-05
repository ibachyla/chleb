package com.github.ibachyla.chleb;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ibachyla.chleb.users.rest.dto.GetTokenResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpStatus;
import org.testcontainers.shaded.com.google.common.base.Splitter;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

/**
 * Provides a valid token for the API.
 */
public class TokenProvider {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final CredentialsProvider credentialsProvider;
  private final MockMvcRequestSpecification reqSpec;

  private final Lock lock = new ReentrantLock();

  private String token;

  public TokenProvider(CredentialsProvider credentialsProvider,
                       MockMvcRequestSpecification reqSpec) {
    this.credentialsProvider = credentialsProvider;
    this.reqSpec = reqSpec;
  }

  /**
   * Get a valid token.
   *
   * @return a valid token
   */
  public String get() {
    if (token != null && !isExpired(token) && !willSoonExpire(token)) {
      return token;
    }

    lock.lock();
    try {
      if (token == null || isExpired(token)) {
        token = requestToken();
      } else if (willSoonExpire(token)) {
        token = refreshToken();
      }
      return token;
    } finally {
      lock.unlock();
    }
  }

  private String requestToken() {
    CredentialsProvider.Credentials user = credentialsProvider.get();

    String token = given()
        .spec(reqSpec)
        .contentType(MULTIPART_FORM_DATA)
        .formParam("username", user.username())
        .formParam("password", user.password())
        .when()
        .post("api/auth/token")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .as(GetTokenResponse.class)
        .accessToken();

    return "Bearer " + token;
  }

  private String refreshToken() {
    return given()
        .spec(reqSpec)
        .when()
        .get("api/auth/refresh")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .as(GetTokenResponse.class)
        .accessToken();
  }

  @SuppressWarnings("TimeZoneUsage")
  private boolean isExpired(String token) {
    long exp = getExpirationTime(token);

    return Instant.now().getEpochSecond() > exp;
  }

  @SuppressWarnings("TimeZoneUsage")
  private boolean willSoonExpire(String token) {
    long exp = getExpirationTime(token);

    return Instant.now().getEpochSecond() + 30 > exp;
  }

  private long getExpirationTime(String token) {
    String jwtToken = token.substring(7);
    String payload = Iterables.get(Splitter.on('.').split(jwtToken), 1);
    String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), UTF_8);

    long exp;
    try {
      JsonNode jsonObject = OBJECT_MAPPER.readTree(decodedPayload);
      exp = jsonObject.get("exp").asLong();
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to parse JWT token", e);
    }

    return exp;
  }
}
