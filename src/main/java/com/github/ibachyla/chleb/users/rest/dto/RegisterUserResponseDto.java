package com.github.ibachyla.chleb.users.rest.dto;

/**
 * Register user response DTO.
 */
public record RegisterUserResponseDto(
    String email,
    String fullName,
    String username
) {
}
