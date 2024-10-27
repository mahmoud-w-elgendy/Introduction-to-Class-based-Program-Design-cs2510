package lab10.problem3;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

class ListOfLists<T> implements Iterable<T> {
  ArrayList<ArrayList<T>> contents;
  public ListOfLists() {
    contents = new ArrayList<>();
  }

  void addNewList() {
    contents.add(new ArrayList<>());
  }

  void add(int index, T object) {
    checkOutOfBounds(index);
    contents.get(index).add(object);
  }

  void checkOutOfBounds(int index) {
    if (index < 0 || index >= contents.size()) {
      throw new IndexOutOfBoundsException("No list at the specified index");
    }
  }

  ArrayList<T> get(int index) {
    checkOutOfBounds(index);
    return contents.get(index);
  }

  int size() {
    return contents.size();
  }

  public Iterator<T> iterator() {
    return new ListOfListsIterator<>(this);
  }
}


class ListOfListsIterator<T> implements Iterator<T> {
  ListOfLists<T> list;
  int index;
  Iterator<T> arrayListIterator;

  public ListOfListsIterator(ListOfLists<T> list) {
    this.list = list;
    this.index = 0;
    this.arrayListIterator = Collections.emptyIterator(); // Just for clarity, could use any
    // empty iterator
  }

  public boolean hasNext() {
    return arrayListIterator.hasNext() || (index < list.size());
  }

  public T next() {
    if (arrayListIterator.hasNext()) {
      return arrayListIterator.next();
    } else {
      arrayListIterator = list.get(index).iterator();
      index++;
      return next();
    }
  }
}


class ExamplesListOfLists {
  void test(Tester t) {
    ListOfLists<Integer> lol = new ListOfLists<>();
    t.checkException(new IndexOutOfBoundsException("No list at the specified index"), lol, "get",
        0);
    t.checkException(new IndexOutOfBoundsException("No list at the specified index"), lol, "add",
        0, 0);
    lol.addNewList();
    lol.addNewList();
    lol.add(0, 0);
    lol.add(0, 1);
    t.checkExpect(lol.get(1), new ArrayList<>());
    t.checkExpect(lol.get(0), new ArrayList<>(Arrays.asList(0, 1)));
  }

  void testListOfLists(Tester t) {
    ListOfLists<Integer> lol = new ListOfLists<Integer>();
    //add 3 lists
    lol.addNewList();
    lol.addNewList();
    lol.addNewList();

    //add elements 1,2,3 in first list
    lol.add(0,1);
    lol.add(0,2);
    lol.add(0,3);

    //add elements 4,5,6 in second list
    lol.add(1,4);
    lol.add(1,5);
    lol.add(1,6);

    //add elements 7,8,9 in third list
    lol.add(2,7);
    lol.add(2,8);
    lol.add(2,9);

    //iterator should return elements in order 1,2,3,4,5,6,7,8,9
    int number = 1;
    for (Integer num: lol) {
      t.checkExpect(num,number);
      number = number + 1;
    }
  }
}