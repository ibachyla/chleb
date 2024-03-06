package com.github.ibachyla.chleb;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Chleb application.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Chleb API",
        version = "1.0",
        description = "API for managing your recipes, meal plans, and shopping lists."
    )
)
@SpringBootApplication
public class ChlebApplication {
  public static void main(String[] args) {
    SpringApplication.run(ChlebApplication.class, args);
  }
}
