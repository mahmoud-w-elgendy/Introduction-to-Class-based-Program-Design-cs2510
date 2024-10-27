package lecture28;

import java.util.ArrayList;

public class ArrayUtils {
  // EFFECT: Sorts the provided list according to the given comparator
  <T> void mergesort(ArrayList<T> arr, IComparator<T> comp) {
    // Create a temporary array
    ArrayList<T> temp = new ArrayList<T>();
    // Make sure the temporary array is exactly as big as the given array
    for (int i = 0; i < arr.size(); i = i + 1) {
      temp.add(arr.get(i));
    }
    mergesortHelp(arr, temp, comp, 0, arr.size());
  }
  // EFFECT: Sorts the provided list in the region [loIdx, hiIdx)
  // according to the given comparator.
  //         Modifies both lists in the range [loIdx, hiIdx)
  <T> void mergesortHelp(ArrayList<T> source, ArrayList<T> temp, IComparator<T> comp,
      int loIdx, int hiIdx) {
    // Step 0: stop when finished
    if (hiIdx - loIdx <= 1) {
      return; // nothing to sort
    }
    // Step 1: find the middle index
    int midIdx = (loIdx + hiIdx) / 2;
    // Step 2: recursively sort both halves
    mergesortHelp(source, temp, comp, loIdx, midIdx);
    mergesortHelp(source, temp, comp, midIdx, hiIdx);
    // Step 3: merge the two sorted halves
    merge(source, temp, comp, loIdx, midIdx, hiIdx);
  }

  // EFFECT: sorts the provided list by merging the 2 sublists
  <T> void merge(ArrayList<T> source, ArrayList<T> temp, IComparator<T> comp, int loIdx,
      int midIdx, int hiIdx) {
    int sub1Index = loIdx;
    int sub2Index = midIdx;
    int i = loIdx;

    while (sub1Index < midIdx && sub2Index < hiIdx) {
      if (comp.compare(source.get(sub1Index), source.get(sub2Index)) <= 0) {
        temp.set(i, source.get(sub1Index));
        sub1Index++;
      } else {
        temp.set(i, source.get(sub2Index));
        sub2Index++;
      }
      i++;
    }

    while (sub1Index < midIdx) {
      temp.set(i, source.get(sub1Index));
      sub1Index++;
      i++;
    }
    while (sub2Index < midIdx) {
      temp.set(i, source.get(sub2Index));
      sub2Index++;
      i++;
    }

    for (int j = loIdx; j < hiIdx; j++) {
      source.set(j, temp.get(j));
    }


  }
}

interface IComparator<T> {
  int compare(T t1, T t2);
}