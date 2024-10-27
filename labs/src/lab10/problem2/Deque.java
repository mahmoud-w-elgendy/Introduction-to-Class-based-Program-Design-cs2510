package lab10.problem2;
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