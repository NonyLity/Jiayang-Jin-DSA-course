package oy.tol.tra;

/**
 * A simple array of student grades to be used in testing
 * misbehaving algorithm for reversing the array.
 */
public class Algorithms<T extends Comparable<T>> {

   private T[] grades = null;

   /**
    * A constructor for building IntArrays.
    * @param grades the plain Java integer array with numbers to add.
    */
   public Algorithms(T[] grades) {
      this.grades = grades.clone();
   }

   /**
    * The method to reverse the internal Java int array.
    */
   public static <T extends Comparable<T>> void reverse(T[] grades) {
      int i = 0;
      while (i < grades.length / 2) {
         T temp = grades[i];
         grades[i] = grades[grades.length - i - 1];
         grades[grades.length - i - 1] = temp;
         i++;
      }
   }

   /**
    * Sorts the array to ascending order.
    */
   public static <T extends Comparable<T>> void sort(T[] grades) {
      int n = grades.length;
      for (int i = 1; i < n; i++) {
          boolean flag = true;
          for (int j = 0; j < n - i; j++) {
              if (grades[j].compareTo(grades[j + 1]) > 0) {
                  T temp = grades[j];
                  grades[j] = grades[j + 1];
                  grades[j + 1] = temp;
                  flag = false;
              }
          }
          if (flag) {
              break;
          }
      }
   } 

   /**
    * Perform binary search on a sorted array.
    * @param value the value to search for
    * @param array the sorted array to search in
    * @param fromIndex the index to start the search from
    * @param toIndex the index to end the search at
    * @return the index of the found element, -1 if not found
    */
   public static <T extends Comparable<T>> int binarySearch(T value, T[] array, int fromIndex, int toIndex) {
      while (fromIndex <= toIndex) {
         int mid = (fromIndex + toIndex) / 2;
         int cmp = value.compareTo(array[mid]);
         if (cmp == 0) {
            return mid;
         } else if (cmp < 0) {
            toIndex = mid - 1;
         } else {
            fromIndex = mid + 1;
         }
      }
      return -1;
   }

   /**
    * Perform quicksort on the array.
    * @param array the array to be sorted
    * @param begin the beginning index of the array
    * @param end the ending index of the array
    */
   public static <E extends Comparable<E>> void quickSort(E[] array, int begin, int end) {
      if (begin < end) {
         int partitionIndex = partition(array, begin, end);
         quickSort(array, begin, partitionIndex - 1);
         quickSort(array, partitionIndex + 1, end);
      }
   }

   /**
    * Partition the array for quicksort.
    * @param array the array to be partitioned
    * @param begin the beginning index of the array
    * @param end the ending index of the array
    * @return the partition index
    */
   private static <E extends Comparable<E>> int partition(E[] array, int begin, int end) {
      E pivot = array[end];
      int i = begin - 1;
      for (int j = begin; j < end; j++) {
         if (array[j].compareTo(pivot) < 0) {
            i++;
            E temp = array[i];
            array[i] = array[j];
            array[j] = temp;
         }
      }
      E temp = array[i + 1];
      array[i + 1] = array[end];
      array[end] = temp;
      return i + 1;
   }
}
