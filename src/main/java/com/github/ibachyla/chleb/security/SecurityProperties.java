package com.github.ibachyla.chleb.security;

import jakarta.validation.constraints.NotNull;
import java.net.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Security properties.
 *
 * @param issuer the issuer URL. Used to specify the iss claim in the JWT.
 */
@Validated
@ConfigurationProperties(prefix = "chleb.security")
public record SecurityProperties(@NotNull URL issuer) {
}
