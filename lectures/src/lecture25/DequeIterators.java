package lecture25;

import java.util.Iterator;

class DequeForwardIterator<T> implements Iterator<T> {
  Deque<T> items;
  ANode<T> item;

  public DequeForwardIterator(Deque<T> items) {
    this.items = items;
    this.item = items.header.next;
  }

  public boolean hasNext() {
    return item != items.header;
  }

  public T next() {
    ANode<T> current = item;
    this.item = item.next;
    return current.getData();
  }
}

class DequeReverseIterator<T> implements Iterator<T> {
  Deque<T> items;
  ANode<T> item;

  public DequeReverseIterator(Deque<T> items) {
    this.items = items;
    this.item = items.header.prev;
  }

  public boolean hasNext() {
    return item != items.header;
  }

  public T next() {
    ANode<T> current = item;
    this.item = item.prev;
    return current.getData();
  }
}

class Deque<T> implements Iterable<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  int count() {
    return header.count();
  }

  boolean contains(T data) {
    return header.contains(data);
  }

  void addAtHead(T t) {
    (new Node<T>(t)).insertBetween(header, header.next);
  }

  void addAtTail(T t) {
    (new Node<T>(t)).insertBetween(header.prev, header);
  }

  T removeFromHead() {
    checkNotEmpty();
    return header.removeFromHead();
  }

  T removeFromTail() {
    checkNotEmpty();
    return header.removeFromTail();
  }

  void checkNotEmpty() {
    if (header == header.next) {
      throw new RuntimeException("Attempted to remove from empty list");
    }
  }

  public Iterator<T> iterator() {
    return new DequeForwardIterator<>(this);
  }

  public Iterator<T> reverseIterator() {
    return new DequeReverseIterator<>(this);
  }
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  int count() { return next.countHelper(); }

  int countHelper() { return 0; }

  boolean containsHelper(T t) {
    return false;
  }

  public T removeFromHead() {
    T t = next.getData();
    this.setNext(next.next);
    return t;
  }

  public T removeFromTail() {
    T t = prev.getData();
    this.setPrev(prev.prev);
    return t;
  }

  public boolean contains(T data) {
    return next.containsHelper(data);
  }
}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    if (prev == null || next == null) {
      throw new IllegalArgumentException("Given null instead of ANode<T>");
    }

    this.data = data;
    this.insertBetween(prev, next);
  }

  int countHelper() { return 1 + next.countHelper(); }

  protected T getData() { return data; }

  boolean containsHelper(T data) {
    return (this.data == data) || next.containsHelper(data);
  }
}

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  abstract int countHelper();

  void setNext(ANode<T> node) {
    this.next = node;
    node.prev = this;
  }

  void setPrev(ANode<T> node) {
    this.prev = node;
    node.next = this;
  }

  // Inserts this between the given nodes
  void insertBetween(ANode<T> before, ANode<T> after) {
    before.setNext(this);
    after.setPrev(this);
  }

  protected T getData() { return null; }

  abstract boolean containsHelper(T data);
}
