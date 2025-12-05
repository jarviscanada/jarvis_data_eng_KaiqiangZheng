package ca.jrvs.apps.practice.dataStructure;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EmployeeSort {

  // Comparator for sorting by salary (ascending order)
  public static class SalaryComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee e1, Employee e2) {
      return Long.compare(e1.getSalary(), e2.getSalary());  // Compare salaries
    }
  }

  public static void main(String[] args) {

    List<Employee> employees = Arrays.asList(
        new Employee(1, "Alice", 30, 60000),
        new Employee(2, "Bob", 24, 50000),
        new Employee(3, "Tom", 27, 70000)
    );

    // -----------------------------------
    // 1. Using Comparable (sort by age)
    // -----------------------------------
    System.out.println("Sort by age (Comparable):");
    employees.sort(null);   // null = use Employee.compareTo
    employees.forEach(e -> System.out.println(e.getName() + " - " + e.getAge()));

    // -----------------------------------
    // 2. Using Comparator (sort by salary)
    // -----------------------------------
    System.out.println("\nSort by salary (Comparator):");
    employees.sort(new SalaryComparator());
    employees.forEach(e -> System.out.println(e.getName() + " - " + e.getSalary()));
  }
}
