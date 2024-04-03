package com.github.ibachyla.chleb.security;

import com.github.ibachyla.chleb.security.services.PasswordEncoder;
import java.nio.CharBuffer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Configure and return security filter chain.
   *
   * @param http Http security.
   * @return Security filter chain.
   * @throws Exception If an error occurs.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            authorizeRequests -> authorizeRequests.anyRequest().anonymous())
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return rawPassword -> new BCryptPasswordEncoder().encode(CharBuffer.wrap(rawPassword));
  }
}
