package lecture22;

import java.util.ArrayList;

class ArrayUtils {
  <T> ArrayList<T> interleave(ArrayList<T> arr1, ArrayList<T> arr2) {
    int arrSize = arr1.size();
    ArrayList<T> newArr = new ArrayList<>();
    for (int i = 0; i < arrSize; i++) {
      newArr.add(arr1.get(i));
      newArr.add(arr2.get(i));
    }
    return newArr;
  }

  <T> ArrayList<T> unshuffle(ArrayList<T> arr) {
    int arrSize = arr.size();
    ArrayList<T> newArr = new ArrayList<>();
    for (int i = 0; i < arrSize; i = i + 2) {
      newArr.add(arr.get(i));
    }
    for (int i = 1; i < arrSize; i = i + 2) {
      newArr.add(arr.get(i));
    }
    return newArr;
  }

  int findIdxOfMinInRest(ArrayList<String> arr, int restIndex) {
    int minIndex = restIndex;
    int arrSize = arr.size();
    for (int i = restIndex; i < arrSize; i++) {
      if (arr.get(i).compareTo(arr.get(minIndex)) < 0) {
        minIndex = i;
      }
    }
    return minIndex;
  }

  // EFFECT: Sorts the given list of strings alphabetically
  void sort(ArrayList<String> arr) {
    for (int idx = 0;                                   // (1)
        idx < arr.size();                              // (2)
        idx = idx + 1) {                               // (4)
      // (3)
      int idxOfMinValue = this.findIdxOfMinInRest(arr, idx);
      this.swap(arr, idx, idxOfMinValue);
    }
  }

  // EFFECT: Exchanges the values at the given two indices in the given array
  <T> void swap(ArrayList<T> arr, int index1, int index2) {
    T item1 = arr.get(index1);
    arr.set(index1, arr.get(index2));
    arr.set(index2, item1);
  }
}
