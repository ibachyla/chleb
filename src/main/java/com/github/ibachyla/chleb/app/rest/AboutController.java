package com.github.ibachyla.chleb.app.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.ibachyla.chleb.app.rest.dto.GetAboutResponse;
import com.github.ibachyla.chleb.app.rest.dto.GetStartupInfoResponse;
import com.github.ibachyla.chleb.app.rest.dto.GetThemeResponse;
import com.github.ibachyla.chleb.app.services.AboutService;
import com.github.ibachyla.chleb.app.services.ThemeColorsProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * About controller.
 */
@Slf4j
@Tag(name = "App: About")
@RestController
@RequestMapping("/api/app/about")
@RequiredArgsConstructor
public class AboutController {

  private final AboutService aboutService;
  private final ThemeColorsProvider themeColorsProvider;

  /**
   * Get app info.
   *
   * @return app info
   */
  @ApiResponse(responseCode = "200", description = "Successful Response")
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get App Info",
      description = "Get general application information")
  public GetAboutResponse getAbout() {
    log.info("App info requested");

    GetAboutResponse response = new GetAboutResponse(
        aboutService.isProduction(),
        aboutService.getVersion(),
        aboutService.isDemo(),
        aboutService.isSignupAllowed(),
        aboutService.getDefaultGroupSlug(),
        aboutService.isOidcEnabled(),
        aboutService.isOidcRedirectEnabled(),
        aboutService.getOidcProviderName()
    );

    log.info("Sending app info: {}", response);

    return response;
  }

  /**
   * Get startup info.
   *
   * @return startup info
   */
  @ApiResponse(responseCode = "200", description = "Successful Response")
  @GetMapping(path = "/startup-info", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get Startup Info", description = "Returns helpful startup information")
  public GetStartupInfoResponse getStartupInfo() {
    log.info("Startup info requested");

    GetStartupInfoResponse response = GetStartupInfoResponse.builder()
        .isFirstLogin(aboutService.isFirstLogin())
        .isDemo(aboutService.isDemo())
        .build();

    log.info("Sending startup info: {}", response);

    return response;
  }

  /**
   * Get app theme.
   *
   * @return app theme
   */
  @ApiResponse(responseCode = "200", description = "Successful Response")
  @GetMapping(path = "/theme", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Get App Theme",
      description = "Get the current theme settings")
  public GetThemeResponse getTheme() {
    log.info("Theme requested");

    GetThemeResponse response = new GetThemeResponse(
        themeColorsProvider.getLightPrimary(),
        themeColorsProvider.getLightAccent(),
        themeColorsProvider.getLightSecondary(),
        themeColorsProvider.getLightSuccess(),
        themeColorsProvider.getLightInfo(),
        themeColorsProvider.getLightWarning(),
        themeColorsProvider.getLightError(),
        themeColorsProvider.getDarkPrimary(),
        themeColorsProvider.getDarkAccent(),
        themeColorsProvider.getDarkSecondary(),
        themeColorsProvider.getDarkSuccess(),
        themeColorsProvider.getDarkInfo(),
        themeColorsProvider.getDarkWarning(),
        themeColorsProvider.getDarkError()
    );

    log.info("Sending theme: {}", response);

    return response;
  }
}
