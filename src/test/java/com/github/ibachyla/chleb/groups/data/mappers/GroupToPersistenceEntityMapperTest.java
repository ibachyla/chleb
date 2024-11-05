package com.github.ibachyla.chleb.groups.data.mappers;

import com.github.ibachyla.chleb.groups.TestValues;
import com.github.ibachyla.chleb.groups.data.entities.GroupEntity;
import com.github.ibachyla.chleb.groups.models.entities.Group;
import com.github.ibachyla.chleb.mappers.Mapper;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
final class GroupToPersistenceEntityMapperTest {

  @InjectSoftAssertions
  SoftAssertions softly;

  final Mapper<Group, GroupEntity> mapper = new GroupToPersistenceEntityMapper();

  @Test
  void doMap() {
    // Arrange
    Group group = Group.builder()
        .id(TestValues.groupId())
        .name(TestValues.groupName())
        .build();

    // Act
    GroupEntity groupEntity = mapper.map(group);

    // Assert
    softly.assertThat(groupEntity.id()).isEqualTo(group.id());
    softly.assertThat(groupEntity.name()).isEqualTo(group.name());
  }
}