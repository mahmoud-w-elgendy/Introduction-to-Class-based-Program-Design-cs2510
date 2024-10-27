package assignment6;

import tester.Tester;

import java.util.Comparator;

class Book {
  String title;
  String author;
  int price;

  public Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

class BooksByTitle implements Comparator<Book> {
  public int compare(Book b1, Book b2) {
    return b1.title.compareTo(b2.title);
  }
}

class BooksByAuthor implements Comparator<Book> {
  public int compare(Book b1, Book b2) {
    return b1.author.compareTo(b2.author);
  }
}

class BooksByPrice implements Comparator<Book> {
  public int compare(Book b1, Book b2) {
    return b1.price - b2.price;
  }
}


abstract class ABST<T> {
  Comparator<T> order;
  public ABST(Comparator<T> order) {
    this.order = order;
  }

  abstract public ABST<T> insert(T t);
  abstract public boolean present(T t);

  abstract public T getLeftMost();
  abstract T getLeftMostHelper(Node<T> prev);

  abstract public ABST<T> getRight();
  abstract ABST<T> getRightHelper(Node<T> prev);

  abstract public boolean sameTree(ABST<T> that);
  abstract boolean sameLeaf(Leaf<T> that);
  abstract boolean sameNode(Node<T> that);

  abstract public boolean sameData(ABST<T> that);
  abstract boolean sameDataLeaf(Leaf<T> that);
  abstract boolean sameDataNode(Node<T> that);

  abstract boolean allPresent(ABST<T> that); // return true if every data in this is present in that
  abstract int length();

  abstract public IList<T> buildList();
}

class Leaf<T> extends ABST<T> {
  public Leaf(Comparator<T> order) {
    super(order);
  }

  public ABST<T> insert(T t) {
    return new Node<>(order, t, new Leaf<T>(order), new Leaf<T>(order));
  }

  public boolean present(T t) { return false; }

  public T getLeftMost() { throw new RuntimeException("No leftmost item of an empty tree"); }

  T getLeftMostHelper(Node<T> prev) { return prev.data; }

  public ABST<T> getRight() { throw new RuntimeException("No right of an empty tree"); }

  ABST<T> getRightHelper(Node<T> prev) { return prev.right; }

  public boolean sameTree(ABST<T> that) { return that.sameLeaf(this); }

  boolean sameLeaf(Leaf<T> that) { return true; }

  boolean sameNode(Node<T> that) { return false; }

  public boolean sameData(ABST<T> that) { return that.sameDataLeaf(this); }

  boolean sameDataLeaf(Leaf<T> tLeaf) { return true; }

  boolean sameDataNode(Node<T> node) { return false; }

  boolean allPresent(ABST<T> that) { return true; }

  int length() { return 0; }

  public IList<T> buildList() { return new MtList<T>(); }
}

class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  public Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  public ABST<T> insert(T t) {
    if (order.compare(data, t) < 0) {
      return new Node<>(order, data, left, right.insert(t));
    } else {
      return new Node<>(order, data, left.insert(t), right);
    }
  }

  public boolean present(T t) {
    return (order.compare(data, t) == 0) || left.present(t) || right.present(t);
  }

  public T getLeftMost() {
    return left.getLeftMostHelper(this);
  }

  T getLeftMostHelper(Node<T> prev) {
    return left.getLeftMostHelper(this);
  }

  public ABST<T> getRight() {
    return left.getRightHelper(this);
  }

  ABST<T> getRightHelper(Node<T> prev) {
    return new Node<>(prev.order, prev.data, left.getRightHelper(this), prev.right);
  }

  public boolean sameTree(ABST<T> that) { return that.sameNode(this); }

  boolean sameLeaf(Leaf<T> that) { return false; }

  boolean sameNode(Node<T> that) {
    return (order.compare(data, that.data) == 0) &&
        left.sameTree(that.left) && right.sameTree(that.right);
  }

  public boolean sameData(ABST<T> that) {
    return that.sameDataNode(this);
  }

  boolean sameDataLeaf(Leaf<T> tLeaf) { return false; }

