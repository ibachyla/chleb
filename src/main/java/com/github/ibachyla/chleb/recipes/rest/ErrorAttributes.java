package com.github.ibachyla.chleb.recipes.rest;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * Provides custom error attributes for errors processed by Spring Boot.
 */
@Component
public class ErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                ErrorAttributeOptions options) {
    Map<String, Object> attributes = super.getErrorAttributes(webRequest, options);

    attributes.remove("timestamp");
    attributes.remove("error");
    attributes.remove("trace");
    attributes.remove("path");

    Object status = attributes.remove("status");
    attributes.put("status_code", status);

    return attributes;
  }
}
