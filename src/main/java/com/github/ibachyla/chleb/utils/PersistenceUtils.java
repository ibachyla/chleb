package com.github.ibachyla.chleb.utils;

import com.github.ibachyla.chleb.data.entities.IdentifiedEntity;
import java.util.UUID;
import org.hibernate.proxy.HibernateProxy;

/**
 * Persistence utilities.
 */
public final class PersistenceUtils {

  /**
   * Returns an ID of the entity making sure that additional DB queries are not made.
   *
   * @param entity entity
   * @return entity ID
   */
  public static UUID getId(IdentifiedEntity entity) {
    if (entity == null) {
      return null;
    }

    if (entity instanceof HibernateProxy) {
      return (UUID) ((HibernateProxy) entity).getHibernateLazyInitializer().getIdentifier();
    }

    return entity.id();
  }
}
