package lecture11;

import tester.Tester;

// only includes code not shown on the notes
class Examples {
  CartPt c1 = new CartPt(0, 0);
  CartPt c2 = new CartPt(0, 2);
  Book b1 = new Book("Book", new Author("Author", "Author"));
  Book b2 = new Book("Book", new Author("Author", "NotAuthor"));
  Book b3 = new Book("NotBook", new Author("Author", "Author"));


  boolean testSamePoint(Tester t) {
    return t.checkExpect(c1.samePoint(new CartPt(0, 0)), true) &&
        t.checkExpect(c1.samePoint(c2), false);
  }

  boolean testSameBook(Tester t) {
    return t.checkExpect(b1.sameBook(new Book("Book", new Author("Author", "Author"))), true) &&
        t.checkExpect(b1.sameBook(b2), false) &&
        t.checkExpect(b3.sameBook(b1), false);
  }
}
