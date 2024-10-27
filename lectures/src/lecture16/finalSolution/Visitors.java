package lecture16.finalSolution;

interface IFunc<A, R> {
  R apply(A arg);
}

interface IShape {
  <R> R accept(IShapeVisitor<R> func);
}
class Circle implements IShape {
  int x, y;
  int radius;
  String color;
  Circle(int x, int y, int r, String color) {
    this.x = x;
    this.y = y;
    this.radius = r;
    this.color = color;
  }

  public <R> R accept(IShapeVisitor<R> func) {
    return func.visitCircle(this);
  }
}
class Rect implements IShape {
  int x, y, w, h;
  String color;
  Rect(int x, int y, int w, int h, String color) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.color = color;
  }

  public <R> R accept(IShapeVisitor<R> func) {
    return func.visitRect(this);
  }
}

class Square implements IShape {
  int x, y, size;
  String color;
  Square(int x, int y, int size, String color) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.color = color;
  }

  public <R> R accept(IShapeVisitor<R> func) {
    return func.visitSquare(this);
  }
}


interface IShapeVisitor<R> extends IFunc<IShape, R> {
  R visitCircle(Circle circle);
  R visitRect(Rect rect);
  R visitSquare(Square square);
}

// A function object that computes the area of IShapes
class ShapeArea implements IShapeVisitor<Double> {
  public Double apply(IShape shape) {
    return shape.accept(this);
  }

  public Double visitCircle(Circle circle) {
    return Math.PI * circle.radius * circle.radius;
  }

  public Double visitRect(Rect rect) {
    return (double) rect.w * rect.h;
  }

  public Double visitSquare(Square square) {
    return (double) square.size * square.size;
  }
}