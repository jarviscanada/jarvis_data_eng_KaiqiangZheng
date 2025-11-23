package ca.jrvs.apps.practice.dataStructure.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayJListTest {

  //private ArrayJList<String> list;
  private JList<String> list;

  @BeforeEach
  void setUp() {
    list = new ArrayJList<>();
  }

  @Test
  void testAddAndSize() {
    assertEquals(0, list.size());
    list.add("A");
    list.add("B");
    list.add("C");
    assertEquals(3, list.size());
    assertEquals("C",list.get(2));
  }

  @Test
  void testAddAutoResize() {
    // DEFAULT_CAPACITY = 10, more than 10, will double the size
    for (int i = 0; i < 20; i++) {
      list.add("item-" + i);
    }
    assertEquals(20, list.size());
    assertEquals("item-0", list.get(0));
    assertEquals("item-19", list.get(19));
  }

  @Test
  void testIsEmpty() {
    assertTrue(list.isEmpty());
    list.add("A");
    assertFalse(list.isEmpty());
  }

  @Test
  void testGet() {
    list.add("Apple");
    list.add("Banana");
    assertEquals("Apple", list.get(0));
    assertEquals("Banana", list.get(1));
  }

  @Test
  void testGetIndexOutOfBounds() {
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
  }

  @Test
  void testContainsAndIndexOf() {
    list.add("A");
    list.add("B");
    list.add("C");
    assertTrue(list.contains("A"));
    assertTrue(list.contains("C"));
    assertFalse(list.contains("X"));
    assertEquals(1, list.indexOf("B"));
    assertEquals(-1, list.indexOf("NotExist"));
  }

  @Test
  void testToArray() {
    list.add("A");
    list.add("B");

    Object[] arr = list.toArray();

    assertEquals(2, arr.length);
    assertEquals("A", arr[0]);
    assertEquals("B", arr[1]);
  }

  @Test
  void testRemove() {
    list.add("A");
    list.add("B");
    list.add("C");

    String removed = list.remove(1);

    assertEquals("B", removed);
    assertEquals(2, list.size());
    assertEquals("C", list.get(1)); // shifted left
  }

  @Test
  void testRemoveOutOfBounds() {
    list.add("A");
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(10));
  }

  @Test
  void testClear() {
    list.add("A");
    list.add("B");
    list.add("C");

    list.clear();

    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
    assertEquals(0, list.toArray().length);
  }
}
