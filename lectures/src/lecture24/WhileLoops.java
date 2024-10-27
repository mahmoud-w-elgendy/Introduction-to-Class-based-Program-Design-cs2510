package lecture24;

import java.util.ArrayList;

class ArrayUtils {
  int findIdxOfMinInRest(ArrayList<String> arr, int restIndex) {
    int minIndex = restIndex;
    int arrSize = arr.size();
    int i = restIndex;
    while (i < arrSize) {
      if (arr.get(i).compareTo(arr.get(minIndex)) < 0) {
        minIndex = i;
      }
      i++;
    }
    return minIndex;
  }

  boolean getsToOne(int n) {
    ArrayList<Integer> seen = new ArrayList<>();
    while (n > 1) {
      if (seen.contains(n)) {
        return false;
      }
      seen.add(n);
      if (n % 2 == 0) {
        n = n / 2;
      }
      else {
        n = 3 * n + 1;
      }
    }
    return true;
  }
}