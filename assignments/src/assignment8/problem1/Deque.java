package assignment8.problem1;

import tester.Tester;

import java.util.function.Predicate;

public class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // A helper method for testing convenience
  public String toString() {
    return header.toString();
  }

  int count() {
    return header.count();
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

  ANode<T> find(Predicate<T> pred) {
    return header.find(pred);
  }

  void removeNode(ANode<T> node) {
    // Not mentioned by the problem definition, but we probably
    // don't want to do anything if the node is not in this deque
    if (!hasNode(node)) { return; }

    node.prev.setNext(node.next);
  }

  boolean hasNode(ANode<T> node) {
    return (header != find(new IsEqual<>(node.getData())));
  }
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  public String toString() {
    return "(sentinel " + next.toStringHelper();
  }

  String toStringHelper() { return "sentinel)"; }

  int count() { return next.countHelper(); }

  int countHelper() { return 0; }

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

  public ANode<T> find(Predicate<T> pred) {
    return next.findHelper(pred);
  }

  ANode<T> findHelper(Predicate<T> pred) { return this; }
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

  String toStringHelper() {
    return this.data.toString() + " " + next.toStringHelper();
  }

  int countHelper() { return 1 + next.countHelper(); }

  protected T getData() { return data; }

  ANode<T> findHelper(Predicate<T> pred) {
    if (pred.test(data)) {
      return this;
    }
    else {
      return next.findHelper(pred);
    }
  }
}

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  abstract String toStringHelper();
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

  abstract ANode<T> findHelper(Predicate<T> pred);
}

// There's actually a built-in predicate for this (Predicate.isEqual)
// BUT we didn't learn about it, so...
class IsEqual<T> implements Predicate<T> {
  T t;

  public IsEqual(T t) {
    this.t = t;
  }

  public boolean test(T t) {
    return this.t.equals(t);
  }
}

class ExamplesDeque {

  Sentinel<String> sentinel2, sentinel3;
  Deque<String> deque1, deque2, deque3;
  Node<String> abc, bcd, cde, def;

  void reset() {
    deque1 = new Deque<>();

    sentinel2 = new Sentinel<>();
    abc = new Node<>("abc", sentinel2, sentinel2);
    bcd = new Node<>("bcd", sentinel2, abc);
    cde = new Node<>("cde", sentinel2, bcd);
    def = new Node<>("def", sentinel2, cde);
    deque2 = new Deque<>(sentinel2);

    sentinel3 = new Sentinel<>();
    new Node<>("blegh",
        new Node<>("wow", new Node<>("aoa", new Node<>("aha", sentinel3, sentinel3), sentinel3),
            sentinel3), sentinel3);
    deque3 = new Deque<>(sentinel3);
  }

  void testCount(Tester t) {
    reset();
    t.checkExpect(deque1.count(), 0);
    t.checkExpect(deque2.count(), 4);
    t.checkExpect(deque3.count(), 4);
  }

  void testAddAtHead(Tester t) {
    reset();

    deque1.addAtHead("hello");
    t.checkExpect(deque1.count(), 1);

    deque2.addAtHead("hello");
    deque2.addAtHead("world");
    t.checkExpect(deque2.toString(), "(sentinel world hello abc bcd cde def sentinel)");
  }

  void testAddAtTail(Tester t) {
    reset();

    deque1.addAtTail("hello");
    t.checkExpect(deque1.count(), 1);

    deque2.addAtTail("hello");
    deque2.addAtTail("world");
    t.checkExpect(deque2.toString(), "(sentinel abc bcd cde def hello world sentinel)");
  }

  void testRemoveFromHead(Tester t) {
    reset();

    t.checkException(new RuntimeException("Attempted to remove from empty list"), deque1,
        "removeFromHead");
    t.checkExpect(deque2.removeFromHead(), "abc");
    t.checkExpect(deque2.toString(), "(sentinel bcd cde def sentinel)");
  }

  void testRemoveFromTail(Tester t) {
    reset();

    t.checkException(new RuntimeException("Attempted to remove from empty list"), deque1,
        "removeFromTail");
    t.checkExpect(deque2.removeFromTail(), "def");
    t.checkExpect(deque2.toString(), "(sentinel abc bcd cde sentinel)");
  }

  void testFind(Tester t) {
    reset();

    t.checkExpect(deque1.find(new IsEqual<>("a")), new Sentinel<String>());
    t.checkExpect(deque2.find(new IsEqual<>("abc")), abc);
    t.checkExpect(deque2.find(new IsEqual<>("cde")), cde);
    t.checkExpect(deque2.find(new IsEqual<>("def")), def);
    t.checkExpect(deque3.find(new IsEqual<>("abc")), sentinel3);
  }

  void testRemoveNode(Tester t) {
    reset();
    deque1.removeNode(abc);
    t.checkExpect(deque1.count(), 0);
    t.checkExpect(deque2.toString(), "(sentinel abc bcd cde def sentinel)");
    deque2.removeNode(cde);
    t.checkExpect(deque2.toString(), "(sentinel abc bcd def sentinel)");
    deque2.removeNode(abc);
    deque2.removeNode(bcd);
    deque2.removeNode(def);
    t.checkExpect(deque2.count(), 0);
  }
}
