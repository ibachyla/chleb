package com.github.ibachyla.chleb.users.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for the response of the token endpoint.
 *
 * @param accessToken the access token
 * @param tokenType   the token type
 */
public record GetTokenResponse(@JsonProperty("access_token") String accessToken,
                               @JsonProperty("token_type") String tokenType) {

  public GetTokenResponse(String accessToken) {
    this(accessToken, "bearer");
  }
}
