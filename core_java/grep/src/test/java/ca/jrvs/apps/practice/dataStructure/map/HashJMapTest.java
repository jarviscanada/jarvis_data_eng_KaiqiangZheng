package ca.jrvs.apps.practice.dataStructure.map;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashJMapTest {

  private HashJMap<String, Integer> map;

  @BeforeEach
  void setUp() {
    map = new HashJMap<>();
  }

  @Test
  void testPutAndGet() {
    assertNull(map.put("one", 1)); // New key returns null
    assertEquals(1, map.get("one"));  // The value for "one" should be 1
    assertEquals(1, map.size());  // The size of the map should be 1

    // Update the value for the existing key
    assertEquals(1, map.put("one", 11));  // The old value (1) is returned
    assertEquals(11, map.get("one"));  // The value for "one" should now be 11
  }

  @Test
  void testContainsKey() {
    map.put("a", 100);
    map.put("b", 200);

    assertTrue(map.containsKey("a"));
    assertTrue(map.containsKey("b"));
    assertFalse(map.containsKey("c"));
  }

  @Test
  void testSize() {
    assertEquals(0, map.size());
    map.put("x", 10);
    assertEquals(1, map.size());
    map.put("y", 20);
    assertEquals(2, map.size());
    map.put("x", 30);  // Updating the value for "x" does not increase the size
    assertEquals(2, map.size());
  }

  @Test
  void testEntrySet() {
    map.put("k1", 1);
    map.put("k2", 2);

    Set<Map.Entry<String, Integer>> entries = map.entrySet();
    assertEquals(2, entries.size());

    boolean foundK1 = entries.stream().anyMatch(e -> e.getKey().equals("k1") && e.getValue() == 1);
    boolean foundK2 = entries.stream().anyMatch(e -> e.getKey().equals("k2") && e.getValue() == 2);

    assertTrue(foundK1);
    assertTrue(foundK2);
  }

  @Test
  void testNullKeyOrValue() {
    assertThrows(NullPointerException.class, () -> map.put(null, 1));
    assertThrows(NullPointerException.class, () -> map.put("key", null));
  }

  @Test
  void testRehashing() {
    // Depends on the default capacity and load factor, which can trigger rehashing
    int n = 20; // Default capacity 16 * load factor 0.75 = 12 -> Inserting 20 will trigger rehash
    for (int i = 0; i < n; i++) {
      map.put("k" + i, i);
    }
    assertEquals(n, map.size());
    for (int i = 0; i < n; i++) {
      assertEquals(i, map.get("k" + i));
    }
  }
}
