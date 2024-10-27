package lecture21;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

class ArrayUtils {
  // EFFECT: Exchanges the values at the given two indices in the given array
  <T> void swap(ArrayList<T> arr, int index1, int index2) {
    T item1 = arr.get(index1);
    T item2 = arr.get(index2);
    arr.set(index1, item2);
    arr.set(index2, item1);
  }

  <T, U> ArrayList<U> map(ArrayList<T> arr, Function<T, U> func) {
    return this.mapHelper(arr, 0, new ArrayList<U>(), func);
  }

  <T, U> ArrayList<U> mapHelper(ArrayList<T> originalArr, int i, ArrayList<U> newArr,
      Function<T, U> func) {
    if (i >= originalArr.size()) {
      return newArr;
    }
    else {
      newArr.add(func.apply(originalArr.get(i)));
      return mapHelper(originalArr, i + 1, newArr, func);
    }
  }

  <T, U> U foldlRecur(ArrayList<T> arr, BiFunction<T, U, U> func, U acc) {
    return this.foldlRecurHelper(arr, func, acc, 0);
  }

  <T, U> U foldlRecurHelper(ArrayList<T> arr, BiFunction<T, U, U> func, U acc, int i) {
    if (i >= arr.size()) {
      return acc;
    }
    else {
      U newAcc = func.apply(arr.get(i), acc);
      return this.foldlRecurHelper(arr, func, newAcc, i + 1);
    }
  }

  // Taking advantage of the fact that unlike linked lists, you can simply access an ArrayList from
  // right to left
  <T, U> U foldrRecur(ArrayList<T> arr, BiFunction<T, U, U> func, U acc) {
    return this.foldrRecurHelper(arr, func, acc, arr.size() - 1);
  }

  <T, U> U foldrRecurHelper(ArrayList<T> arr, BiFunction<T, U, U> func, U acc, int i) {
    if (i < 0) {
      return acc;
    }
    else {
      return this.foldrRecurHelper(arr, func, func.apply(arr.get(i), acc), i - 1);
    }
  }

  // The more "traditional" approach for lisp-style linked lists
  <T, U> U foldrRecurAlternate(ArrayList<T> arr, BiFunction<T, U, U> func, U acc) {
    return this.foldrRecurHelperAlternate(arr, func, acc, 0);
  }

  <T, U> U foldrRecurHelperAlternate(ArrayList<T> arr, BiFunction<T, U, U> func, U acc, int i) {
    if (i >= arr.size()) {
      return acc;
    }
    else {
      return func.apply(arr.get(i), this.foldrRecurHelper(arr, func, acc, i + 1));
    }
  }

  <T, U> U foldlForEach(ArrayList<T> arr, BiFunction<T, U, U> func, U base) {
    U acc = base;
    for (T t: arr) {
      acc = func.apply(t, acc);
    }
    return acc;
  }

  // This is only so cumbersome because we didn't learn about regular for loop yet...
  <T, U> U foldrForEach(ArrayList<T> arr, BiFunction<T, U, U> func, U base) {
    U acc = base;
    int lastIndex = arr.size() - 1;
    ArrayList<Integer> indices = this.getIndices(arr);
    for (Integer i: indices) {
      acc = func.apply(arr.get(lastIndex - i), acc);
    }
    return acc;
  }

  private <T> ArrayList<Integer> getIndices(ArrayList<T> arr) {
    ArrayList<Integer> indices = new ArrayList<>();
    int i = 0;
    for (T t: arr) {
      indices.add(i);
      i++;
    }
    return indices;
  }
}