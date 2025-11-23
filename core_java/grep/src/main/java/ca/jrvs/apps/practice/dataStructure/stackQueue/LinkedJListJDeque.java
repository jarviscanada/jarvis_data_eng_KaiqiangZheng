package ca.jrvs.apps.practice.dataStructure.stackQueue;

import ca.jrvs.apps.practice.dataStructure.list.LinkedJList;
import java.util.NoSuchElementException;

public class LinkedJListJDeque<E> extends LinkedJList<E> implements JDeque<E> {

  // JQueue.add (enqueue at tail)
  @Override
  public boolean add(E e) {
    if (e == null)
      throw new NullPointerException();

    super.add(e); // add to tail (LinkedJList already append)
    return true;
  }

  // JQueue.remove (dequeue from head)
  @Override
  public E remove() {
    if (isEmpty())
      throw new NoSuchElementException();

    return super.remove(0); // remove head
  }

  // JQueue.peek (peek head)
  @Override
  public E peek() {
    if (isEmpty())
      return null;

    return super.get(0); // peek head
  }

  // JStack.push (stack push = add at head)
  @Override
  public void push(E e) {
    if (e == null)
      throw new NullPointerException();

    // insert at head
    super.addFirst(e);
  }

  // JStack.pop (stack pop = remove head)
  @Override
  public E pop() {
    if (isEmpty())
      throw new NoSuchElementException();

    return super.remove(0);
  }

  // JStack.peek (stack top = head) same as queue implementation
//  @Override
//  public E peekStack() {
//    if (isEmpty())
//      return null;
//
//    return super.get(0);
//  }
}