  boolean sameDataNode(Node<T> that) {
    // two trees have the same data if:
    // 1. every data in tree 1 exists in tree 2
    // 2. the trees have the same length, therefore there is no extraneous data in tree 2
    return (this.length() == that.length()) && this.allPresent(that);
  }

  boolean allPresent(ABST<T> that) {
    return that.present(this.data) && left.allPresent(that) && right.allPresent(that);
  }

  int length() {
    return 1 + left.length() + right.length();
  }

  public IList<T> buildList() {
    // As BSTs are sorted, we only need to get the left item each time, then repeat with the rest
    // of the BST
    return new ConsList<>(this.getLeftMost(), this.getRight().buildList());
  }
}

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


class ExamplesABST {
  Book b1 = new Book("Book one", "One Dude", 50);
  Book b2 = new Book("Book two", "Dude Two", 32);
  Book b3 = new Book("The Boring Book", "Boring Dude", 100);
  Book b4 = new Book("The Really Boring Book", "Boring Dude", 97);
  Book b5 = new Book("Not a Book", "Not a Dude" , 0);

  Comparator<Book> bbt = new BooksByTitle();
  Comparator<Book> bba = new BooksByAuthor();
  Comparator<Book> bbp = new BooksByPrice();

  // A tree with a single node(ordered by author) for testing
  ABST<Book> btNode1 = new Node<>(bbt, b1, new Leaf<>(bbt), new Leaf<>(bbt));

  // A tree of Book nodes sorted by title, sides are about equal
  ABST<Book> btNode2 = new Node<>(bbt, b5,
      new Node<>(bbt, b2,
          new Node<>(bbt, b1, new Leaf<>(bbt), new Leaf<>(bbt)),
          new Leaf<>(bbt)),
      new Node<>(bbt, b4,
          new Node<>(bbt, b3, new Leaf<>(bbt), new Leaf<>(bbt)),
          new Leaf<>(bbt))
  );

  // Ordered by author, maximum depth - right side only.
  ABST<Book> baLeaf = new Leaf<>(bba);
  ABST<Book> baNode = new Node<>(bba, b3, baLeaf, new Node<>(bba, b4, baLeaf, new Node<>(bba, b2,
   baLeaf, new Node<>(bba, b5, baLeaf, new Node<>(bba, b1, baLeaf, baLeaf)))));

  // Ordered by price, tilted to the right
  ABST<Book> bpLeaf = new Leaf<>(bbp);
  ABST<Book> bpNode = new Node<>(bbp, b2,
      new Node<>(bbp, b5, bpLeaf, bpLeaf),
      new Node<>(bbp, b4,
          new Node<>(bbp, b1, bpLeaf, bpLeaf),
          new Node<>(bbp, b3, bpLeaf,
          bpLeaf)));

  IList<Book> l0 = new MtList<>();
  IList<Book> btList = new ConsList<>(b1, new ConsList<>(b2, new ConsList<>(b5, new ConsList<>(b3,
      new ConsList<>(b4, l0)))));
  IList<Book> baList = new ConsList<>(b3, new ConsList<>(b4, new ConsList<>(b2, new ConsList<>(b5,
      new ConsList<>(b1, l0)))));
  IList<Book> bpList = new ConsList<>(b5, new ConsList<>(b2, new ConsList<>(b1, new ConsList<>(b4,
      new ConsList<>(b3, l0)))));


