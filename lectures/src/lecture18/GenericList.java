package lecture18;

interface IList<T> { }

class MtList<T> implements IList<T> { }

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  public ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}
