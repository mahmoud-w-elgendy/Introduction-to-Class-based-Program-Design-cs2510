package lab6;

import tester.Tester;

// Represents functions of signature A -> R, for some argument type A and
// result type R
interface IFunc<A, R> {
  R apply(A input);
}

interface IFunc2<A, B, R> {
  R apply(A arg1, B arg2);
}

// generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(IFunc<T, U> f);
  <U> U foldr(IFunc2<T, U, U> func, U base);
  <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup);
}

// empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }

  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return base;
  }

  public <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup) {
    return backup;
  }
}

// non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }

  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return func.apply(first, rest.foldr(func, base));
  }

  public <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup) {
    U result = convert.apply(first);
    if (pred.apply(result)) {
      return result;
    } else {
      return rest.findSolutionOrElse(convert, pred, backup);
    }
  }
}

class SumNumbers implements IFunc2<Integer, Integer, Integer> {
  public Integer apply(Integer arg1, Integer arg2) {
    return arg1 + arg2;
  }
}

interface IPred<T> extends IFunc<T, Boolean> { }

class ExamplesFoldr {
  boolean testSum(Tester t) {
    IList<Integer> lon0 = new MtList<Integer>();
    IList<Integer> lon1 = new ConsList<Integer>(14, lon0);
    IList<Integer> lon2 = new ConsList<Integer>(1, lon1);
    IList<Integer> lon3 = new ConsList<Integer>(-2, lon2);

    return t.checkExpect(lon0.foldr(new SumNumbers(), 0), 0) &&
        t.checkExpect(lon1.foldr(new SumNumbers(), 0), 14) &&
        t.checkExpect(lon2.foldr(new SumNumbers(), 0), 15) &&
        t.checkExpect(lon3.foldr(new SumNumbers(), 0), 13);
  }
}