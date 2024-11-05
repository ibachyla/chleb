package com.github.ibachyla.chleb.groups.services;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.ibachyla.chleb.app.services.AboutService;
import com.github.ibachyla.chleb.groups.data.entities.GroupEntity;
import com.github.ibachyla.chleb.groups.data.mappers.GroupToPersistenceEntityMapper;
import com.github.ibachyla.chleb.groups.data.repositories.GroupRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class GroupServiceImplTest {

  private static final String GROUP_NAME = "home";

  @Mock
  GroupRepository groupRepositoryMock;

  @Mock
  AboutService aboutServiceMock;

  GroupService groupService;

  @BeforeEach
  void setUp() {
    when(aboutServiceMock.getDefaultGroupSlug()).thenReturn(GROUP_NAME);

    groupService = new GroupServiceImpl(
        groupRepositoryMock,
        new GroupToPersistenceEntityMapper(),
        aboutServiceMock
    );
  }

  @Test
  void createDefaultGroup_groupDoesNotExist() {
    // Arrange
    UUID groupId = randomUUID();
    GroupEntity groupEntity = new GroupEntity();
    groupEntity.id(groupId);
    groupEntity.name(GROUP_NAME);

    when(groupRepositoryMock.findByName(GROUP_NAME)).thenReturn(Optional.empty());
    when(groupRepositoryMock.save(argThat(g -> g.name().equals(GROUP_NAME))))
        .thenReturn(groupEntity);

    // Act & Assert
    assertThat(groupService.createDefaultGroup()).isEqualTo(groupId);
  }

  @Test
  void createDefaultGroup_groupExists() {
    // Arrange
    UUID groupId = randomUUID();
    GroupEntity groupEntity = new GroupEntity();
    groupEntity.id(groupId);
    groupEntity.name(GROUP_NAME);

    when(groupRepositoryMock.findByName(GROUP_NAME)).thenReturn(Optional.of(groupEntity));

    // Act & Assert
    verify(groupRepositoryMock, never()).save(any());
    assertThat(groupService.createDefaultGroup()).isEqualTo(groupId);
  }
}