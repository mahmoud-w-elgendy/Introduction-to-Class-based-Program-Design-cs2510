package assignment10;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
import java.util.*;

class CellState {
  static int UNVISITED = 0; // the cell has not been visited
  static int VISITED = 1;  // the cell has been visited during the search
  static int IN_PATH = 2; // the cell is in the final path
}

class Cell {
  int pos;
  int row;
  int col;
  int state;

  Cell right;
  Cell down;
  Cell left;
  Cell up;

  public Cell(int pos, int row, int col) {
    this.pos = pos;
    this.row = row;
    this.col = col;
    this.state = CellState.UNVISITED;
  }

  // connect this to a neighbor cell
  void connect(Cell other) {
    if (this.row == other.row) {
      if (this.col - other.col == -1) {
        this.right = other;
      }
      else if (this.col - other.col == 1) {
        this.left = other;
      }
    }
    else if (this.col == other.col) {
      if (this.row - other.row == -1) {
        this.down = other;
      }
      else if (this.row - other.row == 1) {
        this.up = other;
      }
    }
  }

  WorldImage draw(int width, int height) {
    WorldImage rect = new RectangleImage(width, height, "solid", getColor());
    rect = rect.movePinholeTo(new Posn(-width / 2, -height / 2));
    return rect;
  }

  Color getColor() {
    if (state < 0 || state > 2) {
      throw new RuntimeException("cell state is invalid");
    }
    return MazeWorld.CELL_COLORS.get(state);
  }
}

// represents an (unordered) passage between 2 cells
class Passage {
  int c1;
  int c2;

  public Passage(int c1, int c2) {
    this.c1 = c1;
    this.c2 = c2;
  }

  public boolean equals(Object other) {
    if (!(other instanceof Passage)) { return false; }
    Passage that = (Passage) other;
    return (this.c1 == that.c1 && this.c2 == that.c2) || (this.c1 == that.c2 && this.c2 == that.c1);
  }

  public int hashCode() { return Math.min(c1, c2) * 1000 + Math.max(c1, c2); }
}

class MazeBuilder {
  int rows;
  int cols;
  Random rand;
  HashMap<Integer, Integer> representatives;
  ArrayList<Cell> cells;
  Iterator<Passage> worklist;
  int edges;
  int cellsNum;

  MazeBuilder(int rows, int cols, ArrayList<Cell> cells, Random rand) {
    this.rows = rows;
    this.cols = cols;
    this.rand = rand;
    this.representatives = new HashMap<>();
    this.cells = cells;
    setupMaze();
  }

  void setupMaze() {
    cells.clear();
    // create cells & representatives
    for (int i = 0; i < rows * cols; i++) {
      cells.add(new Cell(i, i / cols, i % cols));
      representatives.put(i, i); // Set every cell to be its own representative
    }

    // The maze should be generated randomally, so the order of the passages to be removed is random
    worklist = ArrayUtils.shuffle(getPassages(), rand).iterator();

    cellsNum = cells.size();
    edges = 0;
    this.clear();
  }

  // clear the maze by resetting the states of the cells; doesn't generate a new maze
  void clear() {
    for (int i = 1; i < cells.size() - 1; i++) {
      cells.get(i).state = CellState.UNVISITED;
    }
    cells.get(0).state = CellState.VISITED;
    cells.get(cells.size() - 1).state = CellState.IN_PATH;
  }

  void buildNext() {
    // for 𝑛 cells, we need 𝑛−1 edges to connect them all.
    if (hasNext()) {
      Passage p = worklist.next();
      Integer r1 = findRepresentative(representatives, p.c1);
      Integer r2 = findRepresentative(representatives, p.c2);

      if (!r1.equals(r2)) {
        // connect cells
        Cell c1 = cells.get(p.c1);
        Cell c2 = cells.get(p.c2);
        c1.connect(c2);
        c2.connect(c1);
        // Union the representatives
        representatives.put(r2, r1);

        edges++;
      }
    }
  }

  void buildAll() {
    while (hasNext()) {
      buildNext();
    }
  }

  boolean hasNext() {
    return edges < cellsNum - 1;
  }

