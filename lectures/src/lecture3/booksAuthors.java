package lecture3;

import tester.Tester;

// to represent a book in a bookstore
class Book {
  String title;
  Author author;
  int price;

  // the constructor
  Book(String title, Author author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }

  /* TEMPLATE:
   Fields:
   ... this.title ...        -- String
   ... this.author ...       -- Author
   ... this.price ...        -- int

   Methods:
   ... this.salePrice(int) ...   -- int
   ... this.sameAuthor(Book) ... -- boolean

   Methods for fields:
   ... this.author.mmm(??) ...    -- ??
   ... this.author.sameAuthor(Author) ...    -- boolean
*/

  // is this book written by the same author as the given book?
  /* TEMPLATE: everything in the template for Book, plus
     Fields of parameters:
     ... that.title ...        -- String
     ... that.author ...       -- Author
     ... that.price ...        -- int

     Methods on parameters:
     ... that.salePrice(int) ...   -- int
     ... that.sameAuthor(Book) ... -- boolean
     ... that.author.sameAuthor(Author) ...    -- boolean
  */
  boolean sameAuthor(Book that) {
    return this.author.sameAuthor(that.author);
  }
}

// to represent a author of a book in a bookstore
class Author {
  String name;
  int yob;

  // the constructor
  Author(String name, int yob) {
    this.name = name;
    this.yob = yob;
  }

  /* TEMPLATE
     Fields:
     ... this.name ...    -- String
     ... this.yob ...     -- int

     Methods:
     ... this.sameAuthor(Author) ... -- boolean
  */

  // is this author the same as the given author?
  Boolean sameAuthor(Author that) {
    return this.name.equals(that.name) &&
           this.yob == that.yob;
  }
}


// examples and tests for the classes that represent books and authors
class ExamplesBooksAuthors {
  ExamplesBooksAuthors() {}

  // examples of authors
  Author pat = new Author("Pat Conroy", 1948);
  Author dan = new Author("Dan Brown", 1962);

  // examples of books
  Book beaches = new Book("Beaches", this.pat, 20);
  Book prince = new Book("Prince of Tides", this.pat, 15);
  Book code = new Book("Da Vinci Code", this.dan, 20);

  // test the method sameAuthor in the class Book
  boolean testSameBookAuthor(Tester t) {
    return t.checkExpect(this.beaches.sameAuthor(this.prince), true)
        && t.checkExpect(this.beaches.sameAuthor(this.code), false);
  }

  // test the method sameAuthor in the class Author
  boolean testSameAuthor(Tester t) {
    return t.checkExpect(
        this.pat.sameAuthor(new Author("Pat Conroy", 1948)),
        true)
        && t.checkExpect(this.pat.sameAuthor(this.dan), false);
  }
}