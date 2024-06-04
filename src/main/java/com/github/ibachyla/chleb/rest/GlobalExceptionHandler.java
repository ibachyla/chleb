package com.github.ibachyla.chleb.rest;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableList;

import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles any runtime exception.
   *
   * @param ex exception
   * @return error response
   */
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleRuntimeException(RuntimeException ex) {
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
  }

  /**
   * Handles validation exceptions.
   *
   * @param ex exception
   * @return error response
   */
  @ApiResponse(responseCode = "422",
      description = "Validation Error",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class))
  )
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
    StringBuilder messageBuilder = new StringBuilder();

    List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
    if (!globalErrors.isEmpty()) {
      messageBuilder.append("Validation failed. Reason(s): ");

      String errorMessages = globalErrors.stream()
          .map(ObjectError::getDefaultMessage)
          .collect(joining(", ", "", "."));
      messageBuilder.append(errorMessages);
    }

    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    if (!globalErrors.isEmpty() && !fieldErrors.isEmpty()) {
      messageBuilder.append("\n");
    }

    String errorMessages = fieldErrors.stream()
        .collect(groupingBy(FieldError::getField, toUnmodifiableList()))
        .entrySet()
        .stream()
        .map(entry -> String.format("Validation for field `%s` failed. Reason(s): %s.",
            entry.getKey(),
            entry.getValue().stream()
                .map(FieldError::getDefaultMessage)
                .collect(joining(", "))))
        .collect(joining("\n"));
    messageBuilder.append(errorMessages);

    return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), messageBuilder.toString());
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleAuthenticationException(AuthenticationException ex) {
    return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
  }
}
