package com.github.ibachyla.chleb.recipes.models.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ibachyla.chleb.models.validators.ValidationErrorsHandlerImpl;
import com.github.ibachyla.chleb.models.validators.ValidationException;
import org.junit.jupiter.api.Test;

final class ValidationErrorsHandlerImplTest {

  @Test
  void throwIfHasErrors_noErrors() {
    // Arrange
    ValidationErrorsHandlerImpl errorsHandler = new ValidationErrorsHandlerImpl();

    // Act & Assert
    assertThatCode(errorsHandler::throwIfHasErrors).doesNotThrowAnyException();
  }

  @Test
  void throwIfHasErrors_withErrors() {
    // Arrange
    ValidationErrorsHandlerImpl errorsHandler = new ValidationErrorsHandlerImpl();
    errorsHandler.addError("Error 1");
    errorsHandler.addError("Error 2");

    // Act & Assert
    Exception ex = assertThrows(ValidationException.class, errorsHandler::throwIfHasErrors);
    assertThat(ex.getMessage()).contains("Error 1").contains("Error 2");
  }
}
