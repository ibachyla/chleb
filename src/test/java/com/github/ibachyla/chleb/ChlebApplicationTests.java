package com.github.ibachyla.chleb;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@Import(ChlebTestConfiguration.class)
final class ChlebApplicationTests {

  @Test
  void contextLoads() {
  }
}
