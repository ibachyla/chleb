package com.github.ibachyla.chleb.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ibachyla.chleb.rest.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Custom implementation of {@link AuthenticationEntryPoint} that adds JSON body to response.
 */
public final class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final BearerTokenAuthenticationEntryPoint bearerTokenAuthenticationEntryPoint
      = new BearerTokenAuthenticationEntryPoint();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {
    bearerTokenAuthenticationEntryPoint.commence(request, response, authException);

    response.setContentType(APPLICATION_JSON_VALUE);

    ErrorResponse errorResponse = new ErrorResponse(response.getStatus(), "Unauthorized");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    response.getWriter().flush();
  }
}
