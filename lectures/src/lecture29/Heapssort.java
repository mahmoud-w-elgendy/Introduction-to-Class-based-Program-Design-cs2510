package lecture29;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Utils {
  void heapSort(ArrayList<Integer> arr) {
    buildHeap(arr);
    int arrSize = arr.size();
    for (int i = 0; i < arrSize; i++) {
      removeFromHeap(arr, arrSize - i - 1);
    }
  }

  // EFFECT: turns the given array into a valid HeapQueue
  void buildHeap(ArrayList<Integer> arr) {
    for (int i = 1; i < arr.size(); i++) {
      upheap(arr, i);
    }
  }

  void upheap(ArrayList<Integer> arr, int i) {
    int parentIdx = (int) Math.floor((i - 1) / 2.0);
    if (parentIdx < 0) { return; }
    if (arr.get(parentIdx) < arr.get(i)) {
      swap(arr, parentIdx, i);
      upheap(arr, parentIdx);
    }
  }

  // EFFECT: removes the root element from the heap
  // Every item in the array after lastIdx is not part of the heap
  void removeFromHeap(ArrayList<Integer> arr, int lastIdx) {
    swap(arr, 0, lastIdx);
    downheap(arr, 0, lastIdx - 1);
  }

  void downheap(ArrayList<Integer> arr, int i, int lastIdx) {
    int leftIdx = 2*i + 1;
    int rightIdx = 2*i + 2;
    Integer biggerChild = findBiggerChild(arr, leftIdx, rightIdx, lastIdx);

    if (biggerChild != null && (arr.get(i) < arr.get(biggerChild))) {
      swap(arr, i, biggerChild);
      downheap(arr, biggerChild, lastIdx);
    }
  }

  // Return the index of the bigger children
  // if there are no children, returns null
  // if there is no right child, returns the left one.
  Integer findBiggerChild(ArrayList<Integer> arr, int leftIdx, int rightIdx, int lastIdx) {
    if (rightIdx > lastIdx && leftIdx > lastIdx) {
      return null;
    } else if (rightIdx > lastIdx) {
      return leftIdx;
    } else {
      if (arr.get(leftIdx) > arr.get(rightIdx)) {
        return leftIdx;
      }
      return rightIdx;
    }
  }
  // EFFECT: Exchanges the values at the given two indices in the given array
  <T> void swap(ArrayList<T> arr, int index1, int index2) {
    T item1 = arr.get(index1);
    arr.set(index1, arr.get(index2));
    arr.set(index2, item1);
  }

  // uses the Heap class instead
  <T> void heapSort(ArrayList<T> arr, IComparator<T> comp) {
    Heap<T> heap = new Heap<T>(arr, comp);
    int arrSize = arr.size();
    for (int i = 0; i < arrSize; i++) {
      heap.remove(arrSize - i - 1);
    }
  }
}


class Heap<T> {
  ArrayList<T> contents;
  IComparator<T> comp; // Compare by priority

  public Heap(ArrayList<T> contents, IComparator<T> comp) {
    this.comp = comp;
    this.contents = contents;
    build();
  }

  private void build() {
    for (int i = 1; i < contents.size(); i++) {
      upheap(i);
    }
  }

  void upheap(int i) {
    int parentIdx = (int) Math.floor((i - 1) / 2.0);
    if (parentIdx < 0) { return; }
    if (comp.compare(contents.get(parentIdx), contents.get(i)) < 0) {
      swap(parentIdx, i);
      upheap(parentIdx);
    }
  }

  void swap(int index1, int index2) {
    T item1 = contents.get(index1);
    contents.set(index1, contents.get(index2));
    contents.set(index2, item1);
  }

  // EFFECT: removes the root element from the heap
  // Every item in the array after lastIdx is not part of the heap
  void remove(int lastIdx) {
    swap(0, lastIdx);
    downheap(0, lastIdx - 1);
  }

