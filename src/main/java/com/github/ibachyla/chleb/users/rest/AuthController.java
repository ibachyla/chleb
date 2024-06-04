package com.github.ibachyla.chleb.users.rest;

import static com.github.ibachyla.chleb.utils.ArrayUtils.fillWithZeroes;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.users.rest.dto.GetTokenResponse;
import com.github.ibachyla.chleb.users.services.UserService;
import com.github.ibachyla.chleb.users.services.exceptions.AuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

  /**
   * Validate user credentials and return a JWT token.
   *
   * @param username username
   * @param password password
   * @return JWT token
   */
  @ApiResponse(responseCode = "200", description = "Successful Response")
  @Operation(summary = "Get Token",
      requestBody = @RequestBody(content = @Content(mediaType = APPLICATION_FORM_URLENCODED_VALUE))
  )
  @PostMapping(path = "/token",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public GetTokenResponse getToken(
      @RequestParam("username") String username,
      @Schema(type = "string", format = "password") @RequestParam("password") char[] password
  ) {
    String token = userService.login(username, password);
    fillWithZeroes(password);
    return new GetTokenResponse(token);
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
