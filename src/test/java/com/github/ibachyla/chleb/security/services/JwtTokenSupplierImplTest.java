package com.github.ibachyla.chleb.security.services;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.security.SecurityProperties;
import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.crypto.spec.SecretKeySpec;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JoseHeaderNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
final class JwtTokenSupplierImplTest {

  private static final byte[] SECRET_KEY = "secretabcdefghijklmnopabcdefghijklmnop".getBytes(UTF_8);
  private static final JwtEncoder JWT_ENCODER = createJwtEncoder();
  private static final JwtDecoder JWT_DECODER = createJwtDecoder();

  private static final long TOKEN_EXPIRATION_TIME = 5;
  private static final ChronoUnit TOKEN_EXPIRATION_UNIT = ChronoUnit.MINUTES;
  private static final Duration TOKEN_EXPIRATION =
      Duration.of(TOKEN_EXPIRATION_TIME, TOKEN_EXPIRATION_UNIT);
  private static final SecurityProperties SECURITY_PROPERTIES = new SecurityProperties(
      issuer(), TOKEN_EXPIRATION_TIME, TOKEN_EXPIRATION_UNIT);

  @InjectSoftAssertions
  SoftAssertions softly;

  @Mock
  private Clock clock;

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void supply() {
    // Arrange
    Instant now = Instant.now();
    JwtTokenSupplier jwtTokenSupplierImpl = jwtTokenSupplier(now);
    User user = TestValues.user();

    // Act
    String encodedToken = jwtTokenSupplierImpl.supply(user);
    Jwt token = JWT_DECODER.decode(encodedToken);

    // Assert
    assertToken(token, user, now);
  }

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void refresh() {
    // Arrange
    Instant now = Instant.now();
    JwtTokenSupplier jwtTokenSupplierImpl = jwtTokenSupplier(now);
    User user = TestValues.user();
    Jwt token = JWT_DECODER.decode(jwtTokenSupplierImpl.supply(user));

    // Act
    String encodedToken = jwtTokenSupplierImpl.refresh(token);
    Jwt refreshedToken = JWT_DECODER.decode(encodedToken);

    // Assert
    assertToken(refreshedToken, user, now);
  }

  private void assertToken(Jwt token, User user, Instant tokenCreationTime) {
    softly.assertThat(token.getSubject()).isEqualTo(user.id().toString());
    softly.assertThat(token.getIssuer()).isEqualTo(SECURITY_PROPERTIES.issuer());
    softly.assertThat(token.getExpiresAt())
        .isEqualTo(tokenCreationTime.plus(TOKEN_EXPIRATION).truncatedTo(ChronoUnit.SECONDS));
    softly.assertThat(token.getHeaders()).containsEntry(JoseHeaderNames.ALG, "HS256");
  }

  private JwtTokenSupplier jwtTokenSupplier(Instant tokenCreationTime) {
    when(clock.instant()).thenReturn(tokenCreationTime);

    return new JwtTokenSupplierImpl(SECURITY_PROPERTIES, JWT_ENCODER, clock);
  }

  private static JwtEncoder createJwtEncoder() {
    JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(SECRET_KEY);
    return new NimbusJwtEncoder(immutableSecret);
  }

  private static JwtDecoder createJwtDecoder() {
    SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(secretKey).build();
  }

  private static URL issuer() {
    try {
      return URI.create("http://localhost:8080").toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}