package lecture30;

import java.util.ArrayList;
import java.util.Iterator;

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

interface IList<T> extends Iterable<T>  {
  T find(IPred<T> whichOne);
  // EFFECT: Finds and modifies the person in this list matching the
  // given predicate, by using the given operation
  Void find(IPred<T> whichOne, IFunc<T, Void> whatToDo);
  boolean isCons();
  ConsList<T> asCons();
  IList<T> reverse();
  IList<T> reverseHelper(IList<T> ts);
  boolean contains(T t);
}

class MtList<T> implements IList<T> {
  public T find(IPred<T> whichOne) {
    return null;
  }

  public Void find(IPred<T> whichOne, IFunc<T, Void> whatToDo) { return null; }

  public boolean isCons() {
    return false;
  }

  public ConsList<T> asCons() {
    throw new UnsupportedOperationException("Not a Cons!");
  }

  public IList<T> reverse() {
    return this;
  }

  public IList<T> reverseHelper(IList<T> acc) {
    return acc;
  }

  public boolean contains(T t) {
    return false;
  }

  public Iterator<T> iterator() {
    return new IListIterator<T>(this);
  }
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

  public boolean isCons() {
    return true;
  }

  public ConsList<T> asCons() {
    return this;
  }

  public IList<T> reverse() {
    return reverseHelper(new MtList<>());
  }

  public IList<T> reverseHelper(IList<T> acc) {
    return rest.reverseHelper(new ConsList<>(first, acc));
  }

  public boolean contains(T t) {
    return first.equals(t) || rest.contains(t);
  }

  public Iterator<T> iterator() {
    return new IListIterator<T>(this);
  }
}


class IListIterator<T> implements Iterator<T> {
  IList<T> items;

  public IListIterator(IList<T> items) {
    this.items = items;
  }

  public boolean hasNext() {
    return items.isCons();
  }

  public T next() {
    ConsList<T> itemsAsCons = this.items.asCons();
    this.items = itemsAsCons.rest;
    return itemsAsCons.first;
  }

  public void remove() {
    throw new UnsupportedOperationException("Don't do this!");
  }
}


class Utils {
  <T> IList<T> listFromArrayList(ArrayList<T> arr) {
    IList<T> list = new MtList<>();
    for (T t: arr) {
      list = new ConsList<>(t, list);
    }
    return list.reverse();
  }
}

