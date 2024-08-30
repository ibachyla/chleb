package com.github.ibachyla.chleb.app;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Theme properties.
 *
 * @param lightPrimary   Primary color for light theme.
 * @param lightAccent    Accent color for light theme.
 * @param lightSecondary Secondary color for light theme.
 * @param lightSuccess   Success color for light theme.
 * @param lightInfo      Info color for light theme.
 * @param lightWarning   Warning color for light theme.
 * @param lightError     Error color for light theme.
 * @param darkPrimary    Primary color for dark theme.
 * @param darkAccent     Accent color for dark theme.
 * @param darkSecondary  Secondary color for dark theme.
 * @param darkSuccess    Success color for dark theme.
 * @param darkInfo       Info color for dark theme.
 * @param darkWarning    Warning color for dark theme.
 * @param darkError      Error color for dark theme.
 */
@Validated
@ConfigurationProperties(prefix = "chleb.theme")
public record ThemeProperties(
    @NotBlank
    String lightPrimary,
    @NotBlank
    String lightAccent,
    @NotBlank
    String lightSecondary,
    @NotBlank
    String lightSuccess,
    @NotBlank
    String lightInfo,
    @NotBlank
    String lightWarning,
    @NotBlank
    String lightError,
    @NotBlank
    String darkPrimary,
    @NotBlank
    String darkAccent,
    @NotBlank
    String darkSecondary,
    @NotBlank
    String darkSuccess,
    @NotBlank
    String darkInfo,
    @NotBlank
    String darkWarning,
    @NotBlank
    String darkError) {
}
