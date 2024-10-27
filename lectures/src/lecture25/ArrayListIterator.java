package lecture25;

import java.util.ArrayList;
import java.util.Iterator;


class ArrayListIterator<T> implements Iterator<T> {
  // the list of items that this iterator iterates over
  ArrayList<T> items;
  // the index of the next item to be returned
  int nextIdx;
  // Construct an iterator for a given ArrayList
  ArrayListIterator(ArrayList<T> items) {
    this.items = items;
    this.nextIdx = 0;
  }

  public boolean hasNext() {
    return items.size() > nextIdx;
  }

  public T next() {
    T next = items.get(nextIdx);
    nextIdx++;
    return next;
  }

  public void remove() {
    throw new UnsupportedOperationException("Don't do this!");
  }
}


