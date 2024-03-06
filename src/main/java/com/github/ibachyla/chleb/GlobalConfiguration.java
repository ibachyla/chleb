package com.github.ibachyla.chleb;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Spring configuration.
 */
@Configuration
public class GlobalConfiguration {

  @Bean
  @SuppressWarnings("TimeZoneUsage")
  public Clock clock() {
    return Clock.systemUTC();
  }
}
