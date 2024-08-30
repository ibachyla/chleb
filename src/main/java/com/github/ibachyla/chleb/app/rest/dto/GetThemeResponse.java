package com.github.ibachyla.chleb.app.rest.dto;

/**
 * Get theme response.
 *
 * @param lightPrimary   primary color for light theme
 * @param lightAccent    accent color for light theme
 * @param lightSecondary secondary color for light theme
 * @param lightSuccess   success color for light theme
 * @param lightInfo      info color for light theme
 * @param lightWarning   warning color for light theme
 * @param lightError     error color for light theme
 * @param darkPrimary    primary color for dark theme
 * @param darkAccent     accent color for dark theme
 * @param darkSecondary  secondary color for dark theme
 * @param darkSuccess    success color for dark theme
 * @param darkInfo       info color for dark theme
 * @param darkWarning    warning color for dark theme
 * @param darkError      error color for dark theme
 */
public record GetThemeResponse(String lightPrimary,
                               String lightAccent,
                               String lightSecondary,
                               String lightSuccess,
                               String lightInfo,
                               String lightWarning,
                               String lightError,
                               String darkPrimary,
                               String darkAccent,
                               String darkSecondary,
                               String darkSuccess,
                               String darkInfo,
                               String darkWarning,
                               String darkError) {
}
