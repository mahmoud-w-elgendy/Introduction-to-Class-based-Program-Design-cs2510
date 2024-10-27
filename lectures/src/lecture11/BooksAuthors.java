package lecture11;

class Book {
  String title;
  Author author;

  public Book(String title, Author author) {
    this.title = title;
    this.author = author;
  }

  boolean sameBook(Book that) {
    return this.title.equals(that.title) &&
        this.author.sameAuthor(that.author);
  }
}

class Author {
  String firstName;
  String lastName;

  public Author(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  boolean sameAuthor(Author that) {
    return this.firstName.equals(that.firstName) &&
        this.lastName.equals(that.lastName);
  }
}