  boolean testInsert(Tester t) {
    Book first = new Book("A", "A", -2);
    Book last = new Book("Zen", "Zen", 1000);
    return t.checkExpect(btNode1.insert(b2), new Node<>(bbt, b1, new Leaf<>(bbt),
        new Node<>(bbt, b2, new Leaf<>(bbt), new Leaf<>(bbt)))) &&
        t.checkExpect(btNode1.insert(new Book("A Book", "An Author", 32)), new Node<>(bbt, b1,
            new Node<>(bbt, new Book("A Book", "An Author", 32), new Leaf<>(bbt), new Leaf<>(bbt)),
            new Leaf<>(bbt))) &&
        t.checkExpect(btNode2.insert(last),
            new Node<>(bbt, b5,
                new Node<>(bbt, b2,
                    new Node<>(bbt, b1, new Leaf<>(bbt), new Leaf<>(bbt)),
                    new Leaf<>(bbt)),
                new Node<>(bbt, b4,
                    new Node<>(bbt, b3, new Leaf<>(bbt), new Leaf<>(bbt)),
                    new Node<>(bbt, last, new Leaf<>(bbt), new Leaf<>(bbt)))
            )) &&
        t.checkExpect(btNode2.insert(new Book("Book pne", "S", 2)),
            new Node<>(bbt, b5,
                new Node<>(bbt, b2,
                    new Node<>(bbt, b1,
                        new Leaf<>(bbt),
                        new Node<>(bbt, new Book("Book pne", "S", 2), new Leaf<>(bbt),
                            new Leaf<>(bbt)       )),
                    new Leaf<>(bbt)),
                new Node<>(bbt, b4,
                    new Node<>(bbt, b3, new Leaf<>(bbt), new Leaf<>(bbt)),
                    new Leaf<>(bbt))
            )) &&
        t.checkExpect(baNode.insert(first),
            new Node<>(bba, b3, new Node<>(bba, first, baLeaf, baLeaf), new Node<>(bba, b4, baLeaf,
                new Node<>(bba, b2,
                baLeaf, new Node<>(bba, b5, baLeaf, new Node<>(bba, b1, baLeaf, baLeaf)))))
            ) &&
        t.checkExpect(bpNode.insert(last),
            new Node<>(bbp, b2,
                new Node<>(bbp, b5, bpLeaf, bpLeaf),
                new Node<>(bbp, b4,
                    new Node<>(bbp, b1, bpLeaf, bpLeaf),
                    new Node<>(bbp, b3, bpLeaf,
                        new Node<>(bbp, last, bpLeaf, bpLeaf)))));
  }

  boolean testPresent(Tester t) {
    return t.checkExpect(bpLeaf.present(b3), false) &&
        t.checkExpect(btNode2.present(b5), true) &&
        t.checkExpect(baNode.present(b1), true) &&
        t.checkExpect(baNode.present(b5), true) &&
        t.checkExpect(baNode.present(new Book("Book one", "S", 50)), false) &&
        t.checkExpect(baNode.present(new Book("Book one", "Dude Two", 50)), true) &&
        t.checkExpect(bpNode.present(b3), true) &&
        t.checkExpect(bpNode.present(new Book("Zen", "Zen", 1000)), false);
  }

  boolean testGetLeftMost(Tester t) {
    return t.checkException(new RuntimeException("No leftmost item of an empty tree"),
        baLeaf, "getLeftMost") &&
        t.checkExpect(btNode2.getLeftMost(), b1) &&
        t.checkExpect(baNode.getLeftMost(), b3) &&
        t.checkExpect(bpNode.getLeftMost(), b5);
  }

