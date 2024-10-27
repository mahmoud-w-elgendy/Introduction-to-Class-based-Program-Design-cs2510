package lecture32;

import java.util.ArrayList;

class HeapNode<T> {
  T data;
  int weight;
  int index; // for the optimization

  public HeapNode(T data, int weight, int index) {
    this.data = data;
    this.weight = weight;
    this.index = index;
  }
}

class Heap<T> {
  ArrayList<HeapNode<T>> contents;
  IComparator<Integer> comp; // Compare by priority

  public Heap(IComparator<Integer> comp) {
    this.comp = comp;
    this.contents = new ArrayList<>();
  }

  public HeapNode<T> add(T data, int weight) {
    HeapNode<T> node = new HeapNode<>(data, weight, contents.size());
    contents.add(node);
    upheap(contents.size() - 1);
    return node;
  }

  T remove() {
    int lastIdx = contents.size() - 1;
    swap(0, lastIdx);
    downheap(0, lastIdx - 1);
    return contents.remove(lastIdx).data;
  }

  private void build() {
    for (int i = 1; i < contents.size(); i++) {
      upheap(i);
    }
  }

  void upheap(int i) {
    int parentIdx = (int) Math.floor((i - 1) / 2.0);
    if (parentIdx < 0) { return; }
    if (compare(parentIdx, i) < 0) {
      swap(parentIdx, i);
      upheap(parentIdx);
    }
  }

  // A convenience wrapper method
  int compare(int i, int j) {
    return comp.compare(contents.get(i).weight, contents.get(j).weight);
  }

  void swap(int index1, int index2) {
    HeapNode<T> item1 = contents.get(index1);
    HeapNode<T> item2 = contents.get(index2);
    // Swap the elements in the heap
    contents.set(index1, item2);
    contents.set(index2, item1);
    // Update their indices
    item1.index = index2;
    item2.index = index1;
  }

  void downheap(int i, int lastIdx) {
    int leftIdx = 2*i + 1;
    int rightIdx = 2*i + 2;
    Integer biggerChild = findBiggerChild(leftIdx, rightIdx, lastIdx);

    if (biggerChild != null && (compare(i, biggerChild)) < 0) {
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
      if (compare(leftIdx, rightIdx) > 0) {
        return leftIdx;
      }
      return rightIdx;
    }
  }

  public boolean isEmpty() { return contents.isEmpty(); }
}

class MaxHeap<T> extends Heap<T> {
  public MaxHeap(ArrayList<Integer> contents) {
    super(new MaxComparator());
  }
}

class PriorityQueue<T> extends Heap<T> {
  public PriorityQueue() {
    super(new MinComparator());
  }
}

class MinComparator implements IComparator<Integer> {
  public int compare(Integer t1, Integer t2) {
    return t2 - t1;
  }
}

class MaxComparator implements IComparator<Integer> {
  public int compare(Integer t1, Integer t2) {
    return t1 - t2;
  }
}

interface IComparator<T> {
  int compare(T t1, T t2);
}