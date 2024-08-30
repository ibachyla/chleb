package com.github.ibachyla.chleb.app.services;

import com.github.ibachyla.chleb.app.ThemeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Provides theme colors.
 */
@Component
@RequiredArgsConstructor
public class ThemeColorsProviderImpl implements ThemeColorsProvider {
  private final ThemeProperties themeProperties;

  @Override
  public String getLightPrimary() {
    return themeProperties.lightPrimary();
  }

  @Override
  public String getLightAccent() {
    return themeProperties.lightAccent();
  }

  @Override
  public String getLightSecondary() {
    return themeProperties.lightSecondary();
  }

  @Override
  public String getLightSuccess() {
    return themeProperties.lightSuccess();
  }

  @Override
  public String getLightInfo() {
    return themeProperties.lightInfo();
  }

  @Override
  public String getLightWarning() {
    return themeProperties.lightWarning();
  }

  @Override
  public String getLightError() {
    return themeProperties.lightError();
  }

  @Override
  public String getDarkPrimary() {
    return themeProperties.darkPrimary();
  }

  @Override
  public String getDarkAccent() {
    return themeProperties.darkAccent();
  }

  @Override
  public String getDarkSecondary() {
    return themeProperties.darkSecondary();
  }

  @Override
  public String getDarkSuccess() {
    return themeProperties.darkSuccess();
  }

  @Override
  public String getDarkInfo() {
    return themeProperties.darkInfo();
  }

  @Override
  public String getDarkWarning() {
    return themeProperties.darkWarning();
  }

  @Override
  public String getDarkError() {
    return themeProperties.darkError();
  }
}
