package lab10.problem2;

import java.util.ArrayList;
import java.util.Arrays;

class Utils {
  <T> ArrayList<T> reverse(ArrayList<T> source) {
    Stack<T> stack = new Stack<>();
    for (T t: source) {
      stack.push(t);
    }

    ArrayList<T> reversed = new ArrayList<>();
    while (!stack.isEmpty()) {
      reversed.add(stack.pop());
    }

    return reversed;
  }


}
