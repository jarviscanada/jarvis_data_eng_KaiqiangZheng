package ca.jrvs.apps.practice.dataStructure.stackQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LinkedJListJDequeTest {

  private LinkedJListJDeque<Integer> deque;

  @BeforeEach
  void setUp() {
    deque = new LinkedJListJDeque<>();
  }

  // -----------------------------
  // Queue Tests (add, remove, peek)
  // -----------------------------

  @Test
  void testQueueAddAndPeek() {
    assertTrue(deque.add(10));
    assertTrue(deque.add(20));

    assertEquals(10, deque.peek());   // queue head
  }

  @Test
  void testQueueRemove() {
    deque.add(10);
    deque.add(20);

    assertEquals(10, deque.remove());
    assertEquals(20, deque.remove());
  }

  @Test
  void testQueueRemove_ThrowsException() {
    assertThrows(NoSuchElementException.class, () -> deque.remove());
  }

  // -----------------------------
  // Stack Tests (push, pop, peek)
  // -----------------------------

  @Test
  void testStackPushAndPeek() {
    deque.push(1);
    deque.push(2);
    deque.push(3);

    assertEquals(3, deque.peek());  // stack top = head
  }

  @Test
  void testStackPop() {
    deque.push(1);
    deque.push(2);
    deque.push(3);

    assertEquals(3, deque.pop());
    assertEquals(2, deque.pop());
    assertEquals(1, deque.pop());
  }

  @Test
  void testStackPop_ThrowsException() {
    assertThrows(NoSuchElementException.class, () -> deque.pop());
  }

  // -----------------------------
  // Null Tests
  // -----------------------------

  @Test
  void testAddNull_ThrowsException() {
    assertThrows(NullPointerException.class, () -> deque.add(null));
  }

  @Test
  void testPushNull_ThrowsException() {
    assertThrows(NullPointerException.class, () -> deque.push(null));
  }

  // -----------------------------
  // Mixed Deque Tests
  // -----------------------------

  @Test
  void testMixedOperations() {
    deque.add(10);   // queue add -> tail
    deque.push(20);  // stack push -> head
    deque.add(30);

    // Current list: [20, 10, 30]

    assertEquals(20, deque.peek());
    assertEquals(20, deque.pop());    // pop stack head

    assertEquals(10, deque.remove()); // remove queue head
    assertEquals(30, deque.remove());
  }

  @Test
  void testPeekOnEmpty() {
    assertNull(deque.peek());
  }
}
