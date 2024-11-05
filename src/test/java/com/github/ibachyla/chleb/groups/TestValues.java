package com.github.ibachyla.chleb.groups;

import static java.util.UUID.randomUUID;

import java.util.UUID;
import net.datafaker.Faker;

/**
 * Provides test values for the group package tests.
 */
public final class TestValues {

  private static final Faker faker = new Faker();

  public static UUID groupId() {
    return randomUUID();
  }

  public static String groupName() {
    return faker.book().title();
  }
}
