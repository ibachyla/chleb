package com.github.ibachyla.chleb.recipes.rest;

import static java.util.stream.Collectors.joining;

import com.github.ibachyla.chleb.recipes.rest.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
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
    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> String.format("Validation for field `%s` failed. Reason: %s.",
            error.getField(),
            error.getDefaultMessage()))
        .collect(joining());
    return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), message);
  }
}
