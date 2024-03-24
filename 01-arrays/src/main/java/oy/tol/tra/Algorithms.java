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

//    public T[] getArray() {
//        return grades;
//    }
}
