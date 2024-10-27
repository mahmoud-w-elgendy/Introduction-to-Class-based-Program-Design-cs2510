package lecture31;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;

class HeapNode<T> {
  T data;
  int weight;
  int index; // for the optimization

  public HeapNode(T data, int weight, int index) {
    this.data = data;
    this.weight = weight;
    this.index = index;
  }
}

class Heap<T> {
  ArrayList<HeapNode<T>> contents;
  IComparator<Integer> comp; // Compare by priority

  public Heap(IComparator<Integer> comp) {
    this.comp = comp;
    this.contents = new ArrayList<>();
  }

  public HeapNode<T> add(T data, int weight) {
    HeapNode<T> node = new HeapNode<>(data, weight, contents.size());
    contents.add(node);
    upheap(contents.size() - 1);
    return node;
  }

  T remove() {
    int lastIdx = contents.size() - 1;
    swap(0, lastIdx);
    downheap(0, lastIdx - 1);
    return contents.remove(lastIdx).data;
  }

  private void build() {
    for (int i = 1; i < contents.size(); i++) {
      upheap(i);
    }
  }

  void upheap(int i) {
    int parentIdx = (int) Math.floor((i - 1) / 2.0);
    if (parentIdx < 0) { return; }
    if (compare(parentIdx, i) < 0) {
      swap(parentIdx, i);
      upheap(parentIdx);
    }
  }

  // A convenience wrapper method
  int compare(int i, int j) {
    return comp.compare(contents.get(i).weight, contents.get(j).weight);
  }

  void swap(int index1, int index2) {
    HeapNode<T> item1 = contents.get(index1);
    HeapNode<T> item2 = contents.get(index2);
    // Swap the elements in the heap
    contents.set(index1, item2);
    contents.set(index2, item1);
    // Update their indices
    item1.index = index2;
    item2.index = index1;
  }

  void downheap(int i, int lastIdx) {
    int leftIdx = 2*i + 1;
    int rightIdx = 2*i + 2;
    Integer biggerChild = findBiggerChild(leftIdx, rightIdx, lastIdx);

    if (biggerChild != null && (compare(i, biggerChild)) < 0) {
      swap(i, biggerChild);
      downheap(biggerChild, lastIdx);
    }
  }

  // Return the index of the bigger children
  // if there are no children, returns null
  // if there is no right child, returns the left one.
  Integer findBiggerChild(int leftIdx, int rightIdx, int lastIdx) {
    if (rightIdx > lastIdx && leftIdx > lastIdx) {
      return null;
    } else if (rightIdx > lastIdx) {
      return leftIdx;
    } else {
      if (compare(leftIdx, rightIdx) > 0) {
        return leftIdx;
      }
      return rightIdx;
    }
  }

  public boolean isEmpty() { return contents.isEmpty(); }
}

class MaxHeap<T> extends Heap<T> {
  public MaxHeap(ArrayList<Integer> contents) {
    super(new MaxComparator());
  }
}

class PriorityQueue<T> extends Heap<T> {
  public PriorityQueue() {
    super(new MinComparator());
  }
}

class MinComparator implements IComparator<Integer> {
  public int compare(Integer t1, Integer t2) {
    return t2 - t1;
  }
}

class MaxComparator implements IComparator<Integer> {
  public int compare(Integer t1, Integer t2) {
    return t1 - t2;
  }
}


class Vertex {
  IList<Edge> outEdges; // edges from this node
String name;
HeapNode<Vertex> reference;

  public Vertex(String name) {
    this.name = name;
    this.outEdges = new MtList<>();
  }

  public void addEdge(Edge edge) {
    this.outEdges = new ConsList<>(edge, this.outEdges);
  }

  public String toString() {
    return name;
  }
}

class Edge {
  Vertex from;
  Vertex to;
  int weight;

  public Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
    this.from.addEdge(this);
  }
}
class Graph {
  ArrayList<Vertex> allVertices;

  public Graph(ArrayList<Vertex> allVertices) {
    this.allVertices = allVertices;
  }

  IList<Edge> getEdges() {
    IList<Edge> edges = new MtList<>();
    for (Vertex v: allVertices) {
      for (Edge e: v.outEdges) {
        edges = new ConsList<>(e, edges);
      }
    }
    return edges;
  }

