package lecture15;

import tester.Main;

interface IPred<T> {
  boolean apply(T t);
}

interface IComparator<T> {
  int compare(T t1, T t2);
}
interface IFunc<A, R> {
  R apply(A arg);
}
interface IFunc2<A, B, R> {
  R apply(A arg1, B arg2);
}

interface IList<T> {
  IList<T> filter(IPred<T> pred);
  IList<T> sort(IComparator<T> comp);
  IList<T> insert(IComparator<T> comp, T t);
  int length();
  <U> IList<U> map(IFunc<T, U> f);
  <U> U foldr(IFunc2<T, U, U> f, U base);
}

class MtList<T> implements IList<T> {
  public IList<T> filter(IPred<T> pred) { return this; }
  public IList<T> sort(IComparator<T> comp) { return this; }

  public IList<T> insert(IComparator<T> comp, T t) {
    return new ConsList<T>(t, this);
  }

  public int length() { return 0; }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }

  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return base;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  public ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public IList<T> filter(IPred<T> pred) {
    if (pred.apply(first)) {
      return new ConsList<T>(first, rest.filter(pred));
    } else {
      return rest.filter(pred);
    }
  }

  public IList<T> sort(IComparator<T> comp) {
    return rest.sort(comp).insert(comp, first);
  }

  public IList<T> insert(IComparator<T> comp, T t) {
    if (comp.compare(first, t) < 0) {
      return new ConsList<T>(first, rest.insert(comp, t));
    } else {
      return new ConsList<T>(t, this);
    }
  }

  public int length() {
    return 1 + rest.length();
  }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(first), rest.map(f));
  }

  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return f.apply(first,
              rest.foldr(f, base));
  }
}


class AccumulatePrice implements IFunc2<Book, Integer, Integer> {
  public Integer apply(Book arg1, Integer arg2) {
    return arg1.price + arg2;
  }
}

class Utils {
  Integer totalPrice(IList<Book> books) {
    return books.foldr(new AccumulatePrice(), 0);
  }
}


interface IShape {
  <U> U beAppliedTo(IApplyToShape<U> f);
}

class Circle implements IShape{
  int x, y;
  int radius;
  Circle(int x, int y, int radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
  public <U> U beAppliedTo(IApplyToShape<U> f) {
    return f.applyToCircle(this);
  }
}

class Rect implements IShape {
  int x, y;
  int width, height;

  public Rect(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public <U> U beAppliedTo(IApplyToShape<U> f) {
    return f.applyToRect(this);
  }
}

interface IApplyToShape<T> extends IFunc<IShape, T> {
  T applyToCircle(Circle c);
  T applyToRect(Rect r);
}


class Perimeter implements IApplyToShape<Double> {
  public Double apply(IShape shape) {
    return shape.beAppliedTo(this);
  }

  public Double applyToCircle(Circle c) {
    return 2 * c.radius * Math.PI;
  }

  public Double applyToRect(Rect r) {
    return r.height * 2.0 + r.width * 2;
  }
}