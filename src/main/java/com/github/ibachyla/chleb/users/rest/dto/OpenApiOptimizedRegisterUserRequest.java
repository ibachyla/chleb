package com.github.ibachyla.chleb.users.rest.dto;

import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Username;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * This record is used purely to generate OpenAPI documentation, as there is an issue with how
 * springdoc-openapi-starter-webmvc-ui handles records with char[] fields.
 * It is a copy of {@link RegisterUserRequest} with String fields instead of char[].
 * It is not used in the application.
 */
public record OpenApiOptimizedRegisterUserRequest(
    @NotBlank
    String email,

    @NotBlank
    @Size(min = Username.MIN_LENGTH,
        max = Username.MAX_LENGTH)
    @Pattern(regexp = Username.PATTERN)
    String username,

    @NotBlank
    String fullName,

    @Schema(format = "password")
    @NotEmpty
    @Size(min = HashedPassword.MIN_RAW_LENGTH,
        max = HashedPassword.MAX_RAW_LENGTH)
    String password,

    @Schema(format = "password")
    @NotEmpty
    String passwordConfirm
) {

  public OpenApiOptimizedRegisterUserRequest {
    throw new UnsupportedOperationException(
        "This class is only for OpenAPI documentation generation");
  }
}
