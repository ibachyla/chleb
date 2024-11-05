package com.github.ibachyla.chleb.app.services;

import com.github.ibachyla.chleb.app.DeploymentProperties;
import com.github.ibachyla.chleb.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * About service.
 */
@Component
@RequiredArgsConstructor
public class AboutServiceImpl implements AboutService {

  private final DeploymentProperties deploymentProperties;
  private final UserService userService;

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

  @Override
  public boolean isFirstLogin() {
    return userService.isDefaultUserPresent();
  }
}
