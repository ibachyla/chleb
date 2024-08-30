package com.github.ibachyla.chleb.app.rest.dto;

/**
 * Get about response.
 *
 * @param production       production status
 * @param version          app version
 * @param demoStatus       demo status
 * @param allowSignup      is signup allowed
 * @param defaultGroupSlug default group slug
 * @param enableOidc       is OIDC enabled
 * @param oidcRedirect     is OIDC redirect enabled
 * @param oidcProviderName OIDC provider name
 */
public record GetAboutResponse(boolean production,
                               String version,
                               boolean demoStatus,
                               boolean allowSignup,
                               String defaultGroupSlug,
                               boolean enableOidc,
                               boolean oidcRedirect,
                               String oidcProviderName) {
}
