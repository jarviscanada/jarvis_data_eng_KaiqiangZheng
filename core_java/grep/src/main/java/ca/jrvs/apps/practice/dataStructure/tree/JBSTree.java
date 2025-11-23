package ca.jrvs.apps.practice.dataStructure.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A simplified BST implementation
 *
 * @param <E> type of object to be stored
 */
public class JBSTree<E> implements JTree<E> {

  private Node<E> root;
  private Comparator<E> comparator;
  private int size = 0;

  /**
   * Create a new BST
   *
   * @param comparator the comparator that will be used to order this map.
   * @throws IllegalArgumentException if comparator is null
   */
  public JBSTree(Comparator<? super E> comparator) {
    if (comparator == null) throw new IllegalArgumentException("Comparator cannot be null");
    this.comparator = (Comparator<E>) comparator;
  }

  /**
   * Insert an object into the BST.
   * Please review the BST property.
   *
   * @param object item to be inserted
   * @return inserted item
   * @throws IllegalArgumentException if the object already exists
   */
  @Override
  public E insert(E object) {
    if (object == null) throw new NullPointerException();
    if (root == null) {
      root = new Node<>(object, null);
      size++;
      return object;
    }

    Node<E> parent = null;
    Node<E> current = root;
    while (current != null) {
      int cmp = comparator.compare(object, current.value);
      if (cmp == 0) throw new IllegalArgumentException("Duplicate value");
      else if (cmp < 0) {
        parent = current;
        current = current.left;
      } else {
        parent = current;
        current = current.right;
      }
    }

    Node<E> newNode = new Node<>(object, parent);
    if (comparator.compare(object, parent.value) < 0) parent.left = newNode;
    else parent.right = newNode;
    size++;
    return object;
  }


  /**
   * Search and return an object, return null if not found
   *
   * @param object to be found
   * @return the object if exists or null if not
   */
  @Override
  public E search(E object) {
    Node<E> node = getNode(root, object);
    return node == null ? null : node.value;
  }


  /**
   * Remove an object from the tree.
   *
   * @param object to be removed
   * @return removed object
   * @throws IllegalArgumentException if the object not exists
   */
  @Override
  public E remove(E object) {
    Node<E> node = getNode(root, object);
    if (node == null) throw new IllegalArgumentException("Value not exists");

    E removedValue = node.value;

    // case 1: leaf
    if (node.left == null && node.right == null) {
      replaceNodeInParent(node, null);
    }
    // case 2: one child
    else if (node.left == null) {
      replaceNodeInParent(node, node.right);
    }
    else if (node.right == null) {
      replaceNodeInParent(node, node.left);
    }
    // case 3: two children
    else {
      Node<E> successor = findMin(node.right);
      node.value = successor.value;   // overwrite value
      deleteNode(successor);          // remove successor node (NOT by value)
    }

    return removedValue;
  }

  /**
   * traverse the tree recursively
   *
   * @return all objects in pre-order
   */
  @Override
  public E[] preOrder() {
    List<E> list = new ArrayList<>();
    preOrderHelper(root, list);
    return (E[]) list.toArray();
  }

  private void preOrderHelper(Node<E> node, List<E> list) {
    if (node == null) return;
    list.add(node.value);
    preOrderHelper(node.left, list);
    preOrderHelper(node.right, list);
  }

  /**
   * traverse the tree recursively
   *
   * @return all objects in-order
   */
  @Override
  public E[] inOrder() {
    List<E> list = new ArrayList<>();
    inOrderHelper(root, list);
    return (E[]) list.toArray();
  }

  private void inOrderHelper(Node<E> node, List<E> list) {
    if (node == null) return;
    inOrderHelper(node.left, list);
    list.add(node.value);
    inOrderHelper(node.right, list);
  }

  /**
   * traverse the tree recursively
   *
   * @return all objects pre-order
   */
  @Override
  public E[] postOrder() {
    List<E> list = new ArrayList<>();
    postOrderHelper(root, list);
    return (E[]) list.toArray();
  }

  private void postOrderHelper(Node<E> node, List<E> list) {
    if (node == null) return;
    postOrderHelper(node.left, list);
    postOrderHelper(node.right, list);
    list.add(node.value);
  }

  /**
   * traverse through the tree and find out the tree height
   * @return height
   * @throws NullPointerException if the BST is empty
   */
  @Override
  public int findHeight() {
    if (root == null) throw new NullPointerException("BST is empty");
    return heightHelper(root);
  }


  private int heightHelper(Node<E> node) {
    if (node == null) return -1;
    return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
  }

  private Node<E> getNode(Node<E> current, E value) {
    while (current != null) {
      int cmp = comparator.compare(value, current.value);
      if (cmp == 0) return current;
      else if (cmp < 0) current = current.left;
      else current = current.right;
    }
    return null;
  }

  private void replaceNodeInParent(Node<E> node, Node<E> newNode) {
    if (node.parent == null) root = newNode;
    else if (node == node.parent.left) node.parent.left = newNode;
    else node.parent.right = newNode;
    if (newNode != null) newNode.parent = node.parent;
  }

  private Node<E> findMin(Node<E> node) {
    while (node.left != null) node = node.left;
    return node;
  }

  private void deleteNode(Node<E> node) {

    // case 1: leaf
    if (node.left == null && node.right == null) {
      replaceNodeInParent(node, null);
    }
    // case 2: one child
    else if (node.left == null) {
      replaceNodeInParent(node, node.right);
    }
    else if (node.right == null) {
      replaceNodeInParent(node, node.left);
    }
    // case 3 NEVER happens for successor (successor always has at most 1 child)
    size--;
  }


  static final class Node<E> {

    E value;
    Node<E> left;
    Node<E> right;
    Node<E> parent;

    public Node(E value, Node<E> parent) {
      this.value = value;
      this.parent = parent;
    }

    public E getValue() {
      return value;
    }

    public void setValue(E value) {
      this.value = value;
    }

    public Node<E> getLeft() {
      return left;
    }

    public void setLeft(Node<E> left) {
      this.left = left;
    }

    public Node<E> getRight() {
      return right;
    }

    public void setRight(Node<E> right) {
      this.right = right;
    }

    public Node<E> getParent() {
      return parent;
    }

    public void setParent(Node<E> parent) {
      this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Node)) {
        return false;
      }
      Node<?> node = (Node<?>) o;
      return getValue().equals(node.getValue()) &&
          Objects.equals(getLeft(), node.getLeft()) &&
          Objects.equals(getRight(), node.getRight()) &&
          getParent().equals(node.getParent());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getValue(), getLeft(), getRight(), getParent());
    }
  }

}