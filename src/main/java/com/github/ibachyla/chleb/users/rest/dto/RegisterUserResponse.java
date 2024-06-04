package com.github.ibachyla.chleb.users.rest.dto;

/**
 * Register user response DTO.
 */
public record RegisterUserResponse(
    String email,
    String fullName,
    String username
) {
}
