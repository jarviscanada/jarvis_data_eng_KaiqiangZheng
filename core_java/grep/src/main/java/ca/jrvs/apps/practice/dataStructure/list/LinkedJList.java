package ca.jrvs.apps.practice.dataStructure.list;

public class LinkedJList<E> implements JList<E> {

  private static class Node<E> {

    E value;
    Node<E> next;

    Node(E value) {
      this.value = value;
      //this.next = null; By default is null as well
    }
  }
    private Node<E> head;
    int size;

    public LinkedJList(){
      head = null;
      size = 0;
    }

  @Override
  public boolean add(E e) {
    if (e == null) {
      throw new NullPointerException("Null elements not allowed");
    }

    Node<E> newNode = new Node<>(e);

    if (head == null) {            // empty linkedlist
      head = newNode;
    } else {
      Node<E> curr = head;
      while (curr.next != null) { // upon the last one
        curr = curr.next;
      }
      curr.next = newNode;
    }

    size++;
    return true;
  }

  @Override
  public Object[] toArray() {
    Object[] arr = new Object[size];
    Node<E> curr = head;

    int i = 0;
    while (curr != null) {
      arr[i++] = curr.value;
      curr = curr.next;
    }
    return arr;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public int indexOf(Object o) {
    if (o == null) {
      throw new NullPointerException("Null not allowed");
    }

    Node<E> curr = head;
    int index = 0;

    while (curr != null) {
      if (o.equals(curr.value)) {
        return index;
      }
      curr = curr.next;
      index++;
    }

    return -1;
  }

  @Override
  public boolean contains(Object o) {
    return indexOf(o) != -1;
  }

  @Override
  public E get(int index) {
    checkIndex(index);

    Node<E> curr = head;
    for (int i = 0; i < index; i++) {
      curr = curr.next;
    }

    return curr.value;
  }

  @Override
  public E remove(int index) {
    checkIndex(index);

    if (index == 0) {   // Delete head
      E val = head.value;
      head = head.next;
      size--;
      return val;
    }

    Node<E> prev = head;
    for (int i = 0; i < index - 1; i++) {
      prev = prev.next;
    }

    Node<E> removed = prev.next;
    prev.next = removed.next;

    size--;
    return removed.value;
  }

  @Override
  public void clear() {
    head = null;
    size = 0;
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
    }
  }
 //helper for JDeque for Stack
  public void addFirst(E e) {
    Node newNode = new Node(e);
    newNode.next = head;
    head = newNode;
    size++;
  }
}
