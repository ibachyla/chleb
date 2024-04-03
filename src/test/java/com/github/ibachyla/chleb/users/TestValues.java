package com.github.ibachyla.chleb.users;

import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.Username;
import net.datafaker.Faker;

/**
 * Provides test values for the user module.
 */
public class TestValues {

  private static final Faker faker = new Faker();

  /**
   * Generates a random email.
   *
   * @return random email
   */
  public static Email email() {
    return new Email(faker.internet().emailAddress());
  }

  /**
   * Generates a random username.
   *
   * @return random username
   */
  public static Username username() {
    String username = "";
    while (username.length() < Username.MIN_LENGTH || username.length() > Username.MAX_LENGTH) {
      username = faker.internet().username();
    }
    return new Username(username);
  }

  /**
   * Generates a random full name.
   *
   * @return random full name
   */
  public static String fullName() {
    return faker.name().fullName();
  }

  /**
   * Generates a random password.
   *
   * @return random password
   */
  public static char[] password() {
    return faker.internet()
        .password(8, 64, true, true, true)
        .toCharArray();
  }
}
