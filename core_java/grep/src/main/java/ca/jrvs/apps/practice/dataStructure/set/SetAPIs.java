package ca.jrvs.apps.practice.dataStructure.set;

import java.util.*;

public class SetAPIs {
  public static void main(String[] args) {

    System.out.println("===== HashSet Example (Unordered) =====");
    Set<String> hashSet = new HashSet<>();
    hashSet.add("Apple");
    hashSet.add("Banana");
    hashSet.add("Orange");
    hashSet.add("Apple"); // Duplicate elements will not be added

    System.out.println("HashSet: " + hashSet); // Output unordered
    System.out.println("Contains 'Banana'? " + hashSet.contains("Banana"));

    hashSet.remove("Orange");
    System.out.println("After removing Orange: " + hashSet);

    System.out.println("\n===== LinkedHashSet Example (Insertion Order) =====");
    Set<String> linkedSet = new LinkedHashSet<>();
    linkedSet.add("A");
    linkedSet.add("C");
    linkedSet.add("B");
    System.out.println("LinkedHashSet: " + linkedSet);

    System.out.println("\n===== TreeSet Example (Automatically Sorted) =====");
    Set<Integer> treeSet = new TreeSet<>();
    treeSet.add(50);
    treeSet.add(10);
    treeSet.add(30);
    System.out.println("TreeSet (sorted): " + treeSet);

    System.out.println("\n===== Set Traversal Methods =====");
    System.out.println("For-each:");
    for (String s : hashSet) {
      System.out.println(" " + s);
    }

    System.out.println("Iterator:");
    Iterator<String> it = hashSet.iterator();
    while (it.hasNext()) {
      System.out.println(" " + it.next());
    }

    System.out.println("forEach (Java 8):");
    hashSet.forEach(s -> System.out.println(" " + s));

    System.out.println("\n===== Set Operations (Union / Intersection / Difference) =====");
    Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
    Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5));

    // Union
    Set<Integer> union = new HashSet<>(set1);
    union.addAll(set2);
    System.out.println("Union (1 U 2): " + union);

    // Intersection
    Set<Integer> intersection = new HashSet<>(set1);
    intersection.retainAll(set2);
    System.out.println("Intersection (1 , 2): " + intersection); //No upside down U symbol

    // Difference
    Set<Integer> difference = new HashSet<>(set1);
    difference.removeAll(set2);
    System.out.println("Difference (1 - 2): " + difference);

    System.out.println("\n===== Convert Between Set and List =====");
    List<Integer> list = Arrays.asList(1, 2, 2, 3);
    Set<Integer> setFromList = new HashSet<>(list);
    System.out.println("List to Set (auto remove duplicates): " + setFromList);

    List<Integer> listFromSet = new ArrayList<>(setFromList);
    System.out.println("Set to List: " + listFromSet);

    System.out.println("\n===== Basic Set Methods (remove, size, isEmpty, clear) =====");
    Set<String> basicSet = new HashSet<>();
    basicSet.add("Red");
    basicSet.add("Green");
    basicSet.add("Blue");

    System.out.println("Initial Set: " + basicSet);
    System.out.println("Size: " + basicSet.size());
    System.out.println("Is empty? " + basicSet.isEmpty());

    basicSet.remove("Green");
    System.out.println("After removing 'Green': " + basicSet);

    basicSet.clear();
    System.out.println("After clear(): " + basicSet);
    System.out.println("Is empty after clear? " + basicSet.isEmpty());

    System.out.println("\n===== Completed =====");
  }
}
