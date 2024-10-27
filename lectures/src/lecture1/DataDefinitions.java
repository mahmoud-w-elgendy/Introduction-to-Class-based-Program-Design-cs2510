package lecture1;
/*

  +---------------------+
  | Book                |
  +---------------------+
  | String title        |
  | Author author       |--------------+
  | int price           |              |
  | Publisher publisher |              |
  +------------------+                 |
      |                                |
      |                                |
      v                                v
  +---------------------------+  +-------------+
  | Publisher                 |  | Author      |
  +---------------------------+  +-------------+
  | String name               |  | String name |
  | String countryOfOperation |  | int yob     |
  | int yearFounded           |  +-------------+
  +------------------------+

*/

// to represent a book in a bookstore
class Book{
  String title;
  Author author;
  Publisher publisher;
  int price;

  // the constructor
  Book(String title, Author author, Publisher publisher, int price){
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.price = price;
  }
}

// to represent an author of a book in a bookstore
class Author{
  String name;
  int yob;

  // the constructor
  Author(String name, int yob){
    this.name = name;
    this.yob = yob;
  }
}

// to represent a book's publisher
class Publisher{
  String name;
  String countryOfOperation;
  int yearFounded;

  Publisher(String name, String countryOfOperation, int yearFounded) {
    this.name = name;
    this.countryOfOperation = countryOfOperation;
    this.yearFounded = yearFounded;
  }
}

// examples and tests for the class hierarchy that represents
// books and authors
class ExamplesBooksAuthors{
  ExamplesBooksAuthors(){}

  Author pat = new Author("Pat Conroy", 1948);
  Publisher booker = new Publisher("Booker", "Italy", 2025);
  Book beaches = new Book("Beaches", this.pat, this.booker, 20);
}

