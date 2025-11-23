package ca.jrvs.apps.practice.dataStructure.set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JTreeSetTest {

  private JTreeSet<Integer> treeSet;

  @BeforeEach
  void setUp() {
    treeSet = new JTreeSet<>();
  }

  @Test
  void testAddAndContains() {
    assertTrue(treeSet.add(5));
    assertTrue(treeSet.add(3));
    assertTrue(treeSet.add(7));

    // duplicates should not be added
    assertFalse(treeSet.add(5));

    assertTrue(treeSet.contains(5));
    assertTrue(treeSet.contains(3));
    assertTrue(treeSet.contains(7));
    assertFalse(treeSet.contains(10));

    // null should throw exception
    assertThrows(NullPointerException.class, () -> treeSet.add(null));
    assertThrows(NullPointerException.class, () -> treeSet.contains(null));
  }

  @Test
  void testSize() {
    assertEquals(0, treeSet.size());
    treeSet.add(1);
    treeSet.add(2);
    treeSet.add(3);
    assertEquals(3, treeSet.size());
    treeSet.remove(2);
    assertEquals(2, treeSet.size());
    treeSet.clear();
    assertEquals(0, treeSet.size());
  }

  @Test
  void testRemove() {
    treeSet.add(5);
    treeSet.add(3);
    treeSet.add(7);
    treeSet.add(6);
    treeSet.add(8);

    assertTrue(treeSet.remove(5)); // root node
    assertFalse(treeSet.contains(5));

    assertTrue(treeSet.remove(6)); // leaf node
    assertFalse(treeSet.contains(6));

    assertTrue(treeSet.remove(7)); // node with one child
    assertFalse(treeSet.contains(7));

    // removing non-existing element
    assertFalse(treeSet.remove(10));

    // null should throw exception
    assertThrows(NullPointerException.class, () -> treeSet.remove(null));
  }

  @Test
  void testClear() {
    treeSet.add(1);
    treeSet.add(2);
    treeSet.add(3);
    assertEquals(3, treeSet.size());

    treeSet.clear();
    assertEquals(0, treeSet.size());
    assertFalse(treeSet.contains(1));
    assertFalse(treeSet.contains(2));
  }
}
