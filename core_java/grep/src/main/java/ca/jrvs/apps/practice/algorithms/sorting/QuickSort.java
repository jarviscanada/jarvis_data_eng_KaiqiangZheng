package ca.jrvs.apps.practice.algorithms.sorting;

public class QuickSort {

  // Public sorting function
  public void quickSort(int[] arr) {
    quickSort(arr, 0, arr.length - 1);
  }

  // Recursive quicksort function
  private void quickSort(int[] arr, int left, int right) {
    if (left >= right) {
      return; // Base case for recursion
    }

    // Get the final position of the pivot
    int pivotIndex = partition(arr, left, right);

    // Recursively sort the left and right partitions
    quickSort(arr, left, pivotIndex - 1);
    quickSort(arr, pivotIndex + 1, right);
  }

  // Partition the array and return the pivot's final index
  private int partition(int[] arr, int left, int right) {
    int pivot = arr[right]; // Choose the last element as the pivot
    int i = left - 1;       // i tracks the end of the region with elements smaller than pivot

    // Traverse all elements except the pivot
    for (int j = left; j < right; j++) {
      if (arr[j] <= pivot) { // If current element <= pivot
        i++;
        swap(arr, i, j);   // Move it into the smaller elements region
      }
    }

    // Place the pivot in its correct sorted position
    swap(arr, i + 1, right);

    return i + 1; // Return the pivot's final index
  }

  // Swap two elements in the array
  private void swap(int[] arr, int a, int b) {
    int temp = arr[a];
    arr[a] = arr[b];
    arr[b] = temp;
  }

  // Test the implementation
  public static void main(String[] args) {
    QuickSort qs = new QuickSort();
    int[] arr = {10, 7, 8, 9, 1, 5};

    qs.quickSort(arr);

    for (int n : arr) {
      System.out.print(n + " ");
    }
  }
}