  // get all possible passages by generating a passage between every 2 adjacent cells
  ArrayList<Passage> getPassages() {
    ArrayList<Passage> passages = new ArrayList<>();

    // get vertical passages
    for (int i = 0; i < rows - 1; i++) {
      for (int j = 0; j < cols; j++) {
        int pos1 = i * cols + j;
        int pos2 = (i + 1) * cols + j;
        passages.add(new Passage(pos1, pos2));
      }
    }
    // get horizontal passages
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols - 1; j++) {
        int pos1 = i * cols + j;
        int pos2 = i * cols + j + 1;
        passages.add(new Passage(pos1, pos2));
      }
    }

    return passages;
  }

  Integer findRepresentative(HashMap<Integer, Integer> representatives, int cellPos) {
    int r = representatives.get(cellPos);
    while (r != (representatives.get(r))) {
      r = representatives.get(r);
    }
    return r;
  }
}

class MazeSolver {
  ArrayList<Cell> cells;
  ICollection<Cell> worklist;
  Cell startCell;
  Cell endCell;
  Deque<Cell> visited;
  boolean endFound;
  String algorithm;

  MazeSolver(ArrayList<Cell> cells, String algorithm, Cell startCell, Cell endCell) {
    this.cells = cells;
    this.visited = new Deque<>();
    this.startCell = startCell;
    this.endCell = endCell;
    this.algorithm = algorithm;
    this.endFound = false;

    if (algorithm.equalsIgnoreCase("bfs")) {
      worklist = new Queue<>();
    }
    else if (algorithm.equalsIgnoreCase("dfs")) {
      worklist = new Stack<>();
    }
    else {
      throw new IllegalArgumentException("Enter 'bfs' or 'dfs'");
    }

    // Initialize the worklist with the first cell
    worklist.add(startCell);
  }

  // We generally want the maze to be solved from top-left to bottom-right
  MazeSolver(ArrayList<Cell> cells, String algorithm) {
    this(cells, algorithm, cells.get(0), cells.get(cells.size() - 1));
  }

  void next() {
    if (!endFound && worklist.isEmpty()) { throw new RuntimeException("Couldn't solve the maze"); }

    if (!endFound) {
      Cell next = searchNext();
      if (next != null) {
        next.state = CellState.VISITED;
      }
    }
    else {
      Cell next = backtrackNext();
      if (next != null) {
        next.state = CellState.IN_PATH;
      }
    }
  }

  Cell searchNext() {
    // keep looking for the next cell if we're at a reduntant one. no recursion for efficiency.
    while (true) {
      if (worklist.isEmpty()) { return null; }

      Cell next = worklist.remove();
      if (next.equals(endCell)) {
        endFound = true;
        return null;
      }
      if (!visited.contains(next)) {
        // add all the neighbors of next to the worklist for further processing
        // techincally, we didn't learn about arrays, but you can just use an ArrayList instead.
        // Arrays are just more efficient.
        for (Cell c : new Cell[] { next.left, next.up, next.right, next.down }) {
          if (c != null) {
            worklist.add(c);
          }
        }
        // add next to alreadySeen, since we're done with it
        visited.addAtTail(next);
        return next;
      }
    }
  }

  void searchAll() {
    while (!endFound && !worklist.isEmpty()) {
      searchNext();
    }
  }

  Cell backtrackNext() {
    // keep looking for the next cell if we're at a reduntant one. no recursion for efficiency.
    while (true) {
      if (visited.isEmpty()) { return null; }

      // A cell is redundant if the maze can be solved without it.
      // therefore, we attempt to solve the maze without it, and if we succeed we can discard it.
      Cell c = visited.removeFromTail();
      MazeSolver solveWithoutC = new MazeSolver(cells, algorithm);
      solveWithoutC.visited.addAtTail(c);
      solveWithoutC.searchAll();

      if (!solveWithoutC.endFound) {
        return c;
      }
    }
  }
}

class MazeWorld extends World {
  static int WIDTH = 800;
  static int HEIGHT = 600;
  double TICK_RATE = 1.0 / 60.0;

  static Color unvisitedCellColor = Color.GRAY;
  static Color startColor = new Color(37, 150, 190);
  static Color endColor = new Color(32, 128, 70);
  static ArrayList<Color> CELL_COLORS = new ArrayList<>(
      Arrays.asList(unvisitedCellColor, startColor, endColor));

  WorldImage downWall, rightWall;
  MazeBuilder maze;
  MazeSolver solver;
  ArrayList<Cell> cells;
  int rows, cols, cellHeight, cellWidth;
  Random rand;
  boolean showConstruction;

