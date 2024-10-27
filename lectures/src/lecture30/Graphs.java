package lecture30;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;

class Vertex {
  IList<Edge> outEdges; // edges from this node

  public Vertex() {
    this.outEdges = new MtList<>();
  }

  public void addEdge(Edge edge) {
    this.outEdges = new ConsList<>(edge, this.outEdges);
  }

  boolean hasPathToAcc(Vertex dest) {
    return hasPathToHelper(dest, new ArrayList<>());
  }

  boolean hasPathToHelper(Vertex dest, ArrayList<Vertex> visited) {
    for (Edge e : this.outEdges) {
      if (!(visited.contains(e.to))) {
        visited.add(e.to);
        if ((e.to == dest || e.to.hasPathToHelper(dest, visited))) {
          return true;
        }
      }
    }
    return false;
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
  IList<Vertex> allVertices;

  public Graph(IList<Vertex> allVertices) {
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

  IList<Vertex> inEdges(Vertex to) {
    IList<Vertex> reach = new MtList<>();
    for (Vertex v: allVertices) {
      if (v.outEdges.find(new EdgeReaches(to)) != null) {
        reach = new ConsList<>(v, reach);
      }
    }
    return reach;
  }

  Deque<Vertex> bfs(Vertex from, Vertex to) {
    return findPath(from, to, new Queue<Vertex>());
  }
  Deque<Vertex> dfs(Vertex from, Vertex to) {
    return findPath(from, to, new Stack<Vertex>());
  }
  // Returns the path from one vertex to another, or null if there is no such path
  Deque<Vertex> findPath(Vertex from, Vertex to, ICollection<Vertex> worklist) {
    Deque<Vertex> alreadySeen = new Deque<Vertex>();

    // Initialize the worklist with the from vertex
    worklist.add(from);
    // As long as the worklist isn't empty...
    while (!worklist.isEmpty()) {
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
          worklist.add(e.to);
        }
        // add next to alreadySeen, since we're done with it
        alreadySeen.addAtTail(next);
      }
    }
    // We haven't found the to vertex, and there are no more to try
    return null;
  }
}

class EdgeReaches implements IPred<Edge> {
  Vertex to;

  public EdgeReaches(Vertex to) {
    this.to = to;
  }

  public boolean apply(Edge edge) {
    return edge.to.equals(to);
  }
}


class ExamplesGraph {
  Vertex v0, v1, v2, v3, v4, A, B, C, D, E;
  Edge e1to2, e1to3, e1to4, e2to3, e2to4, e3to4;
  Graph g0, g1, g2;

  void setup() {
    v0 = new Vertex();
    v1 = new Vertex();
    v2 = new Vertex();
    v3 = new Vertex();
    v4 = new Vertex();

    e1to2 = new Edge(v1, v2, 12);
    e1to3 = new Edge(v1, v3, 13);
    e1to4 = new Edge(v1, v4, 14);
    e2to3 = new Edge(v2, v3, 23);
    e2to4 = new Edge(v2, v4, 24);
    e3to4 = new Edge(v3, v4, 34);

    A = new Vertex();
    B = new Vertex();
    C = new Vertex();
    D = new Vertex();
    E = new Vertex();
    new Edge(A, B, 12);
    new Edge(B, C, 23);
    new Edge(C, A, 31);
    new Edge(B, D, 24);
    new Edge(E, B, 52);

    g0 = new Graph(new ConsList<>(v0, new MtList<>()));
    g1 = new Graph(new ConsList<>(v0, new ConsList<>(v1, new ConsList<>(v2,
        new ConsList<>(v3, new ConsList<>(v4, new MtList<>()))))));
    g2 = new Graph(new ConsList<>(A, new ConsList<>(B, new ConsList<>(C,
        new ConsList<>(D, new ConsList<>(E, new MtList<>()))))));
  }

  void testGetEdges(Tester t) {
    setup();

    t.checkExpect(g0.getEdges(), new MtList<>());
    t.checkExpect(g1.getEdges(),
        new Utils().listFromArrayList(new ArrayList<>(Arrays.asList(e3to4, e2to3, e2to4, e1to2,
            e1to3, e1to4))));
  }

  void testInEdges(Tester t) {
    setup();
    t.checkExpect(g0.inEdges(v1), new MtList<>());
    t.checkExpect(g1.inEdges(v0), new MtList<>());
    t.checkExpect(g1.inEdges(v1), new MtList<>());
    t.checkExpect(g1.inEdges(v2), new ConsList<>(v1, new MtList<>()));
    t.checkExpect(g1.inEdges(v3), new ConsList<>(v2, new ConsList<Vertex>(v1, new MtList<>())));
    t.checkExpect(g1.inEdges(v4), new ConsList<>(v3, new ConsList<Vertex>(v2, new ConsList<>(v1,
     new MtList<>()))));
  }

  void testHasPathToAcc(Tester t) {
    setup();
    t.checkExpect(A.hasPathToAcc(B), true);
    t.checkExpect(A.hasPathToAcc(C), true);
    t.checkExpect(A.hasPathToAcc(D), true);
    t.checkExpect(A.hasPathToAcc(E), false);
    t.checkExpect(E.hasPathToAcc(A), true);
    t.checkExpect(D.hasPathToAcc(C), false);
    t.checkExpect(B.hasPathToAcc(B), true);
  }

  void testFindPath(Tester t) {
    setup();
    t.checkExpect(g2.dfs(A, A), new Deque<>(new ArrayList<>(Arrays.asList(A))));
    t.checkExpect(g2.dfs(A, C), new Deque<>(new ArrayList<>(Arrays.asList(A, B, C))));
    t.checkExpect(g2.dfs(B, D), new Deque<>(new ArrayList<>(Arrays.asList(B, C, A, D))));
    t.checkExpect(g2.bfs(B, D), new Deque<>(new ArrayList<>(Arrays.asList(B, D))));
    t.checkExpect(g2.bfs(B, E), null);
  }
}
