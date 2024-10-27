package lecture25;

import java.util.Iterator;

class TakeN<T> implements Iterator<T> {
  Iterator<T> source;
  int n;

  public TakeN(Iterator<T> source, int n) {
    this.source = source;
    this.n = n;
  }

  public boolean hasNext() {
    return this.source.hasNext() && this.n > 0;
  }

  public T next() {
    this.n--;
    return this.source.next();
  }

  public void remove() {
    this.source.remove();
  }
}


class InterleaveIterator<T> implements Iterator<T> {
  Iterator<T> source1;
  Iterator<T> source2;
  Iterator<T> current;

  public InterleaveIterator(Iterator<T> source1, Iterator<T> source2) {
    this.source1 = source1;
    this.source2 = source2;
    this.current = source1;
  }

  public boolean hasNext() {
    return current.hasNext();
  }

  public T next() {
    T answer = this.current.next();
    this.current = getOther();
    return answer;
  }

  private Iterator<T> getOther() {
    return this.current == this.source1 ? this.source2 : this.source1;
  }
}