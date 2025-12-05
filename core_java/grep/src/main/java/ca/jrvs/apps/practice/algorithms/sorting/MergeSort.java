package ca.jrvs.apps.practice.algorithms.sorting;

import java.util.Arrays;

public class MergeSort {

  // Public sorting method
  public void mergeSort(int[] arr) {
    if (arr == null || arr.length <= 1) {
      return; // Return immediately for null or single-element arrays
    }
    mergeSort(arr, 0, arr.length - 1);
  }

  // Recursive merge sort function
  private void mergeSort(int[] arr, int left, int right) {
    if (left >= right) {
      return; // Base case: no need to sort
    }

    int mid = left + (right - left) / 2; // Avoid integer overflow
    mergeSort(arr, left, mid);           // Sort left half
    mergeSort(arr, mid + 1, right);      // Sort right half
    merge(arr, left, mid, right);        // Merge two sorted halves
  }

  // Merge function: merge two sorted subarrays
  private void merge(int[] arr, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;

    int[] L = new int[n1];
    int[] R = new int[n2];

    // Copy data to temporary arrays
    for (int i = 0; i < n1; i++) L[i] = arr[left + i];
    for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

    int i = 0, j = 0, k = left;

    // Merge the two arrays
    while (i < n1 && j < n2) {
      if (L[i] <= R[j]) {
        arr[k++] = L[i++];
      } else {
        arr[k++] = R[j++];
      }
    }

    // Copy remaining elements
    while (i < n1) arr[k++] = L[i++];
    while (j < n2) arr[k++] = R[j++];
  }

  // Test the implementation
  public static void main(String[] args) {
    MergeSort ms = new MergeSort();
    int[] arr = {12, 11, 13, 5, 6, 7};

    ms.mergeSort(arr);

    System.out.println(Arrays.toString(arr));
  }
}