  void downheap(int i, int lastIdx) {
    int leftIdx = 2*i + 1;
    int rightIdx = 2*i + 2;
    Integer biggerChild = findBiggerChild(leftIdx, rightIdx, lastIdx);

    if (biggerChild != null && (comp.compare(contents.get(i), contents.get(biggerChild))) < 0) {
      swap(i, biggerChild);
      downheap(biggerChild, lastIdx);
    }
  }

  // Return the index of the bigger children
  // if there are no children, returns null
  // if there is no right child, returns the left one.
  Integer findBiggerChild(int leftIdx, int rightIdx, int lastIdx) {
    if (rightIdx > lastIdx && leftIdx > lastIdx) {
      return null;
    } else if (rightIdx > lastIdx) {
      return leftIdx;
    } else {
      if (comp.compare(contents.get(leftIdx), contents.get(rightIdx)) > 0) {
        return leftIdx;
      }
      return rightIdx;
    }
  }
}

interface IComparator<T> {
  int compare(T t1, T t2);
}

class CompareIntReverse implements IComparator<Integer> {
  public int compare(Integer t1, Integer t2) {
    return t2 - t1;
  }
}

class ExamplesHeapSort {
  void testSort(Tester t) {
    Utils u = new Utils();
    ArrayList<Integer> a0 = new ArrayList<>();
    ArrayList<Integer> a1 = new ArrayList<>(List.of(1));
    ArrayList<Integer> a2 = new ArrayList<>(Arrays.asList(3, 1, 2, 8, 20, 0));
    u.heapSort(a0);
    t.checkExpect(a0, new ArrayList<>());
    u.heapSort(a1);
    t.checkExpect(a1, new ArrayList<>(List.of(1)));
    u.heapSort(a2);
    t.checkExpect(a2, new ArrayList<>(Arrays.asList(0, 1, 2, 3, 8, 20)));
  }

  void testSortT(Tester t) {
    Utils u = new Utils();
    IComparator<Integer> compInt = new CompareIntReverse();
    ArrayList<Integer> a0 = new ArrayList<>();
    ArrayList<Integer> a1 = new ArrayList<>(List.of(1));
    ArrayList<Integer> a2 = new ArrayList<>(Arrays.asList(3, 1, 2, 8, 20, 0));
    u.heapSort(a0, compInt);
    t.checkExpect(a0, new ArrayList<>());
    u.heapSort(a1, compInt);
    t.checkExpect(a1, new ArrayList<>(List.of(1)));
    u.heapSort(a2, compInt);
    t.checkExpect(a2, new ArrayList<>(Arrays.asList(20, 8, 3, 2, 1, 0)));
  }
}

