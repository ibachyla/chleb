package com.github.ibachyla.chleb.users.models.entities;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Role;
import com.github.ibachyla.chleb.users.models.values.Username;
import java.util.UUID;
import org.junit.jupiter.api.Test;

final class UserTest {

  public static final UUID ID = randomUUID();
  public static final Email EMAIL = new Email("test@example.com");
  public static final Username USERNAME = new Username("testuser");
  public static final String FULL_NAME = "Test User";
  public static final HashedPassword PASSWORD = new HashedPassword("ValidPassword1!");
  public static final UUID GROUP_ID = randomUUID();

  @Test
  void constructor_positive_validUser() {
    // Act
    User user = new User(ID, EMAIL, USERNAME, FULL_NAME, PASSWORD, Role.USER, GROUP_ID);

    // Assert
    assertThat(user.id()).isEqualTo(ID);
    assertThat(user.email()).isEqualTo(EMAIL);
    assertThat(user.username()).isEqualTo(USERNAME);
    assertThat(user.fullName()).isEqualTo(FULL_NAME);
    assertThat(user.password()).isEqualTo(PASSWORD);
    assertThat(user.role()).isEqualTo(Role.USER);
    assertThat(user.groupId()).isEqualTo(GROUP_ID);
  }

  @Test
  void constructor_negative_nullEmail() {
    // Act & Assert
    assertThatThrownBy(() -> new User(null, USERNAME, FULL_NAME, PASSWORD))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("email cannot be null");
  }

  @Test
  void constructor_negative_nullUsername() {
    // Act & Assert
    assertThatThrownBy(() -> new User(EMAIL, null, FULL_NAME, PASSWORD))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("username cannot be null");
  }

  @Test
  void constructor_negative_nullFullName() {
    // Act & Assert
    assertThatThrownBy(() -> new User(EMAIL, USERNAME, null, PASSWORD))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("fullName cannot be blank");
  }

  @Test
  void constructor_negative_blankFullName() {
    // Act & Assert
    assertThatThrownBy(() -> new User(EMAIL, USERNAME, "", PASSWORD))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("fullName cannot be blank");
  }

  @Test
  void constructor_negative_nullPassword() {
    // Act & Assert
    assertThatThrownBy(() -> new User(EMAIL, USERNAME, FULL_NAME, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("password cannot be null");
  }

  @Test
  void setEmail_positive() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);
    Email newEmail = new Email("newtest@example.com");

    // Act
    user.email(newEmail);

    // Assert
    assertThat(user.email()).isEqualTo(newEmail);
  }

  @Test
  void setEmail_negative_nullEmail() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);

    // Act & Assert
    assertThatThrownBy(() -> user.email(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("email cannot be null");
  }

  @Test
  void setUsername_positive() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);
    Username newUsername = new Username("newtestuser");

    // Act
    user.username(newUsername);

    // Assert
    assertThat(user.username()).isEqualTo(newUsername);
  }

  @Test
  void setUsername_negative_nullUsername() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);

    // Act & Assert
    assertThatThrownBy(() -> user.username(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("username cannot be null");
  }

  @Test
  void setFullName_positive() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);
    String newFullName = "New Test User";

    // Act
    user.fullName(newFullName);

    // Assert
    assertThat(user.fullName()).isEqualTo(newFullName);
  }

  @Test
  void setFullName_negative_nullFullName() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);

    // Act & Assert
    assertThatThrownBy(() -> user.fullName(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("fullName cannot be blank");
  }

  @Test
  void setFullName_negative_blankFullName() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);

    // Act & Assert
    assertThatThrownBy(() -> user.fullName(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("fullName cannot be blank");
  }

  @Test
  void setPassword_positive() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);
    HashedPassword newPassword = new HashedPassword("NewValidPassword1!");

    // Act
    user.password(newPassword);

    // Assert
    assertThat(user.password()).isEqualTo(newPassword);
  }

  @Test
  void setPassword_negative_nullPassword() {
    // Arrange
    User user = new User(EMAIL, USERNAME, FULL_NAME, PASSWORD);

    // Act & Assert
    assertThatThrownBy(() -> user.password(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("password cannot be null");
  }
}