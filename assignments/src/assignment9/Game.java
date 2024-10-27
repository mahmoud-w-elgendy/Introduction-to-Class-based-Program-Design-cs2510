package assignment9;

import java.util.*;

import tester.*;
import javalib.impworld.*;

import java.awt.Color;

import javalib.worldimages.*;

// Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin in the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = false;
  }

  void placeImage(WorldScene scene) {
    scene.placeImageXY(draw(), FloodItWorld.BORDER_SIZE * 2 + x * FloodItWorld.CELL_SIZE,
        FloodItWorld.BORDER_SIZE * 2 + y * FloodItWorld.CELL_SIZE);
  }

  WorldImage draw() {
    return new RectangleImage(FloodItWorld.CELL_SIZE, FloodItWorld.CELL_SIZE, "solid",
        color).movePinholeTo(new Posn(FloodItWorld.CELL_SIZE / 2, FloodItWorld.CELL_SIZE / 2));
  }
}

class FloodItWorld extends World {
  // Gameplay related consts
  static int BOARD_SIZE = 22;
  static ArrayList<Color> COLORS = new ArrayList<>(
      Arrays.asList(Color.RED, Color.BLUE, Color.WHITE, Color.GREEN, Color.MAGENTA, Color.YELLOW));
  static double TICK_RATE = 1.0 / 100.0;
  // A formula based on trial and error for a reasonable amount of turns. YMMV
  static int MAX_TURNS =
      (int) (((BOARD_SIZE * BOARD_SIZE) / 14.0) + (COLORS.size() * COLORS.size() / 10.0)) + 2;

  // Graphics related consts
  static int CELL_SIZE = 40;
  static int BORDER_SIZE = 40;
  static int SCREEN_SIZE = BOARD_SIZE * CELL_SIZE + BORDER_SIZE * 2;
  static int FONT_SIZE = 20;
  static int FONT_SIZE_BIG = 40;
  static Color FONT_COLOR = Color.white;

  // All the cells of the game
  ArrayList<Cell> board;

  Random rand;
  int current_turn;
  ArrayList<Cell> toFlood;
  HashSet<Cell> visitedFlooded;
  Color floodColor;

  public FloodItWorld() {
    this(new Random());
  }

  public FloodItWorld(Random rand) {
    this.rand = rand;
    setupGame();
  }

  void setupGame() {
    this.board = generateBoard();
    this.current_turn = 0;

    // At the start of the game, all cells adjacent to the topleft cell that are the same color
    // are flooded
    startFlood(board.get(0).color);
  }

