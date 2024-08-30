package com.github.ibachyla.chleb.app.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ibachyla.chleb.app.ThemeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class ThemeColorsProviderImplTest {

  static final String LIGHT_PRIMARY = "lightPrimary";
  static final String LIGHT_ACCENT = "lightAccent";
  static final String LIGHT_SECONDARY = "lightSecondary";
  static final String LIGHT_SUCCESS = "lightSuccess";
  static final String LIGHT_INFO = "lightInfo";
  static final String LIGHT_WARNING = "lightWarning";
  static final String LIGHT_ERROR = "lightError";
  static final String DARK_PRIMARY = "darkPrimary";
  static final String DARK_ACCENT = "darkAccent";
  static final String DARK_SECONDARY = "darkSecondary";
  static final String DARK_SUCCESS = "darkSuccess";
  static final String DARK_INFO = "darkInfo";
  static final String DARK_WARNING = "darkWarning";
  static final String DARK_ERROR = "darkError";

  ThemeProperties themeProperties;
  ThemeColorsProvider themeColorsProvider;

  @BeforeEach
  void setUp() {
    themeProperties = new ThemeProperties(
        LIGHT_PRIMARY,
        LIGHT_ACCENT,
        LIGHT_SECONDARY,
        LIGHT_SUCCESS,
        LIGHT_INFO,
        LIGHT_WARNING,
        LIGHT_ERROR,
        DARK_PRIMARY,
        DARK_ACCENT,
        DARK_SECONDARY,
        DARK_SUCCESS,
        DARK_INFO,
        DARK_WARNING,
        DARK_ERROR
    );
    themeColorsProvider = new ThemeColorsProviderImpl(themeProperties);
  }

  @Test
  void getLightPrimary() {
    // Act & Assert
    assertThat(themeColorsProvider.getLightPrimary()).isEqualTo(LIGHT_PRIMARY);
  }

  @Test
  void getLightAccent() {
    // Act & Assert
    assertThat(themeColorsProvider.getLightAccent()).isEqualTo(LIGHT_ACCENT);
  }

  @Test
  void getLightSecondary() {
    // Act & Assert
    assertThat(themeColorsProvider.getLightSecondary()).isEqualTo(LIGHT_SECONDARY);
  }

  @Test
  void getLightSuccess() {
    // Act & Assert
    assertThat(themeColorsProvider.getLightSuccess()).isEqualTo(LIGHT_SUCCESS);
  }

  @Test
  void getLightInfo() {
    // Act & Assert
    assertThat(themeColorsProvider.getLightInfo()).isEqualTo(LIGHT_INFO);
  }

  @Test
  void getLightWarning() {
    // Act & Assert
    assertThat(themeColorsProvider.getLightWarning()).isEqualTo(LIGHT_WARNING);
  }

  @Test
  void getLightError() {
    // Act & Assert
    assertThat(themeColorsProvider.getLightError()).isEqualTo(LIGHT_ERROR);
  }

  @Test
  void getDarkPrimary() {
    // Act & Assert
    assertThat(themeColorsProvider.getDarkPrimary()).isEqualTo(DARK_PRIMARY);
  }

  @Test
  void getDarkAccent() {
    // Act & Assert
    assertThat(themeColorsProvider.getDarkAccent()).isEqualTo(DARK_ACCENT);
  }

  @Test
  void getDarkSecondary() {
    // Act & Assert
    assertThat(themeColorsProvider.getDarkSecondary()).isEqualTo(DARK_SECONDARY);
  }

  @Test
  void getDarkSuccess() {
    // Act & Assert
    assertThat(themeColorsProvider.getDarkSuccess()).isEqualTo(DARK_SUCCESS);
  }

  @Test
  void getDarkInfo() {
    // Act & Assert
    assertThat(themeColorsProvider.getDarkInfo()).isEqualTo(DARK_INFO);
  }

  @Test
  void getDarkWarning() {
    // Act & Assert
    assertThat(themeColorsProvider.getDarkWarning()).isEqualTo(DARK_WARNING);
  }

  @Test
  void getDarkError() {
    // Act & Assert
    assertThat(themeColorsProvider.getDarkError()).isEqualTo(DARK_ERROR);
  }
}