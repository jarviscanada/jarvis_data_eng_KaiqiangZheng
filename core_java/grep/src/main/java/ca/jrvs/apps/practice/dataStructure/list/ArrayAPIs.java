package ca.jrvs.apps.practice.dataStructure.list;

import java.util.Arrays;
import java.util.List;

public class ArrayAPIs {

  public static void main (String[] args) {
    // ===== 1. Array Definition and Initialization =====
    //    create an array of integers
    //    int[] intArray = new int[10];
    //    intArray[0] = 100; // initialize first element
    //    intArray[1] = 200; // initialize second element
    //    intArray[2] = 300; // and so forth
    //
    //    //shortcut syntax to create and initialize an array
    //    int[] inlineArray = {100, 200, 300};
    int[] arr = {5,3,9,1,7};
    System.out.println(Arrays.toString(arr));

    // ===== 2. Access and Modify Elements =====
    System.out.println("First element: " + arr[0]);
    arr[2] = 10;
    System.out.println(Arrays.toString(arr));

    // ===== 3. Traverse the Array =====
    // for (int i = 0; i < array.length; i++) {} or
    for (int x : arr){
      System.out.print(x + " ");
    }
    System.out.println();

    // ===== 4. Array Length =====
    System.out.println("Array length: " + arr.length);

    // ===== 5. Arrays Utility Methods =====
    // Sorting
    Arrays.sort(arr);
    System.out.println("Sorted array: " + Arrays.toString(arr));

    // Binary Search
    int idx = Arrays.binarySearch(arr, 7);
    System.out.println("Index of 7: " + idx);


    // Array Copying
    int [] copy = Arrays.copyOf(arr, arr.length);
    int [] part = Arrays.copyOfRange(arr, 1,4);
    System.out.println("Full copy: " + Arrays.toString(copy));
    System.out.println("Partial copy: " + Arrays.toString(part));

    // Array Comparison
    int[] arr2 = {1, 7, 9, 10, 5};
    int[] arr3 = {1,3,5,7,10};
    System.out.println("Arrays equal: " + Arrays.equals(arr, arr2));
    System.out.println("Arrays equal: " + Arrays.equals(arr, arr3));

    // Filling Array
    Arrays.fill(arr, 0);
    System.out.println("Filled array: " + Arrays.toString(arr));

    // Converting Array to List
    String[] fruits = {"apple", "banana", "orange"};
    List<String> fruitList = Arrays.asList(fruits);
    System.out.println("List from array: " + fruitList);

    // ===== 6. Method Parameters and Return Values =====
    printArray(copy);
    int[] generated = generateArray();
    System.out.println("Generated array: " + Arrays.toString(generated));

    // ===== 7. Two-Dimensional Arrays =====
    int[][] matrix = new int[3][3];
    matrix[0][0] = 1;
    matrix[1][1] = 5;
    matrix[2][1] = 9;
    System.out.println("2D array (matrix): ");
    for (int i = 0; i < matrix.length; i++) {
      System.out.println(Arrays.toString(matrix[i]));
    }

    // ===== 8. Stream Operations =====
    int sum = Arrays.stream(copy).sum();
    int max = Arrays.stream(copy).max().getAsInt();
    System.out.println("Sum of copy array: " + sum);
    System.out.println("Max of copy array: " + max);

  }
  // Method to print array
  public static void printArray(int[] arr) {
    System.out.println("Printing array inside method:");
    for (int x : arr) System.out.print(x + " ");
    System.out.println();
  }

  // Method to return an array
  public static int[] generateArray() {
    return new int[]{2, 4, 6, 8};
  }
}