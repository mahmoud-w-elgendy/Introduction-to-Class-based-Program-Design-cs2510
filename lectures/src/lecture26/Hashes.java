package lecture26;

import java.awt.desktop.AboutHandler;

class Book {
  Author author;
  String title;
  int year;
  public int hashCode() {
    return this.author.hashCode() * 10000 + this.year;
  }

  public boolean equals(Object other) {
    if (!(other instanceof Book)) { return false; }
    // this cast is safe, because we just checked instanceof
    Book that = (Book)other;
    return this.author.equals(that.author)
        && this.year == that.year
        && this.title.equals(that.title);
  }

  int faultyHashCode() {
    return this.title.hashCode() * 10000 + this.year;
  }

  boolean faultyEquals(Object other) {
    if (!(other instanceof Book)) { return false; }
    Book that = (Book) other;
    return this.title.equals(that.title);
  }
}
class Author {
  Book book;
  String name;
  int yob;
  public int hashCode() {
    return this.name.hashCode() * 10000 + this.yob;
  }

  public boolean equals(Object other) {
    if (!(other instanceof Author)) { return false; }
    // this cast is safe, because we just checked instanceof
    Author that = (Author)other;
    return this.name.equals(that.name)
        && this.yob == that.yob;
  }
}

interface IShape {
  boolean sameShape(IShape that);
  boolean sameCircle(Circle that);
  boolean sameSquare(Square that);
  boolean sameRect(Rect that);
}
abstract class AShape implements IShape {
  public boolean sameCircle(Circle that) { return false; }
  public boolean sameSquare(Square that) { return false; }
  public boolean sameRect(Rect that)     { return false; }
  public boolean equals(Object other) {
    if (!(other instanceof IShape)) { return false; }
    IShape that = (IShape) other;
    return this.sameShape(that);
  }
}

class Circle extends AShape {
  int radius;
  Circle(int radius) { this.radius = radius; }
  public boolean sameShape(IShape that) { return that.sameCircle(this); }
  public boolean sameCircle(Circle that) { return that.radius == this.radius; }
  public int hashCode() { return radius; }
}

class Square extends Rect {
  Square(int x, int y, int s) {
    super(x, y, s, s);
  }

  public boolean sameShape(IShape that) {
    return that.sameSquare(this);
  }

  public boolean sameRect(Rect that) {
    return false;
  }

  public boolean sameSquare(Square that) {
    return this.x == that.x && this.y == that.y && this.w == that.w;
  }

  public int hashCode() {
    return 10000 * x + 1000 * y + this.w;
  }
}

class Rect extends AShape {
  int x, y;
  int w, h;

  Rect(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  public boolean sameShape(IShape that) {
    return that.sameRect(this);
  }

  public boolean sameRect(Rect that) {
    return this.x == that.x && this.y == that.y && this.w == that.w && this.h == that.h;
  }

  public int hashCode() {
    return 10000 * x + 1000 * y + 100 * w + h;
  }
}