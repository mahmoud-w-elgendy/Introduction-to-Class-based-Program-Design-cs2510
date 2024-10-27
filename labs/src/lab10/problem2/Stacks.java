package lab10.problem2;

import java.util.ArrayList;

class Stack<T> {
  Deque<T> contents;

  public Stack() {
    this.contents = new Deque<>();
  }

  void push(T item) {
    contents.addAtHead(item);
  }

  boolean isEmpty() {
    return contents.count() == 0;
  }

  T pop() {
    return contents.removeFromHead();
  }
}
