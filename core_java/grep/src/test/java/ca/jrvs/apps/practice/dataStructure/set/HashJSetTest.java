package ca.jrvs.apps.practice.dataStructure.set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashJSetTest {

  private HashJSet<String> set;

  @BeforeEach
  void setUp() {
    set = new HashJSet<>();
  }

  @Test
  void testAdd() {
    assertTrue(set.add("a"));    // add new element
    assertFalse(set.add("a"));   // duplicate should return false
    assertTrue(set.add("b"));
    assertEquals(2, set.size());
  }

  @Test
  void testContains() {
    set.add("x");
    set.add("y");
    assertTrue(set.contains("x"));
    assertTrue(set.contains("y"));
    assertFalse(set.contains("z"));
  }

  @Test
  void testRemove() {
    set.add("m");
    set.add("n");
    assertTrue(set.remove("m"));   // remove existing
    assertFalse(set.remove("m"));  // remove again
    assertEquals(1, set.size());
  }

  @Test
  void testSize() {
    assertEquals(0, set.size());
    set.add("1");
    set.add("2");
    assertEquals(2, set.size());
    set.remove("1");
    assertEquals(1, set.size());
  }

  @Test
  void testClear() {
    set.add("a");
    set.add("b");
    set.clear();
    assertEquals(0, set.size());
    assertFalse(set.contains("a"));
    assertFalse(set.contains("b"));
  }

  @Test
  void testNullHandling() {
    assertThrows(NullPointerException.class, () -> set.add(null));
    assertThrows(NullPointerException.class, () -> set.contains(null));
    assertThrows(NullPointerException.class, () -> set.remove(null));
  }

  @Test
  void testResize() {
    // Adding more elements than default capacity * load factor to trigger resize
    int initialCapacity = 16;
    int elementsToAdd = (int)(initialCapacity * 0.75) + 5;
    for (int i = 0; i < elementsToAdd; i++) {
      assertTrue(set.add("elem" + i));
    }
    assertEquals(elementsToAdd, set.size());

    // Make sure all elements are still accessible
    for (int i = 0; i < elementsToAdd; i++) {
      assertTrue(set.contains("elem" + i));
    }
  }
}
