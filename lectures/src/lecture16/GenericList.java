package lecture16;

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
