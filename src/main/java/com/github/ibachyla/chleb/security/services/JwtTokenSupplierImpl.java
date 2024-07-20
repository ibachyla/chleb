package com.github.ibachyla.chleb.security.services;

import com.github.ibachyla.chleb.security.SecurityProperties;
import com.github.ibachyla.chleb.users.models.entities.User;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link JwtTokenSupplier}.
 */
@Service
public class JwtTokenSupplierImpl implements JwtTokenSupplier {

  private final SecurityProperties securityProperties;
  private final JwtEncoder jwtEncoder;
  private final Clock clock;
  private final TemporalAmount tokenExpiration;

  /**
   * Creates a new instance of {@link JwtTokenSupplierImpl}.
   *
   * @param securityProperties the security properties
   * @param jwtEncoder         the JWT encoder
   * @param clock              the clock
   */
  public JwtTokenSupplierImpl(SecurityProperties securityProperties,
                              JwtEncoder jwtEncoder,
                              Clock clock) {
    this.securityProperties = securityProperties;
    this.jwtEncoder = jwtEncoder;
    this.clock = clock;
    this.tokenExpiration = Duration.of(securityProperties.tokenExpirationTime(),
        securityProperties.tokenExpirationUnit());
  }

  @Override
  public String supply(User user) {
    return buildToken(user.id().toString());
  }

  @Override
  public String refresh(Jwt token) {
    return buildToken(token.getSubject());
  }

  private String buildToken(String subject) {
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject(subject)
        .issuer(securityProperties.issuer().toString())
        .expiresAt(clock.instant().plus(tokenExpiration))
        .build();

    JwsHeader header = JwsHeader.with(() -> JwsAlgorithms.HS256).build();

    JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claims);
    return jwtEncoder.encode(parameters).getTokenValue();
  }
}
