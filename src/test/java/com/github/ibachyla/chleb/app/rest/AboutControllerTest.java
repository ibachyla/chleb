package com.github.ibachyla.chleb.app.rest;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;

import com.github.ibachyla.chleb.ApiActions;
import com.github.ibachyla.chleb.ChlebTestConfiguration;
import com.github.ibachyla.chleb.app.rest.dto.GetAboutResponse;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@Import(ChlebTestConfiguration.class)
@ExtendWith(SoftAssertionsExtension.class)
final class AboutControllerTest {

  @InjectSoftAssertions
  SoftAssertions softly;

  @Autowired
  ApiActions apiActions;

  @Test
  void getAbout() {
    // Act
    GetAboutResponse response = apiActions.getAbout();

    // Assert
    softly.assertThat(response.production()).isFalse();
    softly.assertThat(response.version()).isEqualTo("test");
    softly.assertThat(response.demoStatus()).isFalse();
    softly.assertThat(response.allowSignup()).isTrue();
    softly.assertThat(response.defaultGroupSlug()).isEqualTo("home");
    softly.assertThat(response.enableOidc()).isFalse();
    softly.assertThat(response.oidcRedirect()).isFalse();
    softly.assertThat(response.oidcProviderName()).isEqualTo("OAuth");
  }

  @Test
  void getTheme() {
    // Act
    var response = apiActions.getTheme();

    // Assert
    softly.assertThat(response.lightPrimary()).isEqualTo("#E58325");
    softly.assertThat(response.lightAccent()).isEqualTo("#007A99");
    softly.assertThat(response.lightSecondary()).isEqualTo("#973542");
    softly.assertThat(response.lightSuccess()).isEqualTo("#43A047");
    softly.assertThat(response.lightInfo()).isEqualTo("#1976D2");
    softly.assertThat(response.lightWarning()).isEqualTo("#FF6D00");
    softly.assertThat(response.lightError()).isEqualTo("#EF5350");
    softly.assertThat(response.darkPrimary()).isEqualTo("#E58325");
    softly.assertThat(response.darkAccent()).isEqualTo("#007A99");
    softly.assertThat(response.darkSecondary()).isEqualTo("#973542");
    softly.assertThat(response.darkSuccess()).isEqualTo("#43A047");
    softly.assertThat(response.darkInfo()).isEqualTo("#1976D2");
    softly.assertThat(response.darkWarning()).isEqualTo("#FF6D00");
    softly.assertThat(response.darkError()).isEqualTo("#EF5350");
  }
}