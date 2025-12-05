package ca.jrvs.apps.practice.dataStructure.list;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedJListTest {

  @Test
  public void testAddAndSize() {
    LinkedJList<Integer> list = new LinkedJList<>();
    assertTrue(list.isEmpty());

    list.add(10);
    list.add(20);
    list.add(30);

    assertEquals(3, list.size());
    assertFalse(list.isEmpty());
  }

  @Test
  public void testGet() {
    LinkedJList<String> list = new LinkedJList<>();
    list.add("A");
    list.add("B");
    list.add("C");

    assertEquals("A", list.get(0));
    assertEquals("B", list.get(1));
    assertEquals("C", list.get(2));

    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
  }

  @Test
  public void testContainsAndIndexOf() {
    LinkedJList<String> list = new LinkedJList<>();
    list.add("X");
    list.add("Y");
    list.add("Z");

    assertTrue(list.contains("Y"));
    assertFalse(list.contains("A"));

    assertEquals(1, list.indexOf("Y"));
    assertEquals(-1, list.indexOf("A"));
  }

  @Test
  public void testRemove() {
    LinkedJList<Integer> list = new LinkedJList<>();
    list.add(5);
    list.add(10);
    list.add(15);

    assertEquals(10, list.remove(1));   // remove middle
    assertEquals(2, list.size());

    assertEquals(5, list.remove(0));    // remove head
    assertEquals(1, list.size());

    assertEquals(15, list.remove(0));   // remove last
    assertTrue(list.isEmpty());

    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
  }

  @Test
  public void testClear() {
    LinkedJList<Integer> list = new LinkedJList<>();
    list.add(1);
    list.add(2);
    list.add(3);

    list.clear();
    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
  }

  @Test
  public void testToArray() {
    LinkedJList<String> list = new LinkedJList<>();
    list.add("hello");
    list.add("world");

    Object[] arr = list.toArray();

    assertEquals(2, arr.length);
    assertEquals("hello", arr[0]);
    assertEquals("world", arr[1]);
  }

  @Test
  public void testAddNull() {
    LinkedJList<String> list = new LinkedJList<>();
    assertThrows(NullPointerException.class, () -> list.add(null));
  }

  @Test
  public void testIndexOfNull() {
    LinkedJList<String> list = new LinkedJList<>();
    list.add("A");

    assertThrows(NullPointerException.class, () -> list.indexOf(null));
  }

  @Test
  public void testContainsNull() {
    LinkedJList<String> list = new LinkedJList<>();
    list.add("A");

    assertThrows(NullPointerException.class, () -> list.contains(null));
  }
}
