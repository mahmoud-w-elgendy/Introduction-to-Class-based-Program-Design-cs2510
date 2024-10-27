package lecture23;

import java.util.ArrayList;
import java.util.function.Function;

class ArrayUtils {
  <T> ArrayList<T> buildList(int n, Function<Integer, T> func) {
    ArrayList<T> arr = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      arr.add(func.apply(i));
    }
    return arr;
  }

  void capitalizeTitles(ArrayList<Book> books) {
    for (Book b: books) {
      b.capitalizeTitle();
    }
  }
}

class Author {
  String name;
  int yob;

  public Author(String name, int yob) {
    this.name = name;
    this.yob = yob;
  }
}

class Book {
  String title;
  Author author;

  public Book(String title, Author author) {
    this.title = title;
    this.author = author;
  }

  // EFFECT: Capitalizes this book's title
  void capitalizeTitle() {
    this.title = this.title.toUpperCase();
  }
}