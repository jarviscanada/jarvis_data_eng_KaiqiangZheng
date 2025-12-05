package ca.jrvs.apps.practice.dataStructure.list;

import java.util.LinkedList;
import java.util.Iterator;

public class LinkedListAPIs {

  private LinkedList<String> list = new LinkedList<>();

  // Add operations
  public void addExamples() {
    list.add("A");
    list.add("B");
    list.add(1,"C");
    list.addFirst("Start");
    list.addLast("End");
    list.offer("Offered");
    System.out.println("After add operations: " + list);
  }

  // Remove operations
  public void removeExamples() {
    System.out.println("Removed first: " + list.removeFirst());
    System.out.println("Removed last: " + list.removeLast());
    System.out.println("Poll (remove head): " + list.poll());
    System.out.println("After remove operations: " + list);
  }

  // Access operations
  public void accessExamples() {
    System.out.println("First element: " + list.getFirst());
    System.out.println("Last element: " + list.getLast());
    if (list.size() > 2) {
      System.out.println("Element at index 2: " + list.get(2));
    }
  }

  // Other operations
  public void otherExamples() {
    System.out.println("Contains 'B'? " + list.contains("B"));
    System.out.println("Size: " + list.size());

    // Traverse with iterator
    System.out.print("Iterating: ");
    Iterator<String> it = list.iterator();
    while (it.hasNext()) {
      System.out.print(it.next() + " ");
    }
    System.out.println();
  }

  public static void main(String[] args) {
    LinkedListAPIs api = new LinkedListAPIs();

    api.addExamples();
    api.removeExamples();
    api.accessExamples();
    api.otherExamples();
  }
}
