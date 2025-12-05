package ca.jrvs.apps.practice.dataStructure.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class JBSTreeTest {

  private JBSTree<Integer> bst;

  @BeforeEach
  void setUp() {
    bst = new JBSTree<>(Comparator.naturalOrder());
  }

  @Test
  void testInsertAndSearch() {
    assertEquals(10, bst.insert(10));
    assertEquals(5, bst.insert(5));
    assertEquals(15, bst.insert(15));

    // Search existing
    assertEquals(10, bst.search(10));
    assertEquals(5, bst.search(5));
    assertEquals(15, bst.search(15));

    // Search non-existing
    assertNull(bst.search(20));

    // Duplicate insertion
    assertThrows(IllegalArgumentException.class, () -> bst.insert(10));
  }

  @Test
  void testRemoveLeafNode() {
    bst.insert(10);
    bst.insert(5);
    bst.insert(15);

    assertEquals(5, bst.remove(5)); // leaf node
    assertNull(bst.search(5));
    assertEquals(2, bst.findHeight() + 1); // root and right child left
  }

  @Test
  void testRemoveNodeWithOneChild() {
    bst.insert(10);
    bst.insert(5);
    bst.insert(15);
    bst.insert(12); // left child of 15

    assertEquals(15, bst.remove(15)); // has one child
    assertNull(bst.search(15));
    assertEquals(12, bst.search(12)); // child promoted
  }

  @Test
  void testRemoveNodeWithTwoChildren() {
    bst.insert(10);
    bst.insert(5);
    bst.insert(15);
    bst.insert(12);
    bst.insert(18);

    assertEquals(15, bst.remove(15)); // has two children
    assertNull(bst.search(15));
    assertEquals(12, bst.search(12)); // in-order successor replaces
    assertEquals(18, bst.search(18));
  }

  @Test
  void testPreOrderTraversal() {
    bst.insert(10);
    bst.insert(5);
    bst.insert(15);
    bst.insert(3);
    bst.insert(7);

    Object[] expected = {10, 5, 3, 7, 15};
    assertArrayEquals(expected, bst.preOrder());
  }

  @Test
  void testInOrderTraversal() {
    bst.insert(10);
    bst.insert(5);
    bst.insert(15);
    bst.insert(3);
    bst.insert(7);

    Object[] expected = {3, 5, 7, 10, 15};
    assertArrayEquals(expected, bst.inOrder());
  }

  @Test
  void testPostOrderTraversal() {
    bst.insert(10);
    bst.insert(5);
    bst.insert(15);
    bst.insert(3);
    bst.insert(7);

    Object[] expected = {3, 7, 5, 15, 10};
    assertArrayEquals(expected, bst.postOrder());
  }

  @Test
  void testFindHeight() {
    assertThrows(NullPointerException.class, () -> bst.findHeight());

    bst.insert(10);
    assertEquals(0, bst.findHeight());
    bst.insert(5);
    bst.insert(15);
    assertEquals(1, bst.findHeight());
    bst.insert(3);
    bst.insert(7);
    assertEquals(2, bst.findHeight());
  }
}