  // Returns the path from one vertex to another, or null if there is no such path
  Deque<Vertex> findCheapestPath(Vertex from, Vertex to) {
    Deque<Vertex> alreadySeen = new Deque<Vertex>();
    PriorityQueue<Vertex> worklist = new PriorityQueue<Vertex>();

    // Initialize the worklist with the from vertex
    worklist.add(from, 0);
    // As long as the worklist isn't empty...
    while (!worklist.isEmpty()) {
      int weightSoFar = worklist.contents.get(0).weight;
      Vertex next = worklist.remove();
      if (next.equals(to)) {
        alreadySeen.addAtTail(next);
        return alreadySeen;
      }
      else if (alreadySeen.contains(next)) {
        // do nothing: we've already seen this one
      }
      else {
        // add all the neighbors of next to the worklist for further processing
        for (Edge e : next.outEdges) {
          worklist.add(e.to, e.weight + weightSoFar);
        }
        // add next to alreadySeen, since we're done with it
        alreadySeen.addAtTail(next);
      }
    }
    // We haven't found the to vertex, and there are no more to try
    return null;
  }

  // Returns the path from one vertex to another, or null if there is no such path
  Deque<Vertex> findCheapestPathOptimized(Vertex from, Vertex to) {
    Deque<Vertex> alreadySeen = new Deque<Vertex>();
    PriorityQueue<Vertex> worklist = new PriorityQueue<Vertex>();

    // Initialize the worklist with the from vertex
    worklist.add(from, 0);
    // As long as the worklist isn't empty...
    while (!worklist.isEmpty()) {
      int weightSoFar = worklist.contents.get(0).weight;
      Vertex next = worklist.remove();
      if (next.equals(to)) {
        alreadySeen.addAtTail(next);
        return alreadySeen;
      }
      else if (alreadySeen.contains(next)) {
        // do nothing: we've already seen this one
      }
      else {
        // add all the neighbors of next to the worklist for further processing
        for (Edge e : next.outEdges) {
          int newWeight = e.weight + weightSoFar;
          if (e.to.reference == null) {
            e.to.reference = worklist.add(e.to, newWeight);
          } else if (e.to.reference.weight > newWeight) {
            e.to.reference.weight = newWeight;
            worklist.upheap(e.to.reference.index);
          }
        }
        // add next to alreadySeen, since we're done with it
        alreadySeen.addAtTail(next);
      }
    }
    // We haven't found the to vertex, and there are no more to try
    return null;
  }
}

class ExamplesCheapestPath {
  Vertex A, B, C, D, E, F;
  Graph g1, g2;

  Vertex zero, one, two, three, four, five, six, seven, eight;

  void setup() {
    A = new Vertex("A");
    B = new Vertex("B");
    C = new Vertex("C");
    D = new Vertex("D");
    E = new Vertex("E");
    F = new Vertex("F");

    new Edge(A, B, 1);
    new Edge(B, C, 1);
    new Edge(C, D, 1);
    new Edge(D, E, 1);
    new Edge(E, F, 1);
    new Edge(A, F, 3);
    g1 = new Graph(new ArrayList<>(Arrays.asList(A, B, C, D, E, F)));


    zero = new Vertex("0");
    one = new Vertex("1");
    two = new Vertex("2");
    three = new Vertex("3");
    four = new Vertex("4");
    five = new Vertex("5");
    six = new Vertex("6");
    seven = new Vertex("7");
    eight = new Vertex("8");

    undirectedEdge(zero, one, 4);
    undirectedEdge(one, two, 8);
    undirectedEdge(two, eight, 2);
    undirectedEdge(two, five, 4);
    undirectedEdge(two, three, 7);
    undirectedEdge(three, five, 14);
    undirectedEdge(three, four, 9);
    undirectedEdge(zero, seven, 8);
    undirectedEdge(seven, six, 1);
    undirectedEdge(seven, eight, 7);
    undirectedEdge(six, eight, 6);
    undirectedEdge(six, five, 2);
    undirectedEdge(five, four, 10);
    g2 = new Graph(new ArrayList<>(Arrays.asList(one, two, three, four, five, six, seven, eight)));
  }

  // We don't really test anything, since the actual path is dependent on the setup -
  // this is just a demonstration
  void testFindCheapestPath(Tester t) {
    setup();
    System.out.println("From A to F: " + formatDeque(g1.findCheapestPath(A, F)));
    System.out.println("From 0 to 4: " + formatDeque(g2.findCheapestPath(zero, four)));
    System.out.println("Optimized (should be the same path):");
    System.out.println("From A to F: " + formatDeque(g1.findCheapestPathOptimized(A, F)));
    System.out.println("From 0 to 4: " + formatDeque(g2.findCheapestPathOptimized(zero, four)));
  }

  void undirectedEdge(Vertex a, Vertex b, int weight) {
    new Edge(a, b, weight);
    new Edge(b, a, weight);
  }

  <T> String formatDeque(Deque<T> d) {
    String s = "";
    for (T t: d) {
      s += t.toString() + " ";
    }
    return s;
  }
}