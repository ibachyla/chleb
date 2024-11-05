package com.github.ibachyla.chleb.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.app.DeploymentProperties;
import com.github.ibachyla.chleb.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class AboutServiceImplTest {

  DeploymentProperties deploymentProperties;

  @Mock
  UserService userServiceMock;

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
    aboutService = new AboutServiceImpl(deploymentProperties, userServiceMock);
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

  @Test
  void isFirstLogin_true() {
    // Arrange
    when(userServiceMock.isDefaultUserPresent()).thenReturn(true);

    // Act & Assert
    assertThat(aboutService.isFirstLogin()).isTrue();
  }

  @Test
  void isFirstLogin_false() {
    // Arrange
    when(userServiceMock.isDefaultUserPresent()).thenReturn(false);

    // Act & Assert
    assertThat(aboutService.isFirstLogin()).isFalse();
  }
}