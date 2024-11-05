package com.github.ibachyla.chleb.users.services;

import com.github.ibachyla.chleb.users.models.entities.User;
import java.util.UUID;

/**
 * User service.
 */
public interface UserService {

  User register(User user);

  String login(String usernameOrEmail, char[] password);

  boolean isDefaultUserPresent();

  void createDefaultUser(UUID groupId);
}
