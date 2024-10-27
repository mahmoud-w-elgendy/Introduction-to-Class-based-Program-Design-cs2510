package lab10.problem2;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamplesStack {
  Stack<String> s0, s1, s2, s3;

  void setup() {
    s0 = new Stack<>();
    s1 = stackFromArrayList(new ArrayList<>(Arrays.asList("A")));
    s2 = stackFromArrayList(new ArrayList<>(Arrays.asList("A", "B", "C", "D")));
  }

  // Create a new stack from array list for testing
  // order is from right to left: the topmost item in the stack
  // is the first item of the arraylist
  <T> Stack<T> stackFromArrayList(ArrayList<T> arr) {
      Stack<T> s = new Stack<>();
      for (int i = arr.size() - 1; i >= 0; i--) {
        s.push(arr.get(i));
      }
      return s;
    }

  void testStack(Tester t) {
    setup();
    t.checkExpect(s0.isEmpty(), true);
    t.checkExpect(s1.isEmpty(), false);
    s0.push("Hello");
    t.checkExpect(s0.isEmpty(), false);
    t.checkExpect(s1.pop(), "A");
    t.checkExpect(s1.isEmpty(), true);
    t.checkExpect(s2.pop(), "A");
    s2.push("Helo");
    t.checkExpect(s2.pop(), "Helo");
    s2.pop();
    s2.pop();
    t.checkExpect(s2.isEmpty(), false);
    s2.pop();
    t.checkExpect(s2.isEmpty(), true);
    t.checkException(new RuntimeException("Attempted to remove from empty list"), s2, "pop");
  }

  void testReverse(Tester t) {
    setup();
    Utils u = new Utils();
    ArrayList<Integer> a1 = new ArrayList<>(List.of(1));
    ArrayList<Integer> a2 = new ArrayList<>(Arrays.asList(1, 2, 3));
    ArrayList<Integer> a2rev = new ArrayList<>(Arrays.asList(3, 2, 1));

    t.checkExpect(u.reverse(new ArrayList<>()), new ArrayList<>());
    t.checkExpect(u.reverse(a1), a1);
    t.checkExpect(u.reverse(a2), a2rev);
  }

  void testRun(Tester t) {
    StringCreator creator = new StringCreator();
    t.checkExpect(creator.getString(),"");
    creator.add('c');
    creator.add('d');
    t.checkExpect(creator.getString(),"cd");
    creator.add('e');
    t.checkExpect(creator.getString(),"cde");
    creator.remove();
    creator.remove();
    t.checkExpect(creator.getString(),"c");
    creator.undo(); //undoes the removal of 'd'
    t.checkExpect(creator.getString(),"cd");
    creator.undo(); //undoes the removal of 'e'
    creator.undo(); //undoes the addition of 'e'
    t.checkExpect(creator.getString(),"cd");
    creator.add('a');
    t.checkExpect(creator.getString(),"cda");
    creator.undo(); //undoes the addition of 'a'
    creator.undo(); //undoes the addition of 'd'
    creator.undo(); //undoes the addition of 'c'
    t.checkExpect(creator.getString(),"");
    creator.undo(); //no effect, there is nothing to undo
  }
}