  private ArrayList<Cell> generateBoard() {
    // populate board
    ArrayList<Cell> board = new ArrayList<>();
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        board.add(new Cell(col, row, COLORS.get(rand.nextInt(COLORS.size()))));
      }
    }

    // link row cells
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE - 1; col++) {
        Cell c1 = board.get(row * BOARD_SIZE + col);
        Cell c2 = board.get(row * BOARD_SIZE + col + 1);

        c1.right = c2;
        c2.left = c1;
      }
    }

    // link column cells
    for (int row = 0; row < BOARD_SIZE - 1; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        Cell c1 = board.get(row * BOARD_SIZE + col);
        Cell c2 = board.get((row + 1) * BOARD_SIZE + col);

        c1.bottom = c2;
        c2.top = c1;
      }
    }

    board.get(0).flooded = true; // Since the game starts from the first tile, it is always flooded
    return board;
  }

  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(SCREEN_SIZE, SCREEN_SIZE);
    // border
    scene.placeImageXY(new RectangleImage(SCREEN_SIZE, SCREEN_SIZE, "solid", Color.BLACK),
        SCREEN_SIZE / 2, SCREEN_SIZE / 2);

    for (Cell c : board) {
      c.placeImage(scene);
    }
    placeTurnsInfo(scene);
    return scene;
  }

  private void placeTurnsInfo(WorldScene scene) {
    WorldImage textInfo = new TextImage(String.format("Turn: %s of %s", current_turn, MAX_TURNS),
        FONT_SIZE, FONT_COLOR);
    textInfo.movePinholeTo(
        new Posn(-(int) textInfo.getWidth() / 2, -(int) textInfo.getHeight() / 2));
    scene.placeImageXY(textInfo, 3 * BORDER_SIZE, BORDER_SIZE / 2);
  }

  public void onTick() {
    if (!isFlooding()) { return; }

    floodNext();

    if (visitedFlooded.size() == BOARD_SIZE * BOARD_SIZE) {
      endOfWorld("You won!");
    }
  }

  // Attempt to flood the next cells, and if successful, add thier neighbors to attempt flood
  // them as well. Doing this incrementally like this creates a visual "waterfull" effect.
  void floodNext() {
    ArrayList<Cell> workList = new ArrayList<>();
    for (Cell c : toFlood) {

      if (c != null && !visitedFlooded.contains(c) && (c.flooded || c.color.equals(floodColor))) {
        visitedFlooded.add(c);
        c.flooded = true;
        c.color = floodColor;
        workList.add(c.top);
        workList.add(c.bottom);
        workList.add(c.left);
        workList.add(c.right);
      }
    }

    toFlood = workList;
  }

  public void onKeyEvent(String key) {
    // reset the game
    if (key.equals("r")) { setupGame(); }
  }

  public void onMousePressed(Posn mouse, String buttomName) {
    if (!buttomName.equals("LeftButton") || isFlooding()) { return; }
    Cell cellClicked = findCell(mouse);
    if (cellClicked == null || cellClicked.color.equals(board.get(0).color)) { return; }

    current_turn++;
    if (current_turn > MAX_TURNS) {
      this.endOfWorld("Game Over");
    }

    startFlood(cellClicked.color);
  }

  // returns the cell at the given position, or null if there is no cell at this position
  Cell findCell(Posn pos) {
    // Account for the border
    Posn gridPos = new Posn(pos.x - 40, pos.y - 40);
    // get logical position
    gridPos.x = gridPos.x / CELL_SIZE;
    gridPos.y = gridPos.y / CELL_SIZE;

    // out of bounds
    if (gridPos.x < 0 || gridPos.x > 21 || gridPos.y < 0 || gridPos.y > 21) {
      return null;
    }
    return board.get(gridPos.y * BOARD_SIZE + gridPos.x);
  }

  void startFlood(Color color) {
    toFlood = new ArrayList<>();
    toFlood.add(board.get(0));
    visitedFlooded = new HashSet<>(); // Could use any other datastructure, but this one is more
    // performant than the ones we learned about
    floodColor = color;
  }

  boolean isFlooding() {
    return !toFlood.isEmpty();
  }

  public void bigBang() {
    bigBang(SCREEN_SIZE, SCREEN_SIZE, TICK_RATE);
  }

  public WorldScene lastScene(String msg) {
    WorldImage msgText = new TextImage(msg, FONT_SIZE_BIG, FONT_COLOR);
    WorldScene scene = getEmptyScene();
    scene.placeImageXY(new RectangleImage(SCREEN_SIZE, SCREEN_SIZE, "solid", Color.BLACK),
        SCREEN_SIZE / 2, SCREEN_SIZE / 2);
    scene.placeImageXY(msgText, SCREEN_SIZE / 2, SCREEN_SIZE / 2);
    return scene;
  }
}

class ExamplesGame {
  Random rand;
  FloodItWorld g1;

  void setup() {
    rand = new Random(100);
    g1 = new FloodItWorld(rand);
    // Finish flooding of all starting cells
    // (cells adjacent to the topleft cell that have the same color)
    tickGame(g1, 7);
  }

  void testGame(Tester t) {
    setup();
    FloodItWorld g = new FloodItWorld();
    g.bigBang();
  }

  void testBoard(Tester t) {
    setup();

    t.checkExpect(g1.board.size(), FloodItWorld.BOARD_SIZE * FloodItWorld.BOARD_SIZE);

    // test links
    t.checkExpect(g1.board.get(0).top, null);
    t.checkExpect(g1.board.get(0).right, g1.board.get(1));
    t.checkExpect(g1.board.get(0).top, null);
    t.checkExpect(g1.board.get(0).bottom, g1.board.get(FloodItWorld.BOARD_SIZE));
    t.checkExpect(g1.board.get(FloodItWorld.BOARD_SIZE * 2 + 2).right,
        g1.board.get(FloodItWorld.BOARD_SIZE * 2 + 3));
  }

  void testFindCell(Tester t) {
    setup();
    t.checkExpect(g1.findCell(new Posn(0, 0)), null);
    t.checkExpect(g1.findCell(new Posn(FloodItWorld.SCREEN_SIZE, 0)), null);
    t.checkExpect(g1.findCell(new Posn(FloodItWorld.BORDER_SIZE, FloodItWorld.BORDER_SIZE)).color,
        Color.BLUE);
    t.checkExpect(g1.findCell(new Posn(FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE * 10,
        FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE * 15)).color, Color.GREEN);
  }

