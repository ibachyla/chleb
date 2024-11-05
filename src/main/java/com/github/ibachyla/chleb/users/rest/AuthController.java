package com.github.ibachyla.chleb.users.rest;

import static com.github.ibachyla.chleb.security.SecurityProperties.SECURITY_SCHEME_NAME;
import static com.github.ibachyla.chleb.utils.ArrayUtils.fillWithZeroes;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.security.services.JwtTokenSupplier;
import com.github.ibachyla.chleb.users.rest.dto.GetTokenResponse;
import com.github.ibachyla.chleb.users.services.UserService;
import com.github.ibachyla.chleb.users.services.exceptions.AuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller.
 */
@Slf4j
@Tag(name = "Users: Authentication")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final JwtTokenSupplier jwtTokenSupplier;

  /**
   * Validate user credentials and return a JWT token.
   *
   * @param username username
   * @param password password
   * @return JWT token
   */
  @ApiResponse(responseCode = "200", description = "Successful Response")
  @Operation(summary = "Get Token",
      requestBody = @RequestBody(content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE))
  )
  @PostMapping(path = "/token",
      produces = APPLICATION_JSON_VALUE,
      consumes = MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public GetTokenResponse getToken(
      @RequestParam("username") String username,
      @Schema(type = "string", format = "password") @RequestParam("password") char[] password
  ) {
    String token = userService.login(username, password);
    fillWithZeroes(password);
    return new GetTokenResponse(token);
  }

  /**
   * Refresh the JWT token.
   *
   * @return new JWT token
   */
  @SecurityRequirement(name = SECURITY_SCHEME_NAME)
  @ApiResponse(responseCode = "200", description = "Successful Response")
  @Operation(summary = "Refresh Token")
  @GetMapping(path = "/refresh", produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @SuppressWarnings("RequestMappingAnnotation")
  public GetTokenResponse refreshToken(Authentication authentication) {
    return new GetTokenResponse(jwtTokenSupplier.refresh((Jwt) authentication.getPrincipal()));
  }

  @ApiResponse(responseCode = "401",
      description = "Invalid Credentials",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class))
  )
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AuthenticationException.class)
  public ErrorResponse handleUserNotFoundException(AuthenticationException e) {
    return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials");
  }
}
