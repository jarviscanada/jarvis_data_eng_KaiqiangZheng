package ca.jrvs.apps.practice.dataStructure.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayListAPIs {

  public static void main(String[] args) {
    // No need to specify type in <>
    // 1. Create ArrayList
    List<String> animals = new ArrayList<>();
    System.out.println("Initial: " + animals);

    // 2. Add elements using add()
    animals.add("Lion");
    animals.add("Tiger");
    animals.add("Dog");
    System.out.println("After add: " + animals);

    // Insert at a specified position
    animals.add(1, "Cat");
    System.out.println("After insert at index 1: " + animals);

    // 3. Get and modify elements using get(), set()
    String firstElement = animals.get(0);
    System.out.println("First element: " + firstElement);

    animals.set(2,"Wolf");
    System.out.println("After set index 2 to 'Wolf': " + animals);

    // 4. Remove elements using remove()
    animals.remove(0);                 // Remove by index
    animals.remove("Dog");            // Remove by object
    System.out.println("After remove: " + animals);

    // 5. Search for elements using contains() O(n), indexOf()
    System.out.println("Contains 'Cat'? " + animals.contains("Cat"));
    System.out.println("Index of 'wolf': " + animals.indexOf("wolf"));
    System.out.println("Index of 'Wolf': " + animals.indexOf("Wolf"));

    // 6. List information using size() not length() unlike array, isEmpty()
    System.out.println("Size: " + animals.size());
    System.out.println("Is empty? " + animals.isEmpty());

    // 7. Traverse the list using for-each loop
    for (String a : animals) {
      System.out.print(a + " ");
    }
    System.out.println();

    // 8. Convert to an array using toArray()
    // String[] array = animals.toArray(new String[animals.size()]); not recommended
    String[] array = animals.toArray(new String[0]);
    System.out.println("Array: " + java.util.Arrays.toString(array));

    // 9. Bulk operations using addAll(), removeAll()
    //List<String> moreAnimals = List.of("Dog", "Horse", "Pig"); //Java 9 above for using List.of
    //List<String> moreAnimals = new ArrayList<>(Arrays.asList("Dog", "Horse", "Pig"));
    List<String> moreAnimals = Arrays.asList("Dog", "Horse", "Pig");
    animals.addAll(moreAnimals);
    System.out.println("After addAll: " + animals);

    //animals.removeAll(List.of("Cat", "Pig")); //Java 9 above for using  List.of
    animals.removeAll(Arrays.asList("Cat", "Pig"));
    System.out.println("After removeAll: " + animals);

    // 10. Sorting using Collections.sort()
    Collections.sort(animals);
    System.out.println("Sorted: " + animals);
    //System.out.println(animals.sort(String::compareToIgnoreCase)); // void method, no return
    animals.sort(String::compareToIgnoreCase);
    System.out.println("Sorted: " + animals);

    // 11. Sublist using subList()
    List<String> sub = animals.subList(0, 2);
    System.out.println("SubList (0,2): " + sub);

    // 12. Clear the list using clear()
    animals.clear();
    System.out.println("After clear: " + animals);
    System.out.println("Is empty now? " + animals.isEmpty());
  }


}