  boolean testGetRight(Tester t) {
    return t.checkException(new RuntimeException("No right of an empty tree"),
        bpLeaf, "getRight") &&
        t.checkExpect(btNode1.getRight(), new Leaf<>(bbt)) &&
        t.checkExpect(btNode2.getRight(),
            new Node<>(bbt, b5,
                new Node<>(bbt, b2,
                    new Leaf<>(bbt),
                    new Leaf<>(bbt)),
                new Node<>(bbt, b4,
                    new Node<>(bbt, b3, new Leaf<>(bbt), new Leaf<>(bbt)),
                    new Leaf<>(bbt))
            )
            ) &&
        t.checkExpect(baNode.getRight(),
            new Node<>(bba, b4, baLeaf, new Node<>(bba, b2,
                baLeaf, new Node<>(bba, b5, baLeaf, new Node<>(bba, b1, baLeaf, baLeaf))))) &&
        t.checkExpect(bpNode.getRight(),
            new Node<>(bbp, b2,
                new Leaf<>(bbp),
                new Node<>(bbp, b4,
                    new Node<>(bbp, b1, bpLeaf, bpLeaf),
                    new Node<>(bbp, b3, bpLeaf,
                        bpLeaf)))
            ) &&
        t.checkExpect(
            new Node<>(bbt, b5,
                new Node<>(bbt, b2,
                    new Node<>(bbt, b3,
                        new Leaf<>(bbt),
                        new Node<>(bbt, b1, new Leaf<>(bbt), new Leaf<>(bbt))),
                    new Leaf<>(bbt)),
                new Node<>(bbt, b4,
                    new Node<>(bbt, b3, new Leaf<>(bbt), new Leaf<>(bbt)),
                    new Leaf<>(bbt))).getRight(),
            new Node<>(bbt, b5,
                new Node<>(bbt, b2,
                    new Node<>(bbt, b1, new Leaf<>(bbt), new Leaf<>(bbt)),
                    new Leaf<>(bbt)),
                new Node<>(bbt, b4,
                    new Node<>(bbt, b3, new Leaf<>(bbt), new Leaf<>(bbt)),
                    new Leaf<>(bbt))));
  }

  boolean testSameTree(Tester t) {
    return t.checkExpect(bpLeaf.sameTree(bpLeaf), true) &&
        t.checkExpect(baNode.sameTree(baLeaf), false) &&
        t.checkExpect(bpLeaf.sameTree(bpNode), false) &&
        t.checkExpect(btNode1.sameTree(btNode1), true) &&
        t.checkExpect(btNode1.sameTree(btNode2), false) &&
        t.checkExpect(btNode2.sameTree(btNode2), true) &&
        t.checkExpect(bpNode.insert(b2).sameTree(bpNode), false) &&
        t.checkExpect(bpNode.sameTree(bpNode.insert(b2)), false) &&
        t.checkExpect(baNode.sameTree(btNode2), false) &&
        t.checkExpect(baNode.insert(b5).sameTree(baNode), false);
  }

  boolean testSameData(Tester t) {
    return t.checkExpect(bpLeaf.sameData(bpLeaf), true) &&
        t.checkExpect(baNode.sameData(baLeaf), false) &&
        t.checkExpect(bpLeaf.sameData(bpNode), false) &&
        t.checkExpect(btNode1.sameData(btNode1), true) &&
        t.checkExpect(btNode1.sameData(btNode2), false) &&
        t.checkExpect(btNode2.sameData(btNode2), true) &&
        t.checkExpect(bpNode.insert(b2).sameData(bpNode), false) &&
        t.checkExpect(bpNode.sameData(bpNode.insert(b2)), false) &&
        t.checkExpect(baNode.sameData(btNode2), true) &&
        t.checkExpect(btNode2.sameData(baNode), true) &&
        t.checkExpect(bpNode.sameData(btNode2), true) &&
        t.checkExpect(bpNode.sameData(baNode), true) &&
        t.checkExpect(baNode.sameData(bpNode), true) &&
        t.checkExpect(baNode.insert(b5).sameData(baNode), false) &&
        t.checkExpect(baNode.sameData(bpNode.insert(b4)), false);
  }

  boolean testBuildList(Tester t) {
    return t.checkExpect(bpLeaf.buildList(), l0) &&
        t.checkExpect(new Node<>(bbp, b2,
            new Leaf<>(bbp),
            new Node<>(bbp, b4, bpLeaf, bpLeaf)).buildList(),
            new ConsList<>(b2, new ConsList<Book>(b4, l0))) &&
        t.checkExpect(new Node<>(bbp, b2,
                new Node<>(bbp, b5, bpLeaf, bpLeaf),
                new Node<>(bbp, b4, bpLeaf, bpLeaf)).buildList(),
            new ConsList<>(b5, new ConsList<>(b2, new ConsList<Book>(b4, l0)))) &&
        t.checkExpect(btNode2.buildList(), btList) &&
        t.checkExpect(baNode.buildList(), baList) &&
        t.checkExpect(bpNode.buildList(), bpList);
  }
}