package com.github.ibachyla.chleb.groups.data.mappers;

import com.github.ibachyla.chleb.groups.data.entities.GroupEntity;
import com.github.ibachyla.chleb.groups.models.entities.Group;
import com.github.ibachyla.chleb.mappers.AbstractMapper;
import org.springframework.stereotype.Component;

/**
 * Group to persistence entity mapper.
 */
@Component
public class GroupToPersistenceEntityMapper extends AbstractMapper<Group, GroupEntity> {

  @Override
  protected GroupEntity doMap(Group source) {
    GroupEntity groupEntity = new GroupEntity();
    groupEntity.id(source.id());
    groupEntity.name(source.name());

    return groupEntity;
  }
}
