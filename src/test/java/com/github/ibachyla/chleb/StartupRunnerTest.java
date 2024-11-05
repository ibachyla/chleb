package com.github.ibachyla.chleb;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.groups.services.GroupService;
import com.github.ibachyla.chleb.users.services.UserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationRunner;

@ExtendWith(MockitoExtension.class)
final class StartupRunnerTest {

  @Mock
  GroupService groupService;

  @Mock
  UserService userService;

  ApplicationRunner startupRunner;

  @BeforeEach
  void setUp() {
    startupRunner = new StartupRunner(groupService, userService);
  }

  @Test
  void run() throws Exception {
    // Arrange
    UUID defaultGroupId = randomUUID();
    when(groupService.createDefaultGroup()).thenReturn(defaultGroupId);

    // Act
    startupRunner.run(null);

    // Assert
    verify(groupService).createDefaultGroup();
    verify(userService).createDefaultUser(defaultGroupId);
  }
}