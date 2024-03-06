package com.github.ibachyla.chleb;

import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test configuration for Chleb application.
 */
@TestConfiguration
public class ChlebTestConfiguration {

  @Bean
  public MockMvcRequestSpecification restAssuredReqSpec(@Autowired MockMvc mockMvc) {
    return new MockMvcRequestSpecBuilder().setMockMvc(mockMvc).build();
  }
}
