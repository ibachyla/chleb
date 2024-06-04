package com.github.ibachyla.chleb.users.models.entities;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.github.ibachyla.chleb.models.entities.Entity;
import com.github.ibachyla.chleb.users.models.values.Email;
import com.github.ibachyla.chleb.users.models.values.HashedPassword;
import com.github.ibachyla.chleb.users.models.values.Role;
import com.github.ibachyla.chleb.users.models.values.Username;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * User entity.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends Entity {

  private Email email;
  private Username username;
  private String fullName;
  private HashedPassword password;
  private Role role;

  /**
   * Constructor.
   *
   * @param email    email
   * @param username username
   * @param fullName full name
   * @param password password
   */
  public User(Email email, Username username, String fullName, HashedPassword password) {
    email(email);
    username(username);
    fullName(fullName);
    password(password);
    role(Role.USER);
  }

  /**
   * Constructor.
   *
   * @param id       id
   * @param email    email
   * @param username username
   * @param fullName full name
   * @param password password
   */
  public User(UUID id,
              Email email,
              Username username,
              String fullName,
              HashedPassword password,
              Role role) {
    super(id);
    email(email);
    username(username);
    fullName(fullName);
    password(password);
    role(role);
  }

  /**
   * Sets the email.
   *
   * @param email email
   */
  public void email(Email email) {
    notNull(email, "email cannot be null");

    this.email = email;
  }

  /**
   * Sets the username.
   *
   * @param username username
   */
  public void username(Username username) {
    notNull(username, "username cannot be null");

    this.username = username;
  }

  /**
   * Sets the full name.
   *
   * @param fullName full name
   */
  public void fullName(String fullName) {
    notNull(fullName, "fullName cannot be null");
    notBlank(fullName, "fullName cannot be blank");

    this.fullName = fullName;
  }

  /**
   * Sets the password.
   *
   * @param password password
   */
  public void password(HashedPassword password) {
    notNull(password, "password cannot be null");

    this.password = password;
  }

  /**
   * Sets the role.
   *
   * @param role role
   */
  public void role(Role role) {
    notNull(role, "role cannot be null");

    this.role = role;
  }
}
