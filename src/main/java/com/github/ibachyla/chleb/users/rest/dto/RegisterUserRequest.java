package com.github.ibachyla.chleb.users.rest.dto;

import static com.github.ibachyla.chleb.utils.ArrayUtils.fillWithZeroes;

import com.github.ibachyla.chleb.models.validators.constraints.PasswordMatch;
import com.github.ibachyla.chleb.models.validators.constraints.ValidPassword;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Username;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Register user request DTO.
 *
 * @param email           email
 * @param username        username
 * @param fullName        full name
 * @param password        password
 * @param passwordConfirm password confirmation
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2",
    justification = "We want to be able to clean the password after it's used")
@PasswordMatch(passwordGetter = "password",
    passwordConfirmGetter = "passwordConfirm",
    message = "password and password confirmation must match")
public record RegisterUserRequest(
    @Email
    @NotBlank(message = "email is required")
    String email,

    @NotBlank(message = "username is required")
    @Size(min = Username.MIN_LENGTH,
        max = Username.MAX_LENGTH,
        message = "must be between {min} and {max} characters long")
    @Pattern(regexp = Username.PATTERN,
        message = "can only contain alphanumeric characters, underscores, hyphens and dots")
    String username,

    @NotBlank(message = "fullName is required")
    String fullName,

    @Schema(type = "string", format = "password")
    @NotEmpty(message = "password is required")
    @Size(min = HashedPassword.MIN_RAW_LENGTH,
        max = HashedPassword.MAX_RAW_LENGTH,
        message = "must be between {min} and {max} characters long")
    @ValidPassword(message = "must contain at least one digit, one uppercase letter,"
        + " one lowercase letter and one special character")
    char[] password,

    @Schema(type = "string", format = "password")
    @NotEmpty(message = "password confirmation is required")
    char[] passwordConfirm
) {

  @SuppressFBWarnings(value = "EI_EXPOSE_REP",
      justification = "We want to be able to clean the password after it's used")
  public char[] password() {
    return password;
  }

  @SuppressFBWarnings(value = "EI_EXPOSE_REP",
      justification = "We want to be able to clean the password after it's used")
  public char[] passwordConfirm() {
    return passwordConfirm;
  }

  public void cleanPassword() {
    fillWithZeroes(password);
    fillWithZeroes(passwordConfirm);
  }

  @Override
  public String toString() {
    return "RegisterUserRequest{"
        + "email='" + email + '\''
        + ", username='" + username + '\''
        + ", fullName='" + fullName + '\''
        + '}';
  }
}
