package assignment10;

import java.util.ArrayList;
import java.util.Random;

public class ArrayUtils {
  // Remove and return a random item from the list
  static <T> T takeRandom(ArrayList<T> arr, Random rand) {

    int i = rand.nextInt(arr.size());
    return arr.remove(i);
  }

  // Return a new shuffled array without modifying the original
  static <T> ArrayList<T> shuffle(ArrayList<T> arr, Random rand) {
    ArrayList<T> newArr = new ArrayList<>();
    ArrayList<T> arrDup = new ArrayList<>(arr);
    while (!arrDup.isEmpty()) {
      newArr.add(takeRandom(arrDup, rand));
    }
    return newArr;
  }
}
