package lecture15;

import tester.Tester;

class Runner {
  String name;
  int age;
  int bib;
  boolean isMale;
  int pos;
  int time;

  Runner(String name, int age, int bib, boolean isMale, int pos, int time) {
    this.name = name;
    this.age = age;
    this.bib = bib;
    this.isMale = isMale;
    this.pos = pos;
    this.time = time;
  }
}

class Author {}
class Book{
  String title;
  int price;

  // the constructor
  Book(String title, int price){
    this.title = title;
    this.price = price;
  }
}
interface IRunnerPredicate {
  boolean apply(Runner r);
}
interface IRunnerComparator {
  int compare(Runner r1, Runner r2);
}
interface IAuthorPredicate {
  boolean apply(Author a);
}
interface IAuthorComparator {
  int compare(Author a1, Author a2);
}

interface IBookPredicate {
  boolean apply(Book b);
}

class BookByAuthor implements IPred<Book> {
  public boolean apply(Book b) { return true;}
}

class RunnerInFirst50 implements IPred<Runner> {
  public boolean apply(Runner r) {
    return r.pos <= 50;
  }
}


class Examples {
  Runner johnny = new Runner("Kelly", 97, 999, true, 30, 360);
  Runner frank  = new Runner("Shorter", 32, 888, true, 245, 130);
  Runner bill = new Runner("Rogers", 36, 777, true, 119, 129);
  Runner joan = new Runner("Benoit", 29, 444, false, 18, 155);

  IPred<Runner> inFirst50 = new RunnerInFirst50();

  Book b1 = new Book("ss",12);
  Book b2 = new Book("ss",46);
  Book b3 = new Book("ss",21);
  IList<Book> lob0 = new MtList<Book>();
  IList<Book> lob1 = new ConsList<Book>(b1, lob0);
  IList<Book> lob2 = new ConsList<Book>(b2, lob1);
  IList<Book> lob3 = new ConsList<Book>(b3, lob2);

  boolean testFirst50(Tester t) {
    return t.checkExpect(inFirst50.apply(johnny), true) &&
        t.checkExpect(inFirst50.apply(frank), false);
  }

  boolean testTotalPrice(Tester t) {
    Utils u = new Utils();
    return t.checkExpect(u.totalPrice(lob0), 0) &&
        t.checkExpect(u.totalPrice(lob1), 12) &&
        t.checkExpect(u.totalPrice(lob2), 58) &&
        t.checkExpect(u.totalPrice(lob3), 79);
  }
}