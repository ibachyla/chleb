package com.github.ibachyla.chleb.users.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.ibachyla.chleb.mappers.Mapper;
import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import com.github.ibachyla.chleb.users.models.entities.User;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserRequest;
import com.github.ibachyla.chleb.users.rest.dto.RegisterUserResponse;
import com.github.ibachyla.chleb.users.services.UserService;
import com.github.ibachyla.chleb.users.services.exceptions.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user registration.
 */
@Slf4j
@Tag(name = "Users: Registration")
@RestController
@RequestMapping("/api/users/register")
@RequiredArgsConstructor
public class UserRegistrationController {

  private final UserService userService;
  private final Mapper<RegisterUserRequest, User> userFromRegisterRequestMapper;
  private final Mapper<User, RegisterUserResponse> userToRegisterResponseMapper;

  /**
   * Register a user.
   *
   * @param body details of the user to register
   * @return description of the created user
   */
  @ApiResponse(responseCode = "201", description = "Successful Response")
  @Operation(summary = "Register New User")
  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public RegisterUserResponse registerUser(@Valid @RequestBody RegisterUserRequest body) {
    log.info("Registering user with email `{}` and username `{}`.", body.email(), body.username());

    User user = userFromRegisterRequestMapper.map(body);
    body.cleanPassword();

    user = userService.register(user);
    log.info("User with email `{}` and username `{}` registered successfully.",
        user.email(), user.username());

    return userToRegisterResponseMapper.map(user);
  }

  @ApiResponse(responseCode = "409",
      description = "User Already Exists",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class))
  )
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
  }
}
