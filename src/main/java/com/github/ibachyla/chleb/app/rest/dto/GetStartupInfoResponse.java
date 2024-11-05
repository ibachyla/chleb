package com.github.ibachyla.chleb.app.rest.dto;

import lombok.Builder;

/**
 * Get startup info response.
 *
 * @param isFirstLogin true if it is a fresh installation and default user credentials have not been
 *                     changed yet
 * @param isDemo       true if the app is running in demo mode
 */
@Builder
public record GetStartupInfoResponse(boolean isFirstLogin, boolean isDemo) {
}
