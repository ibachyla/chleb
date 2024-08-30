package com.github.ibachyla.chleb.security;

import com.google.common.collect.ImmutableList;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Security properties.
 *
 * @param issuer the issuer URL. Used to specify the iss claim in the JWT.
 */
@Validated
@ConfigurationProperties(prefix = "chleb.security")
public record SecurityProperties(@NotNull URL issuer,
                                 @Positive long tokenExpirationTime,
                                 @NotNull ChronoUnit tokenExpirationUnit,
                                 List<String> allowedOrigins) {

  public static final String SECURITY_SCHEME_NAME = "BearerAuth";

  /**
   * Default constructor.
   */
  public SecurityProperties {
    if (allowedOrigins == null) {
      allowedOrigins = ImmutableList.of();
    }
  }

  @Override
  public List<String> allowedOrigins() {
    return List.copyOf(allowedOrigins);
  }
}
