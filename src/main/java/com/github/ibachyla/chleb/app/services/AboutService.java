package com.github.ibachyla.chleb.app.services;

import com.github.ibachyla.chleb.app.DeploymentProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * About service.
 */
@Component
@RequiredArgsConstructor
public class AboutService implements DeploymentAware {

  private final DeploymentProperties deploymentProperties;

  @Override
  public boolean isProduction() {
    return deploymentProperties.production();
  }

  @Override
  public String getVersion() {
    return deploymentProperties.version();
  }

  @Override
  public boolean isDemo() {
    return deploymentProperties.demo();
  }

  @Override
  public boolean isSignupAllowed() {
    return deploymentProperties.signupAllowed();
  }

  @Override
  public String getDefaultGroupSlug() {
    return deploymentProperties.defaultGroupSlug();
  }

  @Override
  public boolean isOidcEnabled() {
    return deploymentProperties.oidcEnabled();
  }

  @Override
  public boolean isOidcRedirectEnabled() {
    return deploymentProperties.oidcRedirectEnabled();
  }

  @Override
  public String getOidcProviderName() {
    return deploymentProperties.oidcProviderName();
  }
}
