package oy.tol.tra;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class Algorithms {
    
    public static <T extends Comparable<T>> void sort(T [] array) {
        int i = array.length-1;
        if (array  != null && array.length != 0){
        for (int j=0; j<i; j++){
           for (int k=0; k<i; k++){
              if(array[k].compareTo(array[k+1])>0){
                 T tmp = array[k];
                 array[k] = array[k+1];
                 array[k+1] = tmp;
              }
           }
        }
     }
   }

   public static <T> void reverse(T [] array) {
    if (array != null && array.length != 0)
      {
      for ( int i=0; i < array.length/2; i++) {
         T temp = array[i];
         array[i] = array[array.length-i-1];
         array[array.length-i-1] = temp;
     }
   }
   }


public static <T extends Comparable<T>> int binarySearch(T aValue, T [] fromArray, int fromIndex, int toIndex) {
    int middle;
    while(fromIndex<=toIndex){
        middle=fromIndex+(toIndex-fromIndex)/2;
        if(aValue.compareTo(fromArray[middle])>0){
            fromIndex=middle+1;
        }else if(aValue.compareTo(fromArray[middle])<0){
            toIndex=middle-1;
        }else{
            return middle;
        }
    }
    return -1;
}

public static <T>void swap(T[] array,int index1,int index2){
    T tmp=array[index1];
    array[index1]=array[index2];
    array[index2]=tmp;
}
private static <E extends Comparable<E>> int partition(E [] array, int begin, int end) {
    E benchmark=array[begin];
    int left=begin;
    int right=end;
    while(left!=right) {
       while((left<right)&&array[right].compareTo(benchmark)>0) {
            right--;
        }
       while((left<right)&&array[left].compareTo(benchmark)<=0) {
            left++;
        }
        if(left<right){
            swap(array, left, right);
        }
    }
    array[begin]=array[left];
    array[left]=benchmark ;
return left;
}


public static <E extends Comparable<E>> void quickSort(E [] array, int begin, int end) {
if(begin>=end){
    return;
}
int pivot=partition(array, begin, end);
quickSort(array, begin, pivot-1);
quickSort(array, pivot+1, end);
}

public static <E extends Comparable<E>> void fastSort(E [] array) {
quickSort(array, 0, array.length - 1);
}

    public static <T> int partitionByRule(T [] pairs,int count,Predicate<T> judgeNullPredicate){
        int left = 0;
        int right = count - 1;
        while (left <= right) {
            while (left <= right && !judgeNullPredicate.test(pairs[left])) {
                left++;
            }

            while (left <= right && judgeNullPredicate.test(pairs[right])) {
                right--;
            }

            if (left < right) {
                swap(pairs, left, right);
                left++;
                right--;
            }
        }
        return left;
    }
    public static <T> void sortWithComparator( T[] array, Comparator<? super T> comparator) {
        Arrays.sort(array, comparator);
    }
}