package ca.jrvs.apps.practice.dataStructure.map;

import java.util.*;

public class employeeHashMapAPIs{
  public static void main(String[] args) {
    // Create a Map to store Employee objects and their corresponding job history (List of Strings)
    Map<employeeMap, List<String>> jobHistory = new HashMap<>();

    // Create employeeMapMap objects
    employeeMap e1 = new employeeMap(1, "Alice", "Lee");
    employeeMap e2 = new employeeMap(2, "Bob", "Smith");
    employeeMap e3 = new employeeMap(1, "Alice", "Lee"); // e3 has the same content as e1 (same ID and name)

    // Add job history for employeeMaps
    jobHistory.put(e1, new ArrayList<>(Arrays.asList("Google", "Amazon")));
    jobHistory.put(e2, new ArrayList<>(Arrays.asList("Facebook")));
    jobHistory.put(e3, new ArrayList<>(Arrays.asList("Microsoft", "Tencent"))); //This will overwrite the put e1
    jobHistory.get(e3).add("RBC");

    // Demonstrate the effect of equals/hashCode
    // Check if the map contains the key `e3` (with the same values as `e1`), should return true
    System.out.println("Map contains e3? " + jobHistory.containsKey(e3)); // true
    System.out.println("Map contains value of String FaceBook? " + jobHistory.containsValue("Facebook")); // false cuz this is string not List<String>
    System.out.println("Map contains value of List<String> FaceBook? " + jobHistory.containsValue(new ArrayList<>(Arrays.asList("Facebook")))); // true
    System.out.println("Size of the map " + jobHistory.size()); // 2
    System.out.println("Is the map empty? " + jobHistory.isEmpty()); // false

    // Add a job history entry for e3 (this will use e3's `equals()` and `hashCode()` to find `e1` in the map)
    jobHistory.computeIfAbsent(e3, k -> new ArrayList<>()).add("Netflix");

    // Iterate over the map to print out each employeeMap's job history
    for (Map.Entry<employeeMap, List<String>> entry : jobHistory.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }

    // Remove Bob (e2) from the map
    jobHistory.remove(e2);

    // Print the map after removing Bob
    System.out.println("After removing Bob:");
    jobHistory.forEach((e, history) -> System.out.println(e + " -> " + history));
  }
}