/*
First buildHeap version for [1, 2, 3, 4, 5, 6, 7, 8, 9]
1

1, 2
2, 1

2, 1, 3
3, 1, 2

3, 1, 2, 4
3, 4, 2, 1
4, 3, 2, 1

4, 3, 2, 1, 5
4, 5, 2, 1, 3
5, 4, 2, 1, 3

5, 4, 2, 1, 3, 6
5, 4, 6, 1, 3, 2
6, 4, 5, 1, 3, 2

6, 4, 5, 1, 3, 2, 7
6, 4, 7, 1, 3, 2, 5
7, 4, 6, 1, 3, 2, 5

7, 4, 6, 1, 3, 2, 5, 8
7, 4, 6, 8, 3, 2, 5, 1
7, 8, 6, 4, 3, 2, 5, 1
8, 7, 6, 4, 3, 2, 5, 1

8, 7, 6, 4, 3, 2, 5, 1, 9
8, 7, 6, 9, 3, 2, 5, 1, 4
8, 9, 6, 7, 3, 2, 5, 1, 4
9, 8, 6, 7, 3, 2, 5, 1, 4

Heapsort on [9, 8, 6, 7, 3, 2, 5, 1, 4]
9 8 6 7 3 2 5 1 4

4 8 6 7 3 2 5 1 || 9
8 4 6 7 3 2 5 1 || 9
8 7 6 4 3 2 5 1 || 9

1 7 6 4 3 2 5 || 8 9
7 1 6 4 3 2 5 || 8 9
7 4 6 1 3 2 5 || 8 9
7 4 6 1 3 2 5 || 8 9

5 4 6 1 3 2 || 7 8 9
6 4 5 1 3 2 || 7 8 9
6 4 5 1 3 2 || 7 8 9

2 4 5 1 3 || 6 7 8 9
5 4 2 1 3 || 6 7 8 9

3 4 2 1 || 5 6 7 8 9
4 3 2 1 || 5 6 7 8 9

4 3 2 1 || 5 6 7 8 9
1 3 2 || 4 5 6 7 8 9
3 1 2 || 4 5 6 7 8 9
1 2 || 3 4 5 6 7 8 9
2 1 || 3 4 5 6 7 8 9
1 || 2 3 4 5 6 7 8 9
1 2 3 4 5 6 7 8 9

Second buildHeap version for [1, 2, 3, 4, 5, 6, 7, 8, 9]

1, 2, 3, 4, 5, 6, 7, 8, 9
1, 2, 3, 4, 5, 6, 7, 8, 9
1, 2, 3, 9, 5, 6, 7, 8, 4
1, 2, 7, 9, 5, 6, 3, 8, 4
1, 2, 7, 9, 5, 6, 3, 8, 4
1, 9, 7, 2, 5, 6, 3, 8, 4
1, 9, 7, 8, 5, 6, 3, 2, 4
9, 1, 7, 8, 5, 6, 3, 2, 4
9, 8, 7, 1, 5, 6, 3, 2, 4
9, 8, 7, 4, 5, 6, 3, 2, 1


Heapsort on [9, 8, 7, 4, 5, 6, 3, 2, 1]

9 8 7 4 5 6 3 2 1

1 8 7 4 5 6 3 2 || 9
8 1 7 4 5 6 3 2 || 9
8 5 7 4 1 6 3 2 || 9
8 5 7 4 1 6 3 2 || 9

2 5 7 4 1 6 3 || 8 9
7 5 2 4 1 6 3 || 8 9
7 5 6 4 1 2 3 || 8 9

3 5 6 4 1 2 || 7 8 9
6 5 3 4 1 2 || 7 8 9

2 5 3 4 1|| 6 7 8 9
5 2 3 4 1|| 6 7 8 9
5 4 3 2 1|| 6 7 8 9

1 4 3 2 || 5 6 7 8 9
4 1 3 2 || 5 6 7 8 9
4 2 3 1 || 5 6 7 8 9

1 2 3 || 4 5 6 7 8 9
3 2 1 || 4 5 6 7 8 9

1 2 || 3 4 5 6 7 8 9
2 1 || 3 4 5 6 7 8 9

1 || 2 3 4 5 6 7 8 9

1 2 3 4 5 6 7 8 9


Heapsort on [9, 8, 7, 6, 5, 4, 3, 2, 1]

buildHeap:
9 8 7 6 5 4 3 2 1

sort:
9 8 7 6 5 4 3 2 1

1 8 7 6 5 4 3 2 || 9
8 1 7 6 5 4 3 2 || 9
8 6 7 1 5 4 3 2 || 9
8 6 7 2 5 4 3 1 || 9

1 6 7 2 5 4 3 || 8 9
7 6 1 2 5 4 3 || 8 9
7 6 4 2 5 1 3 || 8 9

3 6 4 2 5 1 || 7 8 9
6 3 4 2 5 1 || 7 8 9
6 5 4 2 3 1 || 7 8 9

1 5 4 2 3 || 6 7 8 9
5 1 4 2 3 || 6 7 8 9
5 3 4 2 1 || 6 7 8 9

1 3 4 2 || 5 6 7 8 9
4 3 1 2 || 5 6 7 8 9

2 3 1 || 4 5 6 7 8 9
3 2 1 || 4 5 6 7 8 9
1 2 || 3 4 5 6 7 8 9
2 1 || 3 4 5 6 7 8 9

1 || 2 3 4 5 6 7 8 9

1 2 3 4 5 6 7 8 9
*/
