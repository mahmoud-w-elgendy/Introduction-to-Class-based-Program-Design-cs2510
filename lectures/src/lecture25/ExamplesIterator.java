package lecture25;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExamplesIterator {
  Sentinel<String> sentinel1 = new Sentinel<>();
  Node<String> abc = new Node<>("abc", sentinel1, sentinel1);
  Node<String> bcd = new Node<>("bcd", sentinel1, abc);
  Node<String> cde = new Node<>("cde", sentinel1, bcd);
  Node<String> def = new Node<>("def", sentinel1, cde);
  Deque<String> deque1 = new Deque<>(sentinel1);

  Sentinel<String> sentinel2 = new Sentinel<>();
  Node<String> nodes2 = new Node<String>("blegh",
        new Node<String>("wow", new Node<String>("aoa", new Node<String>("aha", sentinel2, sentinel2), sentinel2),
            sentinel2), sentinel2);

  Deque<String> deque2 = new Deque<>(sentinel2);

  IBinaryTree<String> btLeaf = new BTLeaf<>();
  // A tree with a single node A
  IBinaryTree<String> btNode1 = new BTNode<String>("A", new BTLeaf<>(), new BTLeaf<>());

  IBinaryTree<String> btNode2 = new BTNode<>("A",
      new BTNode<>("B",
          new BTNode<>("D", new BTLeaf<>(), new BTLeaf<>()),
          new BTNode<>("E", new BTLeaf<>(), new BTLeaf<>())
      ),
      new BTNode<>("C",
          new BTNode<>("F", new BTLeaf<>(), new BTLeaf<>()),
          new BTNode<>("G", new BTLeaf<>(), new BTLeaf<>()))
  );

  // maximum depth - right side only.
  IBinaryTree<String> btNode3 = new BTNode<>("A", btLeaf, new BTNode<>("B", btLeaf, new BTNode<>(
      "C", btLeaf, new BTNode<>("D", btLeaf, new BTNode<>("E", btLeaf, btLeaf)))));

  // tilted to the right
  IBinaryTree<String> btNode4 = new BTNode<>("A",
      new BTNode<>("B", btLeaf, btLeaf),
      new BTNode<>("C",
          new BTNode<>("D", btLeaf, btLeaf),
          new BTNode<>("E", btLeaf, btLeaf)));


  void testDequeForwardIterator(Tester t) {
    t.checkExpect(iteratorAsList(new DequeForwardIterator<>(new Deque<String>())),
        new ArrayList<>());
    t.checkExpect(iteratorAsList(new DequeForwardIterator<>(deque1)),
        new ArrayList<>(Arrays.asList("abc", "bcd", "cde", "def")));
    t.checkExpect(iteratorAsList(new DequeForwardIterator<>(deque2)),
        new ArrayList<>(Arrays.asList("blegh", "wow", "aoa", "aha")));
  }

  void testDequeReverseIterator(Tester t) {
    t.checkExpect(iteratorAsList(new DequeReverseIterator<>(new Deque<String>())),
        new ArrayList<>());
    t.checkExpect(iteratorAsList(new DequeReverseIterator<>(deque1)),
        new ArrayList<>(Arrays.asList("def", "cde", "bcd", "abc")));
    t.checkExpect(iteratorAsList(new DequeReverseIterator<>(deque2)),
        new ArrayList<>(Arrays.asList("aha", "aoa", "wow","blegh")));
  }

  void testInterleaveIterator(Tester t) {
    t.checkExpect(iteratorAsList(new InterleaveIterator<>(new DequeForwardIterator<String>(new Deque<>()),
            new DequeReverseIterator<String>(new Deque<>()))),
        new ArrayList<>());
    t.checkExpect(iteratorAsList(new InterleaveIterator<>(new DequeForwardIterator<String>(deque1),
            new DequeReverseIterator<String>(deque1))),
        new ArrayList<>(Arrays.asList("abc", "def", "bcd", "cde", "cde", "bcd", "def", "abc")));
    t.checkExpect(iteratorAsList(new InterleaveIterator<>(new DequeReverseIterator<String>(deque1),
            new DequeForwardIterator<String>(deque2))),
        new ArrayList<>(Arrays.asList("def", "blegh", "cde", "wow", "bcd", "aoa", "abc", "aha")));
  }

  void testBTBreadthFirstIterator(Tester t) {
    t.checkExpect(iteratorAsList(new BreadthFirstIterator<String>(btLeaf)), new ArrayList<>());
    t.checkExpect(iteratorAsList(new BreadthFirstIterator<String>(btNode1)),
        new ArrayList<>(List.of("A")));
    t.checkExpect(iteratorAsList(new BreadthFirstIterator<String>(btNode2)),
        new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G")));
    t.checkExpect(iteratorAsList(new BreadthFirstIterator<String>(btNode3)),
        new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
    t.checkExpect(iteratorAsList(new BreadthFirstIterator<String>(btNode4)),
        new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
  }

  void testBTPreOrderIterator(Tester t) {
    t.checkExpect(iteratorAsList(new PreOrderIterator<>(btLeaf)), new ArrayList<>());
    t.checkExpect(iteratorAsList(new PreOrderIterator<String>(btNode1)),
        new ArrayList<>(List.of("A")));
    t.checkExpect(iteratorAsList(new PreOrderIterator<String>(btNode2)),
        new ArrayList<>(Arrays.asList("A", "B", "D", "E", "C", "F", "G")));
    t.checkExpect(iteratorAsList(new PreOrderIterator<String>(btNode3)),
        new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
    t.checkExpect(iteratorAsList(new PreOrderIterator<String>(btNode4)),
        new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
  }

  void testBTPostOrderIterator(Tester t) {
    t.checkExpect(iteratorAsList(new PostOrderIterator<>(btLeaf)), new ArrayList<>());
    t.checkExpect(iteratorAsList(new PostOrderIterator<String>(btNode1)),
        new ArrayList<>(List.of("A")));
    t.checkExpect(iteratorAsList(new PostOrderIterator<String>(btNode2)),
        new ArrayList<>(Arrays.asList("D", "E", "B", "F", "G", "C", "A")));

    t.checkExpect(iteratorAsList(new PostOrderIterator<String>(btNode3)),
        new ArrayList<>(Arrays.asList("E", "D", "C", "B", "A")));

    t.checkExpect(iteratorAsList(new PostOrderIterator<String>(btNode4)),
        new ArrayList<>(Arrays.asList("B", "D", "E", "C", "A")));
  }

  void testBTInOrderIterator(Tester t) {
    t.checkExpect(iteratorAsList(new InOrderIterator<>(btLeaf)), new ArrayList<>());
    t.checkExpect(iteratorAsList(new InOrderIterator<String>(btNode1)),
        new ArrayList<>(List.of("A")));
    t.checkExpect(iteratorAsList(new InOrderIterator<String>(btNode2)),
        new ArrayList<>(Arrays.asList("D", "B", "E", "A", "F", "C", "G")));

    t.checkExpect(iteratorAsList(new InOrderIterator<String>(btNode3)),
        new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));

    t.checkExpect(iteratorAsList(new InOrderIterator<String>(btNode4)),
        new ArrayList<>(Arrays.asList("B", "A", "D", "C", "E")));
  }

  // A helper method for convenient testing
  <T> ArrayList<T> iteratorAsList(Iterator<T> iterator) {
    ArrayList<T> items = new ArrayList<T>();
    while (iterator.hasNext()) {
      items.add(iterator.next());
    }
    return items;
  }
}
