package com.github.ibachyla.chleb;

import static com.github.ibachyla.chleb.Constants.TEST_PROFILE;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles(TEST_PROFILE)
final class ChlebApplicationTests {

  @Test
  void contextLoads() {
  }
}
