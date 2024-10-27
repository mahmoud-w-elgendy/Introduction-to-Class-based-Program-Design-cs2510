package lab9;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

public class Utils {
  <T> ArrayList<T> filter(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> newArr = new ArrayList<>();
    for (T t: arr) {
      if (pred.test(t)) {
        newArr.add(t);
      }
    }
    return newArr;
  }

  <T> void removeExcept(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> toRemove = filter(arr, new NegatePred<>(pred));
    for (T t: toRemove) {
      while (arr.contains(t)) {
        arr.remove(t);
      }
    }
  }

  // Produce an array list of all integers in the given range, inclusive
  ArrayList<Integer> makeIntList(int start, int end) {
    ArrayList<Integer> tileValues = new ArrayList<>();
    for (int i = start; i <= end; i++) {
      tileValues.add(i);
    }
    return tileValues;
  }

  // Remove and return a random item from the list
  <T> T takeRandom(ArrayList<T> arr, Random rand) {
    int i = rand.nextInt(arr.size());
    return arr.remove(i);
  }

  // Same as the built-in pred.negate()
  class NegatePred<T> implements Predicate<T> {
    Predicate<T> pred;
    NegatePred(Predicate<T> pred) {
      this.pred = pred;
    }

    public boolean test(T t) {
      return !pred.test(t);
    }
  }
}
