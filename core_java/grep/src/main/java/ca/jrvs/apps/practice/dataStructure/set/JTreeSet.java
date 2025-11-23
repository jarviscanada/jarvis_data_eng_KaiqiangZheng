package ca.jrvs.apps.practice.dataStructure.set;

import java.util.Objects;

public class JTreeSet<E extends Comparable<E>> implements JSet<E> {

  private Node<E> root;
  private int size;

  private static class Node<E> {
    E value;
    Node<E> left, right;

    Node(E value) {
      this.value = value;
    }
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean contains(Object o) {
    if (o == null) throw new NullPointerException();
    @SuppressWarnings("unchecked")
    E value = (E) o;
    return containsNode(root, value);
  }

  private boolean containsNode(Node<E> node, E value) {
    if (node == null) return false;
    int cmp = value.compareTo(node.value);
    if (cmp < 0) return containsNode(node.left, value);
    else if (cmp > 0) return containsNode(node.right, value);
    else return true;
  }

  @Override
  public boolean add(E e) {
    if (e == null) throw new NullPointerException();
    if (contains(e)) return false; // no duplicates
    root = addNode(root, e);
    size++;
    return true;
  }

  private Node<E> addNode(Node<E> node, E value) {
    if (node == null) return new Node<>(value);
    int cmp = value.compareTo(node.value);
    if (cmp < 0) node.left = addNode(node.left, value);
    else if (cmp > 0) node.right = addNode(node.right, value);
    return node;
  }

  @Override
  public boolean remove(Object o) {
    if (o == null) throw new NullPointerException();
    @SuppressWarnings("unchecked")
    E value = (E) o;
    if (!contains(value)) return false;
    root = removeNode(root, value);
    size--;
    return true;
  }

  private Node<E> removeNode(Node<E> node, E value) {
    if (node == null) return null;
    int cmp = value.compareTo(node.value);
    if (cmp < 0) node.left = removeNode(node.left, value);
    else if (cmp > 0) node.right = removeNode(node.right, value);
    else {
      // node to remove found
      if (node.left == null) return node.right;
      if (node.right == null) return node.left;

      // node with two children: get smallest from right subtree
      Node<E> minNode = findMin(node.right);
      node.value = minNode.value;
      node.right = removeNode(node.right, minNode.value);
    }
    return node;
  }

  private Node<E> findMin(Node<E> node) {
    while (node.left != null) node = node.left;
    return node;
  }

  @Override
  public void clear() {
    root = null;
    size = 0;
  }
}