  public MazeWorld(int rows, int cols, Random rand, boolean showConstruction) {
    this.rows = rows;
    this.cols = cols;
    this.cellWidth = WIDTH / cols;
    this.cellHeight = HEIGHT / rows;
    this.rand = rand;
    this.showConstruction = showConstruction;

    this.cells = new ArrayList<>();
    this.maze = new MazeBuilder(rows, cols, cells, rand);
    if (!showConstruction) {
      maze.buildAll();
    }

    // no need to re-create those every time
    this.downWall = new RectangleImage(cellWidth, 2, "solid", Color.BLACK).movePinholeTo(
        new Posn(-cellWidth / 2, 0));
    this.rightWall = new RectangleImage(2, cellHeight, "solid", Color.BLACK).movePinholeTo(
        new Posn(0, -cellHeight / 2));
  }

  public MazeWorld(int rows, int cols, boolean showConstruction) {
    this(rows, cols, new Random(), showConstruction);
  }

  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();

    // draw cells
    for (Cell c : cells) {
      scene.placeImageXY(c.draw(cellWidth, cellHeight), c.col * cellWidth, c.row * cellHeight);
    }

    // draw walls
    for (Cell c : cells) {
      if (c.down == null) {
        scene.placeImageXY(downWall, c.col * cellWidth, (c.row + 1) * cellHeight);
      }
      if (c.right == null) {
        scene.placeImageXY(rightWall, (c.col + 1) * cellWidth, c.row * cellHeight);
      }
    }
    return scene;
  }

  public void onTick() {
    if (maze.hasNext()) {
      maze.buildNext();
    }
    else if (solver != null) {
      solver.next();
    }
  }

  public void onKeyEvent(String key) {
    // reset the maze, genrerating a new one
    if (key.equals("r")) {
      maze.setupMaze();
      if (!showConstruction) { maze.buildAll(); }
      solver = null;
    }
    // clear the maze
    else if (key.equals("c")) {
      maze.clear();
      solver = null;
    }
    // BFS
    else if (key.equals("b") && !maze.hasNext() && solver == null) {
      solver = new MazeSolver(cells, "bfs");
    }
    // DFS
    else if (key.equals("d") && !maze.hasNext() && solver == null) {
      solver = new MazeSolver(cells, "dfs");
    }
  }

  public void bigBang() {
    super.bigBang(WIDTH, HEIGHT, TICK_RATE);
  }
}

class ExamplesMaze {
  MazeWorld smallMazeWorld, mediumMazeWorld, bigMazeWorld;

  void setup() {
    smallMazeWorld = new MazeWorld(10, 10, new Random(100), false);
    mediumMazeWorld = new MazeWorld(20, 40, new Random(100), false);
    bigMazeWorld = new MazeWorld(60, 100, new Random(100), false);
  }

  void testMaze(Tester t) {
    setup();
    MazeWorld m = new MazeWorld(20, 20, true);
    m.bigBang();
  }

  void testGetPasses(Tester t) {
    setup();

    for (MazeWorld m : new ArrayList<>(
        Arrays.asList(smallMazeWorld, mediumMazeWorld, bigMazeWorld))) {
      // for every row, there's a right pass between every 2 cols, execpt the last one,
      // and for every col, there's a down pass between every 2 rows, except the last one
      t.checkExpect(m.maze.getPassages().size(), m.rows * (m.cols - 1) + m.cols * (m.rows - 1));

      boolean neighborsOnly = isNeighborsOnly(m);
      t.checkExpect(neighborsOnly, true);
    }
  }

  // a helper methods for checking that only neighbors have passes
  boolean isNeighborsOnly(MazeWorld m) {
    boolean neighborsOnly = true;
    for (Passage p : m.maze.getPassages()) {
      boolean horizontalNeighbors = (m.cells.get(p.c1).row == m.cells.get(p.c2).row
          && Math.abs(m.cells.get(p.c1).col - m.cells.get(p.c2).col) == 1);
      boolean verticalNeighbors = (m.cells.get(p.c1).col == m.cells.get(p.c2).col
          && Math.abs(m.cells.get(p.c1).row - m.cells.get(p.c2).row) == 1);
      neighborsOnly = neighborsOnly && (horizontalNeighbors || verticalNeighbors);
    }
    return neighborsOnly;
  }

