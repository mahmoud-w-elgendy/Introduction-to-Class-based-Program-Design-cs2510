package lecture16.firstSolution;

interface IFunc<A, R> {
  R apply(A arg);
}

interface IShape {
  Double beAppliedToBy(IShape2DoubleFunc func);
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

  public Double beAppliedToBy(IShape2DoubleFunc func) {
    return func.applyToCircle(this);
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

  public Double beAppliedToBy(IShape2DoubleFunc func) {
    return func.applyToRect(this);
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

  public Double beAppliedToBy(IShape2DoubleFunc func) {
    return func.applyToSquare(this);
  }
}

// Represents a function object defined over Shapes that returns a Double
interface IShape2DoubleFunc {
  double applyToCircle(Circle circle);
  double applyToRect(Rect rect);
  double applyToSquare(Square square);
}

class ShapePerimeter implements IShape2DoubleFunc {
  public double applyToCircle(Circle circle) {
    return 2 * Math.PI * circle.radius;
  }

  public double applyToRect(Rect rect) {
    return 2 * (rect.w + rect.h);
  }

  public double applyToSquare(Square square) {
    return 4 * square.size;
  }
}
