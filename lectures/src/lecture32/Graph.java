package lecture32;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Vertex {
  ArrayList<Edge> outEdges;
  String name;

  public Vertex(String name) {
    this.name = name;
    this.outEdges = new ArrayList<>();
  }

  public void addEdge(Edge edge) { this.outEdges.add(edge); }

  public String toString() { return name; }
}

class Edge {
  Vertex from, to;
  int weight;

  public Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
    this.from.addEdge(this);
  }

  public String toString() {
    return this.from.toString() + this.to.toString();
  }
}
class Graph {
  ArrayList<Vertex> vertices;

  public Graph(ArrayList<Vertex> vertices) {
    this.vertices = vertices;
  }

  ArrayList<Edge> primsAlgorithm() {
    ArrayList<Edge> tree = new ArrayList<>();
    if (vertices.isEmpty()) { return tree; } // nothing to do; quit

    // the next edges to process - always picks the cheapest
    PriorityQueue<Edge> frontier = new PriorityQueue<>();
    // keep track of vertices already connected so that we won't connect the again
    HashMap<Vertex, Boolean> connected = new HashMap<>();
    for (Vertex v : vertices) { connected.put(v, false); } // initially, nothing is connected

    // choose and process a random node
    Vertex initialVertex = vertices.get(0);
    connected.put(initialVertex, true);
    for (Edge e : initialVertex.outEdges) {
      frontier.add(e, e.weight);
    }

    // starting from the edges of the node, process the rest
    while (!frontier.isEmpty()) {
      // pick the cheapest edge
      Edge e = frontier.remove();
      // discard edges that connects to an already connected node as it creates a cycle
      if (!connected.get(e.to)) {
        // process the edges of the next node
        tree.add(e);
        connected.put(e.to, true);
        for (Edge outEdge : e.to.outEdges) {
          frontier.add(outEdge, outEdge.weight);
        }
      }
    }
    return tree;
  }

  ArrayList<Edge> kruskalsAlgorithm() {
    // using a Union/Find data structure
    HashMap<String, String> representatives = new HashMap<>();

    ArrayList<Edge> edgesInTree = new ArrayList<>();
    ArrayList<Edge> worklist = getSortedEdges();

    if (worklist.isEmpty()) { return edgesInTree; }

    // initialize every node's representative to itself
    for (Vertex v: vertices) {
      representatives.put(v.name, v.name);
    }

    // alternatively: while (moreThanOneRepresentative(representatives))
    while (!worklist.isEmpty()) {
      Edge e = worklist.remove(0);
      String fromRepresentative = findRepresentative(representatives, e.from);
      String toRepresentative = findRepresentative(representatives, e.to);

      if (fromRepresentative.equals(toRepresentative)) {
        // Discard this edge - they're already connected
      } else {
        edgesInTree.add(e);
        // union the representatives
        representatives.put(toRepresentative, fromRepresentative);
      }
    }

    return edgesInTree;
  }

  ArrayList<Edge> getSortedEdges() {
    ArrayList<Edge> edges = new ArrayList<>();

    // use heapsort (although without the space optimization shown in the heapsort lesson)
    PriorityQueue<Edge> edgesQueue = new PriorityQueue<>();
    for (Vertex v: vertices) {
      for (Edge e: v.outEdges) {
          edgesQueue.add(e, e.weight);
      }
    }

    while (!edgesQueue.isEmpty()) {
      edges.add(edgesQueue.remove());
    }

    // Since we're dealing with undirected graph, we probably also want to remove duplicates...

    return edges;
  }

  String findRepresentative(HashMap<String, String> representatives, Vertex v) {
    String r = representatives.get(v.name);
    while (!r.equals(representatives.get(r))) {
      r = representatives.get(r);
    }
    return r;
  }

  boolean moreThanOneRepresentative(HashMap<String, String> representatives) {
    if (vertices.size() < 2) { return false; }

    // A representatives hashmap has one than one representative if there are at least 2
    // different representatives

    String r = findRepresentative(representatives, vertices.get(0));
    for (int i = 1; i < vertices.size(); i++) {
      if (!findRepresentative(representatives, vertices.get(i)).equals(r)) {
        return true;
      }
    }
    return false;
  }
}


class ExamplesMinimumSpanningTree {
  Vertex A, B, C, D, E, F;
  Graph g1;

  void setup() {
    A = new Vertex("A");
    B = new Vertex("B");
    C = new Vertex("C");
    D = new Vertex("D");
    E = new Vertex("E");
    F = new Vertex("F");

    undirectedEdge(A, B, 30);
    undirectedEdge(A, E, 50);
    undirectedEdge(B, C, 40);
    undirectedEdge(B, E, 35);
    undirectedEdge(B, F, 50);
    undirectedEdge(C, E, 15);
    undirectedEdge(C, D, 25);
    undirectedEdge(D, F, 50);
    g1 = new Graph(new ArrayList<>(Arrays.asList(A, B, C, D, E, F)));
  }

  void undirectedEdge(Vertex a, Vertex b, int weight) {
    new Edge(a, b, weight);
    new Edge(b, a, weight);
  }

  void testFindMinimumSpanningTree(Tester t) {
    setup();
    System.out.println("With Prim’s algorithm: " + g1.primsAlgorithm());
    System.out.println("With Kruskal’s algorithm: " + g1.kruskalsAlgorithm());
  }
}