package lecture19;

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
  T find(IPred<T> whichOne);
  // EFFECT: Finds and modifies the person in this list matching the
  // given predicate, by using the given operation
  Void find(IPred<T> whichOne, IFunc<T, Void> whatToDo);
}

class MtList<T> implements IList<T> {
  public T find(IPred<T> whichOne) {
    return null;
  }

  public Void find(IPred<T> whichOne, IFunc<T, Void> whatToDo) { return null; }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  public ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public T find(IPred<T> whichOne) {
    if (whichOne.apply(first)) {
      return first;
    } else {
      return rest.find(whichOne);
    }
  }

  public Void find(IPred<T> whichOne, IFunc<T, Void> whatToDo) {
    if (whichOne.apply(this.first)) {
      whatToDo.apply(this.first);
    }
    else {
      this.rest.find(whichOne, whatToDo);
    }
    return null;
  }
}
