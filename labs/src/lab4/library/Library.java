package lab4.library;

import tester.Tester;

interface IBook {
  int daysOverdue(int today);
  boolean isOverdue(int day);
  int computeFine(int day);
}

abstract class ABook implements IBook {
  String title;
  int dayTaken;

  public ABook(String title, int dayTaken) {
    this.title = title;
    this.dayTaken = dayTaken;
  }

  public boolean isOverdue(int day) {
    return this.daysOverdue(day) > 0;
  }
}

class Book extends ABook {
  String author;

  public Book(String title, int dayTaken, String author) {
    super(title, dayTaken);
    this.author = author;
  }

  public int daysOverdue(int today) {
    return today - (dayTaken + 14);
  }

  public int computeFine(int day) {
    int daysOverdueResult = daysOverdue(day);
    if (daysOverdueResult > 0) {
      return 10 * daysOverdueResult;
    }
    return 0;
  }
}

class RefBook extends ABook {
  public RefBook(String title, int dayTaken) {
    super(title, dayTaken);
  }

  public int daysOverdue(int today) {
    return today - (dayTaken + 2);
  }

  public int computeFine(int day) {
    int daysOverdueResult = daysOverdue(day);
    if (daysOverdueResult > 0) {
      return 10 * daysOverdueResult;
    }
    return 0;
  }
}

class AudioBook extends ABook {
  String author;

  public AudioBook(String title, int dayTaken, String author) {
    super(title, dayTaken);
    this.author = author;
  }

  public int daysOverdue(int today) {
    return today - (dayTaken + 14);
  }

  public int computeFine(int day) {
    int daysOverdueResult = daysOverdue(day);
    if (daysOverdueResult > 0) {
      return 20 * daysOverdueResult;
    }
    return 0;
  }
}

class ExamplesBooks {
  IBook book1 = new Book("War and Peace", 7778, "Leo Tolstoy");
  IBook refBook1 = new RefBook("Encyclopedia", 7778);
  IBook audioBook1 = new AudioBook("The Hobbit", 7778, "J.R.R. Tolkien");

  boolean testDaysOverdue(Tester t) {
    return t.checkExpect(book1.daysOverdue(7793), 1) &&
        t.checkExpect(refBook1.daysOverdue(7793), 13) &&
        t.checkExpect(audioBook1.daysOverdue(7793), 1);
  }

  boolean testIsOverdue(Tester t) {
    return t.checkExpect(book1.isOverdue(7793), true) &&
        t.checkExpect(refBook1.isOverdue(7793), true) &&
        t.checkExpect(audioBook1.isOverdue(7793), true) &&
        t.checkExpect(book1.isOverdue(7792), false);
  }

  boolean testComputeFine(Tester t) {
    return t.checkExpect(book1.computeFine(7793), 10) &&
        t.checkExpect(refBook1.computeFine(7793), 130) &&
        t.checkExpect(audioBook1.computeFine(7793), 20);
  }
}
