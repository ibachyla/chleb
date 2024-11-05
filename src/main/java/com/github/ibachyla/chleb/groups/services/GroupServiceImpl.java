package com.github.ibachyla.chleb.groups.services;

import com.github.ibachyla.chleb.app.services.AboutService;
import com.github.ibachyla.chleb.groups.data.entities.GroupEntity;
import com.github.ibachyla.chleb.groups.data.repositories.GroupRepository;
import com.github.ibachyla.chleb.groups.models.entities.Group;
import com.github.ibachyla.chleb.mappers.Mapper;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Group service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;
  private final Mapper<Group, GroupEntity> groupToPersistenceEntityMapper;
  private final AboutService aboutService;

  @Override
  @Transactional
  public UUID createDefaultGroup() {
    String groupName = aboutService.getDefaultGroupSlug();

    return groupRepository.findByName(groupName)
        .map(GroupEntity::id)
        .orElseGet(() -> {
          log.info("Creating default group with name: {}", groupName);

          Group defaultGroup = Group.builder()
              .name(groupName)
              .build();

          return groupRepository.save(groupToPersistenceEntityMapper.map(defaultGroup)).id();
        });
  }
}