  void testOnMousePressed(Tester t) {
    setup();
    g1.onMousePressed(new Posn(0, 0), "LeftButton");
    t.checkExpect(g1.isFlooding(), false); // Don't flood on invalid input
    g1.onMousePressed(new Posn(FloodItWorld.BORDER_SIZE + 2 * FloodItWorld.CELL_SIZE,
        FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE), "LeftButton"); // Pressed green cell
    t.checkExpect(g1.isFlooding(), true);
    t.checkExpect(g1.floodColor, Color.GREEN);
    tickGame(g1, 4);
    g1.onMousePressed(new Posn(FloodItWorld.BORDER_SIZE + 2 * FloodItWorld.CELL_SIZE,
        FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE), "LeftButton"); // Pressed green cell
    t.checkExpect(g1.isFlooding(), false); // Don't flood if clicked on the same color

    g1.onMousePressed(
        new Posn(FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE, FloodItWorld.BORDER_SIZE),
        "LeftButton"); // Pressed magenta cell
    t.checkExpect(g1.isFlooding(), true);
    t.checkExpect(g1.floodColor, Color.MAGENTA);
    tickGame(g1, 4);

    // All flooded cells change color
    g1.onMousePressed(
        new Posn(FloodItWorld.BORDER_SIZE, FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE),
        "LeftButton"); // Pressed yellow celll
    t.checkExpect(g1.isFlooding(), true);
    t.checkExpect(g1.floodColor, Color.YELLOW);
  }

  void testOnTick(Tester t) {
    setup();
    g1.startFlood(Color.MAGENTA);
    g1.onTick();
    t.checkExpect(g1.board.get(0).color, Color.MAGENTA); // Color of first cell changed
    t.checkExpect(g1.board.get(1).flooded, false); // One tick doesn't flood neighbors
    g1.onTick();
    t.checkExpect(g1.board.get(1).flooded, true); // Next tick flood direct neighbor
    t.checkExpect(g1.board.get(2).flooded, false); // Doens't flood neighbor of neighbor
    g1.onTick();
    t.checkExpect(g1.board.get(1).right.flooded, true);
    t.checkExpect(g1.board.get(1).bottom.flooded, true);
    g1.onTick();
    t.checkExpect(g1.isFlooding(), false); // No more cells to flood, flood ends

    g1.startFlood(Color.yellow);
    tickGame(g1, 7);

    ArrayList<Cell> toCheck = new ArrayList<>(
        Arrays.asList(g1.board.get(0), g1.board.get(1), g1.board.get(2), g1.board.get(0).bottom,
            g1.board.get(1).bottom));
    for (Cell c : toCheck) {
      t.checkExpect(c.flooded, true);
      t.checkExpect(c.color, Color.YELLOW);
    }
    t.checkExpect(g1.isFlooding(), false);
    // Tiles adjacent to flooded cells did not change color
    t.checkExpect(g1.board.get(3).color, Color.RED);
    t.checkExpect(g1.board.get(0).bottom.bottom.color, Color.BLUE);
    t.checkExpect(g1.board.get(1).bottom.bottom.color, Color.WHITE);
    t.checkExpect(g1.board.get(2).bottom.color, Color.GREEN);

    g1.startFlood(Color.RED);
    tickGame(g1, 2);
    g1.onMousePressed(
        new Posn(FloodItWorld.BORDER_SIZE, FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE),
        "LeftButton");
  }

  void testTurns(Tester t) {
    setup();
    t.checkExpect(g1.current_turn, 0);
    // Clicking on something that is not a tile doesn't update turn
    g1.onMousePressed(new Posn(0, 0), "LeftButton");
    t.checkExpect(g1.current_turn, 0);
    // Clicking on a tile of a different color updates the turn
    g1.onMousePressed(new Posn(FloodItWorld.BORDER_SIZE + 2 * FloodItWorld.CELL_SIZE,
        FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE), "LeftButton");
    tickGame(g1, 7);
    t.checkExpect(g1.current_turn, 1);
    g1.onMousePressed(
        new Posn(FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE, FloodItWorld.BORDER_SIZE),
        "LeftButton");
    t.checkExpect(g1.current_turn, 2);
    tickGame(g1, 7);
    // Clicking on a tile of the same color doens't update the turn
    g1.onMousePressed(
        new Posn(FloodItWorld.BORDER_SIZE + FloodItWorld.CELL_SIZE, FloodItWorld.BORDER_SIZE),
        "LeftButton");
    t.checkExpect(g1.current_turn, 2);
  }

  void testBoardReset(Tester t) {
    setup();

    // Simulate some changes in the game state
    g1.startFlood(Color.RED);
    tickGame(g1, 5);

    g1.onKeyEvent("r"); // Reset the game

    t.checkExpect(g1.current_turn, 0); // Turn count reset
    t.checkFail(g1.board.get(2).color, Color.MAGENTA); // Colors should be different

    g1.onKeyEvent("q"); // Some random key other than 'r'
    t.checkExpect(g1.current_turn, 0); // Key press should not affect game state
  }

  void tickGame(FloodItWorld g, int times) {
    for (int i = 0; i < times; i++) {
      g.onTick();
    }
  }
}