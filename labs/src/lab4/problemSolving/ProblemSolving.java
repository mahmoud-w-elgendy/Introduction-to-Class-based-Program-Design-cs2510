package lab4.problemSolving;

import tester.Tester;

// 1.
interface IMaybeInt {};

class NoInt implements IMaybeInt {
  // Represents the absence of an integer value
}

class SomeInt implements IMaybeInt {
  int value;

  public SomeInt(int value) {
    this.value = value;
  }
}

// 2.
interface ILoInt {
  int longestConsecutive();
  int longestConsecutiveHelper(int currentNum, int currentTimes, int prevNum,
      int prevTimes);
}

class MtLoInt implements ILoInt {

  public int longestConsecutive() {
    // A placeholder value, indicating that the list is empty
    return Integer.MIN_VALUE;
  }

  public int longestConsecutiveHelper(int currentNum, int currentTimes, int prevNum,
      int prevTimes) {
    if (currentTimes > prevTimes) {
      return currentNum;
    }
    return prevNum;
  }
}

class ConsLoInt implements ILoInt {
  int first;
  ILoInt rest;

  public ConsLoInt(int first, ILoInt rest) {
    this.first = first;
    this.rest = rest;
  }

  public int longestConsecutive() {
    return rest.longestConsecutiveHelper(first, 1, first, 0);
  }

  public int longestConsecutiveHelper(int currentNum, int currentTimes, int prevNum,
      int prevTimes) {
    if (currentNum == first) {
      return rest.longestConsecutiveHelper(currentNum, currentTimes + 1, prevNum, prevTimes);
    }

    // new number - keep the highest(by appearances) previous value
    if (prevTimes >= currentTimes) {
      return rest.longestConsecutiveHelper(first, 1, prevNum, prevTimes);
    } else {
      return rest.longestConsecutiveHelper(first, 1, currentNum, currentTimes);
    }
  }
}


class ExamplesProblemSolving {
  // Tests for the longest consecutive sublist
  boolean testLongestConsecutiveSublist(Tester t) {
    return t.checkExpect(new ConsLoInt(1, new ConsLoInt(1,
        new ConsLoInt(5, new ConsLoInt(5,
            new ConsLoInt(5, new ConsLoInt(4,
                new ConsLoInt(3, new ConsLoInt(4,
                    new ConsLoInt(4, new ConsLoInt(4, new MtLoInt()))))))))))
        .longestConsecutive(), 5) &&

        // Test for list with a tie in longest consecutive sublist, should return first
        t.checkExpect(new ConsLoInt(2, new ConsLoInt(2,
            new ConsLoInt(3, new ConsLoInt(3,
                new ConsLoInt(4, new ConsLoInt(4, new MtLoInt()))))))
            .longestConsecutive(), 2) &&

        // Test for list with single elements, should return the first one
        t.checkExpect(new ConsLoInt(1, new ConsLoInt(2,
            new ConsLoInt(3, new ConsLoInt(4, new MtLoInt()))))
            .longestConsecutive(), 1) &&

        // Test for a list with all identical elements, should return that element
        t.checkExpect(new ConsLoInt(7, new ConsLoInt(7,
            new ConsLoInt(7, new ConsLoInt(7, new MtLoInt()))))
            .longestConsecutive(), 7) &&

        // Test for an empty list, should return MIN_VALUE
        t.checkExpect(new MtLoInt().longestConsecutive(), Integer.MIN_VALUE);
  }
}

// 3. is big, so its in a separate file - Tasks.java