package lecture4;

import tester.Tester;

// to represent a geometric shape
interface IShape {
  // to compute the area of this shape
  double area();

  // to compute the distance from this shape to the origin
  double distanceToOrigin();

  // to increase the size of this shape by the given increment
  IShape grow(int inc);

  // is the area of this shape bigger than
  // the area of the given shape?
  boolean isBiggerThan(IShape that);

  // is the given point inside this shape?
  boolean contains(IPoint point);
}

interface IPoint {
  double distanceToOrigin();
  boolean isInRadius(IPoint that, int radius);
  boolean isInBounds(IPoint that, IPoint topRight);
  int getX();
  int getY();
}

// To represent a 2-d point by Cartesian coordinates
class CartPt implements IPoint {
  int x;
  int y;

  CartPt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // To compute the distance from this point to the origin
  public double distanceToOrigin() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  public boolean isInRadius(IPoint that, int radius) {
    int x = this.x - that.getX();
    int y = this.y - that.getY();
    return Math.sqrt(x * x + y * y) < radius;
  }

  public boolean isInBounds(IPoint that, IPoint topRight) {
    return (this.x <= that.getX()) &&
        (this.y <= that.getY()) &&
        (topRight.getX() >= that.getX()) &&
        (topRight.getY() >= that.getY());
  }

  public int getX() { return x; }

  public int getY() { return y; }
}

class PolarPt implements IPoint {
  int r;
  int theta;

  PolarPt(int r, int theta) {
    this.r = r;
    this.theta = theta;
  }

  public double distanceToOrigin() {
    return r;
  }

  public boolean isInRadius(IPoint that, int radius) {
    return this.toCartPt().isInRadius(that, radius);
  }

  public boolean isInBounds(IPoint that, IPoint topRight) {
    return this.toCartPt().isInBounds(that, topRight);
  }

  public CartPt toCartPt() {
    return new CartPt(getX(), getY());
  }

  public int getX() {
    double x = r * Math.cos(Math.toRadians(theta));
    return (int) x;
  }

  public int getY() {
    double y = r * Math.sin(Math.toRadians(theta));
    return (int) y;
  }
}

// to represent a circle
class Circle implements IShape {
  IPoint center;
  int radius;
  String color;

  Circle(IPoint center, int radius, String color) {
    this.center = center;
    this.radius = radius;
    this.color = color;
  }

  /* TEMPLATE
     FIELDS:
     ... this.x ...                   -- int
     ... this.y ...                   -- int
     ... this.radius ...              -- int
     ... this.color ...               -- String
     METHODS
     ... this.area() ...              -- double
  */

  // to compute the area of this shape
  public double area() {
    return Math.PI * this.radius * this.radius;
  }

  public double distanceToOrigin() {
    return this.center.distanceToOrigin() - this.radius;
  }

  public IShape grow(int inc) {
    int newRadius = this.radius + inc;
    return new Circle(this.center, newRadius, this.color);
  }

  public boolean isBiggerThan(IShape that) {
    return this.area() > that.area();
  }

  public boolean contains(IPoint point) {
    return this.center.isInRadius(point, this.radius);
  }
}

// to represent a square
class Square implements IShape {
  IPoint topLeft;
  int size;
  String color;

  Square(IPoint topLeft, int size, String color) {
    this.topLeft = topLeft;
    this.size = size;
    this.color = color;
  }

  /* TEMPLATE
     FIELDS:
     ... this.x ...               -- int
     ... this.y ...               -- int
     ... this.size ...            -- int
     ... this.color ...           -- String
     METHODS:
     ... this.area() ...                  -- double
  */

  // to compute the area of this shape
  public double area() {
    return this.size * this.size;
  }

  public double distanceToOrigin() {
    return this.topLeft.distanceToOrigin();
  }

  public IShape grow(int inc) {
    return new Circle(this.topLeft, this.size + inc, this.color);
  }

  public boolean isBiggerThan(IShape that) {
    return this.area() > that.area();
  }

  public boolean contains(IPoint point) {
    IPoint topRight = new CartPt(this.topLeft.getX() + this.size, this.topLeft.getY() + this.size);
    return this.topLeft.isInBounds(point, topRight);
  }
}

class Combo implements IShape {
  IShape shape1;
  IShape shape2;

  Combo(IShape shape1, IShape shape2) {
    this.shape1 = shape1;
    this.shape2 = shape2;
  }

  public double distanceToOrigin() {
    return Math.min(this.shape1.distanceToOrigin(), this.shape2.distanceToOrigin());
  }

  public double area() {
    return this.shape1.area() + this.shape2.area();
  }

  public IShape grow(int inc) {
    return new Combo(this.shape1.grow(inc), this.shape2.grow(inc));
  }

  public boolean isBiggerThan(IShape that) {
    return this.area() > that.area();
  }

  public boolean contains(IPoint point) {
    return this.shape1.contains(point) || this.shape2.contains(point);
  }
}


class ExamplesShapes {
  ExamplesShapes() {}

  IShape c1 = new Circle(new CartPt(50, 50), 10, "red");
  IShape s1 = new Square(new CartPt(50, 50), 30, "red");

  IShape combo = new Combo(c1, s1);

  IPoint p1 = new CartPt(55, 55);  // Inside the circle
  IPoint p2 = new CartPt(30, 30);  // Outside both
  IPoint p3 = new CartPt(60, 60);  // Inside the square, but outside the circle

  boolean testIShapeArea(Tester t) {
    return
        t.checkInexact(this.c1.area(), 314.15, 0.001) &&
            t.checkInexact(this.s1.area(), 900.0, 0.001) &&
            t.checkInexact(this.combo.area(), 1214.15, 0.001);
  }

  boolean testIShapeContains(Tester t) {
    return
        t.checkExpect(this.c1.contains(p1), true) &&
            t.checkExpect(this.c1.contains(p2), false) &&
            t.checkExpect(this.s1.contains(p1), true) &&
            t.checkExpect(this.s1.contains(p2), false);
  }

  boolean testComboContains(Tester t) {
    return
        t.checkExpect(this.combo.contains(p1), true) &&
            t.checkExpect(this.combo.contains(p2), false) &&
            t.checkExpect(this.combo.contains(p3), true);
  }
}