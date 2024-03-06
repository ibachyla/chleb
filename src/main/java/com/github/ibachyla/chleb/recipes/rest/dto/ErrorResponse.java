package com.github.ibachyla.chleb.recipes.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error response.
 *
 * @param statusCode the status code
 * @param message    the error message
 */
public record ErrorResponse(@JsonProperty("status_code") int statusCode,
                            String message) {
}
