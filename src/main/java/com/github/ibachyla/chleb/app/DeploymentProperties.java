package com.github.ibachyla.chleb.app;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Deployment properties.
 */
@Validated
@ConfigurationProperties(prefix = "chleb.deployment")
public record DeploymentProperties(boolean production,
                                   @NotBlank
                                   String version,
                                   boolean demo,
                                   boolean signupAllowed,
                                   @NotBlank
                                   String defaultGroupSlug,
                                   boolean oidcEnabled,
                                   boolean oidcRedirectEnabled,
                                   String oidcProviderName) {
}
