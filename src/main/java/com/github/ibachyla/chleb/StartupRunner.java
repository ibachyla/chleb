package com.github.ibachyla.chleb;

import com.github.ibachyla.chleb.groups.services.GroupService;
import com.github.ibachyla.chleb.users.services.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link ApplicationRunner} that runs on application startup.
 */
@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {

  private final GroupService groupService;
  private final UserService userService;

  @Override
  public void run(ApplicationArguments args) {
    UUID defaultGroupId = groupService.createDefaultGroup();
    userService.createDefaultUser(defaultGroupId);
  }
}
