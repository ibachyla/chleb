package com.github.ibachyla.chleb.app.services;

/**
 * Classes implementing this interface possess knowledge about the deployment configuration.
 */
public interface DeploymentAware {

  boolean isProduction();

  String getVersion();

  boolean isDemo();

  boolean isSignupAllowed();

  String getDefaultGroupSlug();

  boolean isOidcEnabled();

  boolean isOidcRedirectEnabled();

  String getOidcProviderName();
}
