package lecture10;

import tester.Tester;

class Date {
  int day;
  int month;
  int year;

  Date(int year, int month, int day) {
    this.year = new Utils().checkRange(year, 1500, 2100,
        "Invalid year: " + Integer.toString(year));
    this.month = new Utils().checkRange(month, 1, 12,
        "Invalid month " + Integer.toString(month));
    this.day = new Utils().checkRange(day, 1, 31,
        "Invalid day: " + Integer.toString(day));
  }

  Date(int month, int day) {
    this(2022, month, day);
  }
}


class Utils {
  int checkRange(int val, int min, int max, String msg) {
    if (val >= min && val <= max) {
      return val;
    }
    else {
      throw new IllegalArgumentException(msg);
    }
  }
}


class ExamplesDate {
    boolean testCheckRange(Tester t) {
      Utils u = new Utils();
      return t.checkException(new IllegalArgumentException("Invalid value"), u, "checkRange", 5, 6, 7,
          "Invalid value") &&
        t.checkException(new IllegalArgumentException("Invalid value"), u, "checkRange", 8, 6, 7,
          "Invalid value") &&
          t.checkNoException(u, "checkRange", 5,
          4, 6, "Invalid value");
    }
}