  void testCells(Tester t) {
    setup();

    for (MazeWorld m : new ArrayList<>(Arrays.asList(smallMazeWorld, mediumMazeWorld))) {
      // test size
      t.checkExpect(m.cells.size(), m.rows * m.cols);

      // test that the links in each cell are valid
      boolean verticalValid = true;
      int links = 0;

      // test vertical links
      for (int i = 0; i < m.rows - 1; i++) {
        for (int j = 0; j < m.cols; j++) {
          Cell c1 = m.cells.get(i * m.cols + j);
          Cell c2 = m.cells.get((i + 1) * m.cols + j);
          verticalValid = verticalValid && (c1.down == c2 || c1.down == null);
          if (c1.down != null) { links++; }
        }
      }
      // test horizontal links
      boolean horizontalValid = true;
      for (int i = 0; i < m.rows; i++) {
        for (int j = 0; j < m.cols - 1; j++) {
          Cell c1 = m.cells.get(i * m.cols + j);
          Cell c2 = m.cells.get(i * m.cols + j + 1);
          horizontalValid = horizontalValid && (c1.right == c2 || c1.right == null);
          if (c1.right != null) { links++; }
        }
      }

      // also tests that the tree is valid - there are no cycles
      t.checkExpect(verticalValid, true);
      t.checkExpect(horizontalValid, true);

      // Test that the tree is minimal - it has exactly n-1 edges
      t.checkExpect(links, m.cells.size() - 1);

      // test that the tree is spanning
      // if every cell can be reached by a certain cell, then all cells are linked via that cell,
      // and therefore the tree is spanning
      boolean spanning = true;
      for (Cell c : m.cells) {
        MazeSolver solver = new MazeSolver(m.cells, "dfs", m.cells.get(0), c);
        solver.searchAll();
        spanning = spanning & solver.endFound;
        m.maze.clear();
      }
      t.checkExpect(spanning, true);
    }
    // if the tests above pass, we know that the maze is a minimum spanning tree.
  }

  void testSolver(Tester t) {
    setup();
    MazeWorld m = smallMazeWorld;
    m.onKeyEvent("d");
    // complete the search
    while (!m.solver.endFound) {
      m.solver.next();
    }
    m.solver.next();
    t.checkExpect(m.cells.get(99).left.state, CellState.IN_PATH);
    t.checkExpect(m.cells.get(99).left.up.state, CellState.VISITED);

    // complete the backtracking
    while (!m.solver.visited.isEmpty()) {
      m.solver.next();
    }

    // check that all cells of the final path were marked so, and that no other cells were
    ArrayList<Cell> path = findPath(m.cells.get(99), "luuluuuldldlluluuuluul");
    boolean onPath = true;
    boolean notOnPath = true;
    for (Cell c : m.cells) {
      if (path.contains(c)) {
        onPath = onPath && (c.state == CellState.IN_PATH);
      }
      else {
        notOnPath = notOnPath && (c.state != CellState.IN_PATH);
      }
    }
    t.checkExpect(onPath, true);
    t.checkExpect(notOnPath, true);
  }

  ArrayList<Cell> findPath(Cell start, String directions) {
    Cell next = start;
    ArrayList<Cell> path = new ArrayList<>();
    path.add(next);
    for (String d : directions.split("")) {
      if (next == null) { return path; }
      switch (d) {
      case "l":
        next = next.left;
        break;
      case "u":
        next = next.up;
        break;
      case "r":
        next = next.right;
        break;
      case "d":
        next = next.down;
        break;
      }
      path.add(next);
    }
    return path;
  }

  void testReset(Tester t) {
    setup();
    MazeWorld m = mediumMazeWorld;
    m.onKeyEvent("b");
    // complete the search
    while (!m.solver.endFound) {
      m.solver.next();
    }
    // complete the backtracking
    while (!m.solver.visited.isEmpty()) {
      m.solver.next();
    }
    m.onKeyEvent("r");

    boolean allClear = true;
    for (int i = 1; i < m.cells.size() - 2; i++) {
      Cell c = m.cells.get(i);
      allClear = allClear && c.state == CellState.UNVISITED;
    }

    t.checkExpect(allClear, true);
    t.checkExpect(m.cells.get(0).state, CellState.VISITED);
    t.checkExpect(m.cells.get(m.cells.size() - 1).state, CellState.IN_PATH);
  }
}
