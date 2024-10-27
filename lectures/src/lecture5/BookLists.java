package lecture5;

import tester.Tester;

class Book {
  String title;
  String author;
  double price;
  int year;
  Book(String title, String author, int year, double price) {
    this.title = title;
    this.author = author;
    this.price = price;
    this.year = year;
  }
  double salePrice(int discount) {
    return this.price - (discount * this.price) / 100;
  }

  // was this book published before the given year?
  boolean publishedBefore(int year) {
    return this.year < year;
  }

  public boolean isCheaperThan(Book that) {
    return this.price < that.price;
  }

  public boolean isTitleBefore(Book first) {
    return this.title.compareTo(first.title) < 0;
  }
}

/*
 * GOAL:
 * Represent a bunch of books, and be able to compute
 * - their total price
 * - how many books we have
 * - all the books published before the given year
 * - a sorted list of books
 */

/*
 * A list of books is one of
 * empty
 * (cons book list-of-books)
 */

// DYNAMIC DISPATCH: deciding which method definition to invoke (in which class)
// based on the information available at runtime of the object that's invoking
// the method

interface ILoBook {
  // to compute the total price of all books in this list of books
  double salePrice(int discount);
  // to count how many books are in this list of books
  int count();
  // to return a list of all the books in this list of books
  // published before the given year
  ILoBook allBefore(int year);
  // to construct a list of books that contains the same books as
  // this list of books, but sorted increasing by price
  ILoBook sortByPrice();

  ILoBook insertCheaperFirst(Book first);

  ILoBook sortByTitle();

  ILoBook insertTitleBeforeFirst(Book first);
}

class MtLoBook implements ILoBook {
  MtLoBook() {
    // nothing to do!
  }
  public double salePrice(int discount) {
    return 0;
  }
  public int count() {
    return 0;
  }
  public ILoBook allBefore(int year) {
    return this;
  }
  public ILoBook sortByPrice() {
    return this;
  }
  ILoBook insert(Book b) {
    return new ConsLoBook(b, this);
  }
  public ILoBook insertCheaperFirst(Book b) {
    return this.insert(b);
  }

  public ILoBook sortByTitle() {
    return this;
  }

  public ILoBook insertTitleBeforeFirst(Book b) {
    return this.insert(b);
  }
}

class ConsLoBook implements ILoBook {
  Book first;
  ILoBook rest;
  ConsLoBook(Book first, ILoBook rest) {
    this.first = first;
    this.rest = rest;
  }
  public double salePrice(int discount) {
    return this.first.salePrice(discount) + this.rest.salePrice(discount);
  }
  public int count() {
    return 1 + this.rest.count();
  }
  public ILoBook allBefore(int year) {
    if (this.first.publishedBefore(year)) {
      return new ConsLoBook(
          this.first,
          this.rest.allBefore(year));
    } else {
      return this.rest.allBefore(year);
    }
  }
  public ILoBook sortByPrice() {
    return this.rest.sortByPrice().insertCheaperFirst(this.first);

  }

  public ILoBook sortByTitle() {
    return this.rest.sortByTitle().insertTitleBeforeFirst(this.first);
  }

  public ILoBook insertCheaperFirst(Book first) {
    if (first.isCheaperThan(this.first)) {
      return new ConsLoBook(first, this);
    } else {
      return new ConsLoBook(this.first, this.rest.insertCheaperFirst(first));
    }
  }

  public ILoBook insertTitleBeforeFirst(Book first) {
    if (first.isTitleBefore(this.first)) {
      return new ConsLoBook(first, this);
    } else {
      return new ConsLoBook(this.first, this.rest.insertTitleBeforeFirst(first));
    }
  }
}

class ExamplesBookLists {
  //Books
  Book htdp = new Book("HtDP", "MF", 2001, 60);
  Book lpp = new Book("LPP", "STX", 1942, 25);
  Book ll = new Book("LL", "FF", 1986, 10);

  // lists of Books
  ILoBook mtList = new MtLoBook();
  ILoBook listA = new ConsLoBook(this.lpp, this.mtList);
  ILoBook listB = new ConsLoBook(this.htdp, this.mtList);
  ILoBook listC = new ConsLoBook(this.lpp,
      new ConsLoBook(this.ll, this.listB));
  ILoBook listD = new ConsLoBook(this.ll,
      new ConsLoBook(this.lpp,
          new ConsLoBook(this.htdp, this.mtList)));
  ILoBook listDUnsorted =
      new ConsLoBook(this.lpp,
          new ConsLoBook(this.htdp,
              new ConsLoBook(this.ll, this.mtList)));

  // tests for the method count
  boolean testCount(Tester t) {
    return
        t.checkExpect(this.mtList.count(), 0) &&
            t.checkExpect(this.listA.count(), 1) &&
            t.checkExpect(this.listD.count(), 3);
  }

  // tests for the method salePrice
  boolean testSalePrice(Tester t) {
    return
        // no discount -- full price
        t.checkInexact(this.mtList.salePrice(0), 0.0, 0.001) &&
            t.checkInexact(this.listA.salePrice(0), 25.0, 0.001) &&
            t.checkInexact(this.listC.salePrice(0), 95.0, 0.001) &&
            t.checkInexact(this.listD.salePrice(0), 95.0, 0.001) &&
            // 50% off sale -- half price
            t.checkInexact(this.mtList.salePrice(50), 0.0, 0.001) &&
            t.checkInexact(this.listA.salePrice(50), 12.5, 0.001) &&
            t.checkInexact(this.listC.salePrice(50), 47.5, 0.001) &&
            t.checkInexact(this.listD.salePrice(50), 47.5, 0.001);
  }

  boolean testAllBefore(Tester t) {
    return
      t.checkExpect(this.mtList.allBefore(2022), this.mtList) &&
      t.checkExpect(this.listA.allBefore(1950), this.listA) &&
      t.checkExpect(this.listD.allBefore(1990), new ConsLoBook(this.ll,
                                                            new ConsLoBook(this.lpp, mtList))) &&
      t.checkExpect(this.listD.allBefore(1980), new ConsLoBook(lpp, mtList));

  }

  // test the method sort for the lists of books
  boolean testSortByPrice(Tester t) {
    return
        t.checkExpect(this.listC.sortByPrice(), this.listD) &&
            t.checkExpect(this.listDUnsorted.sortByPrice(), this.listD);
  }
}
