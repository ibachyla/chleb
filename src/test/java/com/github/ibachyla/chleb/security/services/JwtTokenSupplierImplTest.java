package com.github.ibachyla.chleb.security.services;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.security.SecurityProperties;
import com.github.ibachyla.chleb.users.TestValues;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Role;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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

  @InjectSoftAssertions
  SoftAssertions softly;

  @Mock
  private Clock clock;

  @Test
  @SuppressWarnings("TimeZoneUsage")
  void supply() throws URISyntaxException, MalformedURLException {
    // Arrange
    SecurityProperties securityProperties = new SecurityProperties(
        new URI("http://localhost:8080").toURL());
    Instant now = Instant.now();
    when(clock.instant()).thenReturn(now);
    Duration tokenExpiration = Duration.ofMinutes(5);

    JwtTokenSupplier jwtTokenSupplierImpl = new JwtTokenSupplierImpl(securityProperties,
        JWT_ENCODER,
        clock,
        tokenExpiration);
    User user = new User(randomUUID(),
        TestValues.email(),
        TestValues.username(),
        TestValues.fullName(),
        new HashedPassword("abcdefg"),
        Role.USER);

    // Act
    String encodedToken = jwtTokenSupplierImpl.supply(user);
    Jwt token = JWT_DECODER.decode(encodedToken);

    // Assert
    softly.assertThat(token.getSubject()).isEqualTo(user.id().toString());
    softly.assertThat(token.getIssuer()).isEqualTo(securityProperties.issuer());
    softly.assertThat(token.getExpiresAt())
        .isEqualTo(now.plus(tokenExpiration).truncatedTo(ChronoUnit.SECONDS));
    softly.assertThat(token.getHeaders()).containsEntry(JoseHeaderNames.ALG, "HS256");
  }

  private static JwtEncoder createJwtEncoder() {
    JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(SECRET_KEY);
    return new NimbusJwtEncoder(immutableSecret);
  }

  private static JwtDecoder createJwtDecoder() {
    SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY, "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(secretKey).build();
  }
}