package com.github.ibachyla.chleb.recipes.mappers;

/**
 * Mapper from one type to another.
 *
 * @param <S> the source type
 * @param <T> the target type
 */
public interface Mapper<S, T> {

  T map(S source);
}
