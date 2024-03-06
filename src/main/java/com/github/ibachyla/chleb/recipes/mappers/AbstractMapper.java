package com.github.ibachyla.chleb.recipes.mappers;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Abstract mapper.
 *
 * @param <S> the source type
 * @param <T> the target type
 */
public abstract class AbstractMapper<S, T> implements Mapper<S, T> {
  @Override
  public T map(S source) {
    notNull(source, "source cannot be null");
    return doMap(source);
  }

  protected abstract T doMap(S source);
}
