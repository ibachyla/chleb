package com.github.ibachyla.chleb.security;

import static com.github.ibachyla.chleb.security.SecurityProperties.SECURITY_SCHEME_NAME;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;

import com.github.ibachyla.chleb.security.services.PasswordEncoder;
import com.github.ibachyla.chleb.security.services.PasswordMatcher;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.nio.CharBuffer;
import java.time.Duration;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Spring Security configuration.
 */
@SecurityScheme(
    name = SECURITY_SCHEME_NAME,
    type = SecuritySchemeType.OAUTH2,
    scheme = "bearer",
    bearerFormat = "JWT",
    flows = @OAuthFlows(
        password = @OAuthFlow(
            tokenUrl = "/api/auth/token"
        )
    )
)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();
  private static final SecretKey SECRET_KEY = generateKey();

  private final SecurityProperties securityProperties;

  /**
   * Configure and return security filter chain.
   *
   * @param http Http security.
   * @return Security filter chain.
   * @throws Exception If an error occurs.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(POST, "/api/auth/token", "/api/users/register")
            .permitAll()
            .requestMatchers(GET,
                "/api/app/about/**",
                "/actuator/**",
                "/api-docs*/**",
                "/swagger-ui*/**")
            .permitAll()
            .requestMatchers(OPTIONS, "/api/app/about/**")
            .permitAll()
            .anyRequest()
            .authenticated())
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults())
            .authenticationEntryPoint(new CustomBearerTokenAuthenticationEntryPoint()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exceptions -> exceptions
            .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
        .build();
  }

  /**
   * Create a CORS filter.
   *
   * @return CORS filter.
   */
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(securityProperties.allowedOrigins());
    config.setAllowCredentials(true);
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    config.setMaxAge(Duration.ofHours(1));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return rawPassword -> BCRYPT_PASSWORD_ENCODER.encode(CharBuffer.wrap(rawPassword));
  }

  @Bean
  public PasswordMatcher passwordMatcher() {
    return (rawPassword, hashedPassword) -> BCRYPT_PASSWORD_ENCODER.matches(
        CharBuffer.wrap(rawPassword), hashedPassword);
  }

  /**
   * Create a JWT decoder.
   *
   * @param clockSkew          Clock skew.
   * @param securityProperties Security properties.
   * @return JWT decoder.
   */
  @Bean
  public JwtDecoder jwtDecoder(Duration clockSkew, SecurityProperties securityProperties) {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(SECRET_KEY).build();

    OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
        new JwtTimestampValidator(clockSkew),
        new JwtIssuerValidator(securityProperties.issuer().toString()));

    jwtDecoder.setJwtValidator(withClockSkew);

    return jwtDecoder;
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(SECRET_KEY);
    return new NimbusJwtEncoder(immutableSecret);
  }

  @Bean
  public Duration clockSkew() {
    return Duration.ofSeconds(60);
  }

  @SneakyThrows
  private static SecretKey generateKey() {
    return KeyGenerator.getInstance("HmacSHA256").generateKey();
  }
}
