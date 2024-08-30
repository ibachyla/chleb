package com.github.ibachyla.chleb.app.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ibachyla.chleb.app.DeploymentProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class AboutServiceTest {

  DeploymentProperties deploymentProperties;
  AboutService aboutService;

  @BeforeEach
  void setUp() {
    deploymentProperties = new DeploymentProperties(false,
        "test",
        false,
        false,
        "home",
        false,
        false,
        "OAuth");
    aboutService = new AboutService(deploymentProperties);
  }

  @Test
  void isProduction() {
    // Act & Assert
    assertThat(aboutService.isProduction()).isFalse();
  }

  @Test
  void getVersion() {
    // Act & Assert
    assertThat(aboutService.getVersion()).isEqualTo("test");
  }

  @Test
  void isDemo() {
    // Act & Assert
    assertThat(aboutService.isDemo()).isFalse();
  }

  @Test
  void isSignupAllowed() {
    // Act & Assert
    assertThat(aboutService.isSignupAllowed()).isFalse();
  }

  @Test
  void getDefaultGroupSlug() {
    // Act & Assert
    assertThat(aboutService.getDefaultGroupSlug()).isEqualTo("home");
  }

  @Test
  void isOidcEnabled() {
    // Act & Assert
    assertThat(aboutService.isOidcEnabled()).isFalse();
  }

  @Test
  void isOidcRedirectEnabled() {
    // Act & Assert
    assertThat(aboutService.isOidcRedirectEnabled()).isFalse();
  }

  @Test
  void getOidcProviderName() {
    // Act & Assert
    assertThat(aboutService.getOidcProviderName()).isEqualTo("OAuth");
  }
}