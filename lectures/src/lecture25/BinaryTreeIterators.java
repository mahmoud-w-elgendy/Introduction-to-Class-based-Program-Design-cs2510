package lecture25;

import java.util.ArrayList;
import java.util.Iterator;

interface IBinaryTree<T> {
  boolean isNode();
  BTNode<T> asNode();
}

class BTNode<T> implements IBinaryTree<T> {
  T data;
  IBinaryTree<T> left;
  IBinaryTree<T> right;

  public BTNode(T data, IBinaryTree<T> left, IBinaryTree<T> right) {
    this.data = data;
    this.left = left;
    this.right = right;
  }

  public boolean isNode() { return true; }

  public BTNode<T> asNode() { return this; }
}

class BTLeaf<T> implements IBinaryTree<T> {
  public boolean isNode() { return false; }

  public BTNode<T> asNode() {
    throw new UnsupportedOperationException("Not a BTNode!");
  }
}


class BreadthFirstIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;

  BreadthFirstIterator(IBinaryTree<T> source) {
    this.worklist = new Deque<IBinaryTree<T>>();
    this.addIfNotLeaf(source);
  }

  // EFFECT: only adds the given binary-tree if it's not a leaf
  void addIfNotLeaf(IBinaryTree<T> bt) {
    if (bt.isNode()) {
      this.worklist.addAtTail(bt);
    }
  }

  public boolean hasNext() {
    // we have a next item if the worklist isn't empty
    return this.worklist.count() > 0;
  }

  public T next() {
    // Get (and remove) the first item on the worklist --
    // and we know it must be a BTNode
    BTNode<T> node = this.worklist.removeFromHead().asNode();
    // Add the children of the node to the tail of the list
    this.addIfNotLeaf(node.left);
    this.addIfNotLeaf(node.right);
    // return the answer
    return node.data;
  }

  public void remove() {
    throw new UnsupportedOperationException("Don't do this!");
  }
}

class PreOrderIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;

  public PreOrderIterator(IBinaryTree<T> source) {
    this.worklist = new Deque<>();
    this.addIfNotLeaf(source);
  }

  // EFFECT: only adds the given binary-tree if it's not a leaf
  void addIfNotLeaf(IBinaryTree<T> bt) {
    if (bt.isNode()) {
      this.worklist.addAtHead(bt);
    }
  }

  public boolean hasNext() {
    return this.worklist.count() > 0;
  }

  public T next() {
    BTNode<T> current = this.worklist.removeFromHead().asNode();
    addIfNotLeaf(current.right);
    addIfNotLeaf(current.left);
    return current.data;
  }
}

class PostOrderIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;
  ArrayList<IBinaryTree<T>> visited;

  public PostOrderIterator(IBinaryTree<T> source) {
    this.worklist = new Deque<>();
    this.visited = new ArrayList<>();
    this.addIfNotLeaf(source);
  }

  // EFFECT: only adds the given binary-tree if it's not a leaf
  void addIfNotLeaf(IBinaryTree<T> bt) {
    if (bt.isNode()) {
      this.worklist.addAtHead(bt);
    }
  }

  public boolean hasNext() {
    return this.worklist.count() > 0;
  }

  public T next() {
    BTNode<T> current = this.worklist.removeFromHead().asNode();

    if (isDone(current.left) && isDone(current.right)) {
      visited.add(current);
      return current.data;
    }

    this.worklist.addAtHead(current);
    addIfNotLeaf(current.right);
    addIfNotLeaf(current.left);
    return next();
  }

  // check that there's nothing more to do at the given tree
  // (either because it is a leaf or because it was already visited)
  boolean isDone(IBinaryTree<T> bt) {
    return !bt.isNode() || visited.contains(bt);
  }
}

class InOrderIterator<T> implements Iterator<T> {
  Deque<IBinaryTree<T>> worklist;
  ArrayList<IBinaryTree<T>> visited;

  public InOrderIterator(IBinaryTree<T> source) {
    this.worklist = new Deque<>();
    this.visited = new ArrayList<>();
    this.addIfNotLeaf(source);
  }

  // EFFECT: only adds the given binary-tree if it's not a leaf
  void addIfNotLeaf(IBinaryTree<T> bt) {
    if (bt.isNode()) {
      this.worklist.addAtHead(bt);
    }
  }

  public boolean hasNext() {
    return this.worklist.count() > 0;
  }

  public T next() {
    BTNode<T> current = this.worklist.removeFromHead().asNode();

    if (isDone(current.left)) {
      // Keeps the right node of a tree with no left node
      if (!current.left.isNode()) {
        addIfNotLeaf(current.right);
      }
      visited.add(current);
      return current.data;
    }

    addIfNotLeaf(current.right);
    addIfNotLeaf(current);
    addIfNotLeaf(current.left);
    return next();
  }

  boolean isDone(IBinaryTree<T> bt) {
    return !bt.isNode() || visited.contains(bt);
  }
}
