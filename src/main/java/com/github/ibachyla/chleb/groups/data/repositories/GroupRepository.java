package com.github.ibachyla.chleb.groups.data.repositories;

import com.github.ibachyla.chleb.groups.data.entities.GroupEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Group repository.
 */
@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {
  Optional<GroupEntity> findByName(String groupName);
}
