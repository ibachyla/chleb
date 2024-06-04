package com.github.ibachyla.chleb.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

final class ArrayUtilsTest {

  @Test
  void fillWithZeroes() {
    // Arrange
    char[] array = new char[5];

    // Act
    ArrayUtils.fillWithZeroes(array);

    // Assert
    assertArrayEquals(new char[] {'0', '0', '0', '0', '0'}, array);
  }

  @Test
  void fillWithZeroesEmptyArray() {
    // Arrange
    char[] array = new char[0];

    // Act
    ArrayUtils.fillWithZeroes(array);

    // Assert
    assertArrayEquals(new char[0], array);
  }


}