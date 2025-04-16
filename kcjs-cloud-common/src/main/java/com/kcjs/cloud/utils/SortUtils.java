package com.kcjs.cloud.utils;

import java.util.Arrays;

public class SortUtils {

    /**
     * Bubble Sort implementation.
     * Time Complexity: O(n^2) in the worst and average case, O(n) in the best case.
     * Space Complexity: O(1)
     */
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    /**
     * Selection Sort implementation.
     * Time Complexity: O(n^2) in all cases.
     * Space Complexity: O(1)
     */
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int tmp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = tmp;
            }
        }
    }

    /**
     * Insertion Sort implementation.
     * Time Complexity: O(n^2) in the worst and average case, O(n) in the best case.
     * Space Complexity: O(1)
     */
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * Quick Sort implementation.
     * Time Complexity: O(n log n) in the average case, O(n^2) in the worst case.
     * Space Complexity: O(log n) due to recursion stack
     */
    public static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(arr, left, right);
            quickSort(arr, left, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, right);
        }
    }

    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        int tmp = arr[i + 1]; arr[i + 1] = arr[right]; arr[right] = tmp;
        return i + 1;
    }

    /**
     * Merge Sort implementation.
     * Time Complexity: O(n log n) in all cases.
     * Space Complexity: O(n) due to temporary array
     */
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            temp[k++] = arr[i] <= arr[j] ? arr[i++] : arr[j++];
        }

        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];

        System.arraycopy(temp, 0, arr, left, temp.length);
    }

    /**
     * Heap Sort implementation.
     * Time Complexity: O(n log n) in all cases.
     * Space Complexity: O(1)
     */
    public static void heapSort(int[] arr) {
        int n = arr.length;

        // 构建大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            int tmp = arr[0]; arr[0] = arr[i]; arr[i] = tmp;
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1, right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;

        if (largest != i) {
            int tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
            heapify(arr, n, largest);
        }
    }

    /**
     * Counting Sort implementation.
     * Time Complexity: O(n + k), where k is the range of the input.
     * Space Complexity: O(k)
     */
    public static void countingSort(int[] arr) {
        if (arr.length == 0) return;

        int max = Arrays.stream(arr).max().getAsInt();
        int[] count = new int[max + 1];

        for (int num : arr) {
            count[num]++;
        }

        int index = 0;
        for (int i = 0; i <= max; i++) {
            while (count[i]-- > 0) {
                arr[index++] = i;
            }
        }
    }

    public static void main(String[] args) {
        int[] array = {64, 34, 25, 12, 22, 11, 90};

        int[] arrayCopy = Arrays.copyOf(array, array.length);
        bubbleSort(arrayCopy);
        System.out.println("Bubble Sort: " + Arrays.toString(arrayCopy));

        arrayCopy = Arrays.copyOf(array, array.length);
        selectionSort(arrayCopy);
        System.out.println("Selection Sort: " + Arrays.toString(arrayCopy));

        arrayCopy = Arrays.copyOf(array, array.length);
        insertionSort(arrayCopy);
        System.out.println("Insertion Sort: " + Arrays.toString(arrayCopy));

        arrayCopy = Arrays.copyOf(array, array.length);
        quickSort(arrayCopy, 0, arrayCopy.length - 1);
        System.out.println("Quick Sort: " + Arrays.toString(arrayCopy));

        arrayCopy = Arrays.copyOf(array, array.length);
        mergeSort(arrayCopy, 0, arrayCopy.length - 1);
        System.out.println("Merge Sort: " + Arrays.toString(arrayCopy));

        arrayCopy = Arrays.copyOf(array, array.length);
        heapSort(arrayCopy);
        System.out.println("Heap Sort: " + Arrays.toString(arrayCopy));

        arrayCopy = Arrays.copyOf(array, array.length);
        countingSort(arrayCopy);
        System.out.println("Counting Sort: " + Arrays.toString(arrayCopy));
    }
}
