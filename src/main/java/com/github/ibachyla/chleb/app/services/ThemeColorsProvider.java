package com.github.ibachyla.chleb.app.services;

/**
 * Provides theme colors.
 */
public interface ThemeColorsProvider {
  String getLightPrimary();

  String getLightAccent();

  String getLightSecondary();

  String getLightSuccess();

  String getLightInfo();

  String getLightWarning();

  String getLightError();

  String getDarkPrimary();

  String getDarkAccent();

  String getDarkSecondary();

  String getDarkSuccess();

  String getDarkInfo();

  String getDarkWarning();

  String getDarkError();
}
