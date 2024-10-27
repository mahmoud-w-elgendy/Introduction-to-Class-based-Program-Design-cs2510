package lab9;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;

// Represents an individual tile
class Tile {
  // The number on the tile.  Use 0 to represent the hole
  int value;

  public Tile(int value) {
    this.value = value;
  }

  // Draws this tile onto the background at the specified logical coordinates
  WorldImage drawAt(int col, int row, WorldImage background) {
    if (value == 0) { return background; }
    int xPos = col * Consts.TILE_SIZE + Consts.TILE_SIZE / 2;
    int yPos = row * Consts.TILE_SIZE + Consts.TILE_SIZE / 2;
    return new OverlayImage(tileImage().movePinholeTo(new Posn(-xPos, -yPos)), background);
  }

  WorldImage tileImage() {
    WorldImage text = new TextImage(String.valueOf(value), Consts.FONT_SIZE, Consts.FONT_COLOR);
    return new OverlayImage(text,
        new RectangleImage(Consts.TILE_SIZE, Consts.TILE_SIZE, OutlineMode.OUTLINE, Color.BLACK));
  }

  public boolean isHole() { return value == 0; }
}

class FifteenGame extends World {
  // represents the rows of tiles
  ArrayList<ArrayList<Tile>> tiles;
  int width, height;
  Random rand;
  ArrayList<String> moves;

  FifteenGame() {
    this.width = Consts.SCREEN_SIZE;
    this.height = Consts.SCREEN_SIZE;
    this.moves = new ArrayList<>();
    this.rand = new Random();
    this.tiles = createTiles();
  }

  FifteenGame(ArrayList<ArrayList<Tile>> tiles) {
    this();
    this.tiles = tiles;
  }

  // Create a random arrangement of tiles
  private ArrayList<ArrayList<Tile>> createTiles() {
    ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
    Utils u = new Utils();
    ArrayList<Integer> tileValues = u.makeIntList(0, 15);
    for (int row = 0; row < 4; row++) {
      ArrayList<Tile> rowTiles = new ArrayList<>();
      for (int col = 0; col < 4; col++) {
        rowTiles.add(new Tile(u.takeRandom(tileValues, rand)));
      }
      tiles.add(rowTiles);
    }
    return tiles;
  }

  // draws the game
  public WorldScene makeScene() {
    WorldImage img = new EmptyImage();
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        img = tiles.get(row).get(col).drawAt(col, row, img);
      }
    }
    WorldScene scene = new WorldScene(width, height);
    scene.placeImageXY(img, 0, 0);
    return scene;
  }

  // handles keystrokes
  public void onKeyEvent(String k) {
    if (move(k)) {
      moves.add(0, k);
    }
    else if (k.equals("u"))
      if (!moves.isEmpty()) {
        undoMove(moves.remove(0));
      }
    if (isGameOver()) {
      this.endOfWorld("Game is Over");
    }
  }

  // Given a key, attempt to move in the direction specified
  // Returns true if the move is successful, false otherwise.
  // A move moves the tile to the other side of the hole, swapping it with the hole
  // For example, moving down means that the tile up from the hole moves down,
  // therefore the hole moves up.
  boolean move(String direction) {
    switch (direction) {
    case "up":
      return swapWithHole(1, 0);
    case "down":
      return swapWithHole(-1, 0);
    case "left":
      return swapWithHole(0, 1);
    case "right":
      return swapWithHole(0, -1);
    }
    // Not a valid direction
    return false;
  }

  // undo a move by moving in the opposite direction
  void undoMove(String direction) {
    switch (direction) {
    case "up":
      move("down");
      break;
    case "down":
      move("up");
      break;
    case "left":
      move("right");
      break;
    case "right":
      move("left");
      break;
    }
  }

  // Swap a tile adjacent to the hole tile with the hole tile
  // The adjacent tile is specified by position in respect to the hole
  // for example to swap with the tile down from the hole,
  // the method should be called with: row = -1, col - 0;
  boolean swapWithHole(int row, int col) {
    Posn hole = findHole();
    row = hole.y + row;
    col = hole.x + col;
    // Cannot swap edges
    if (row < 0 || row >= 4 || col < 0 || col >= 4) {
      return false;
    }

    swapTiles(hole.y, hole.x, row, col);
    return true;
  }

  // Find the position of the hole
  private Posn findHole() {
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        if (tiles.get(row).get(col).isHole()) {
          return new Posn(col, row);
        }
      }
    }

    throw new RuntimeException("Cannot find hole");
  }

  // Swap two tiles by their indices
  public void swapTiles(int rowIndex1, int colIndex1, int rowIndex2, int colIndex2) {
    ArrayList<Tile> row1 = tiles.get(rowIndex1);
    ArrayList<Tile> row2 = tiles.get(rowIndex2);

    Tile t1 = row1.get(colIndex1);
    row1.set(colIndex1, row2.get(colIndex2));
    row2.set(colIndex2, t1);
  }

  public WorldScene lastScene(String msg) {
    WorldImage gameOverText = new TextImage(msg, Consts.FONT_SIZE_BIG, Consts.FONT_COLOR);
    WorldScene scene = new WorldScene(width, height);
    scene.placeImageXY(gameOverText, width / 2, height / 2);
    return scene;
  }

  boolean isGameOver() {
    int prevValue = 0;
    for (ArrayList<Tile> row : tiles) {
      for (Tile t : row) {
        if (t.value != prevValue + 1 && prevValue != 15) {
          return false;
        }
        prevValue += 1;
      }
    }
    return true;
  }

  public void bigBang() {
    super.bigBang(width, height);
  }
}

class ExampleFifteenGame {
  Random rand;
  FifteenGame g1;
  FifteenGame g2;

  void setup() {
    rand = new Random();
    ArrayList<ArrayList<Tile>> tiles1 = createTilesFromList(
        new ArrayList<>(Arrays.asList(1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)));
    ArrayList<ArrayList<Tile>> tiles2 = createTilesFromList(
        new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15)));
    g1 = new FifteenGame(tiles1);
    g2 = new FifteenGame(tiles2);
  }

  void testGame(Tester t) {
    setup();
    FifteenGame g = new FifteenGame();
    g.bigBang();
  }

  void testSwapTiles(Tester t) {
    setup();

    // Swap on same row
    t.checkExpect(g1.tiles.get(3).get(3), new Tile(15));
    t.checkExpect(g1.tiles.get(3).get(2), new Tile(14));
    g1.swapTiles(3, 3, 3, 2);
    t.checkExpect(g1.tiles.get(3).get(3), new Tile(14));
    t.checkExpect(g1.tiles.get(3).get(2), new Tile(15));

    // Swap on different rows
    t.checkExpect(g1.tiles.get(1).get(1), new Tile(5));
    t.checkExpect(g1.tiles.get(2).get(2), new Tile(10));
    g1.swapTiles(1, 1, 2, 2);
    t.checkExpect(g1.tiles.get(1).get(1), new Tile(10));
    t.checkExpect(g1.tiles.get(2).get(2), new Tile(5));
  }

  void testOnKeyEvent(Tester t) {
    setup();
    ArrayList<ArrayList<Tile>> tilesCopy = new ArrayList<>(g1.tiles);

    // Nothing on invalid key
    t.checkExpect(g1.tiles, tilesCopy);
    g1.onKeyEvent("g");
    g1.onKeyEvent(" ");
    t.checkExpect(g1.tiles, tilesCopy);

    // Nothing on invalid direction key
    g1.onKeyEvent("down");
    t.checkExpect(g1.tiles, tilesCopy);

    // Test valid moves
    g1.onKeyEvent("left");
    t.checkExpect(g1.tiles.get(0),
        new ArrayList<>(Arrays.asList(new Tile(1), new Tile(2), new Tile(0), new Tile(3))));
    g1.onKeyEvent("right");
    t.checkExpect(g1.tiles, tilesCopy);
    g1.onKeyEvent("up");
    g1.onKeyEvent("up");
    t.checkExpect(g1.tiles.get(2),
        new ArrayList<>(Arrays.asList(new Tile(8), new Tile(0), new Tile(10), new Tile(11))));
  }

  void testUndoMove(Tester t) {
    setup();
    ArrayList<ArrayList<Tile>> tilesCopy = new ArrayList<>(g1.tiles);

    // Nothing at the beginning
    g1.onKeyEvent("u");
    t.checkExpect(g1.tiles, tilesCopy);

    // After making an invalid move
    g1.onKeyEvent("down");
    g1.onKeyEvent("u");
    t.checkExpect(g1.tiles, tilesCopy);

    // After making valid moves
    g1.onKeyEvent("up");
    g1.onKeyEvent("left");
    g1.onKeyEvent("down");
    g1.onKeyEvent("u");
    g1.onKeyEvent("u");
    t.checkExpect(g1.tiles.get(1),
        new ArrayList<>(Arrays.asList(new Tile(4), new Tile(0), new Tile(6), new Tile(7))));

    // A few more valid moves & invalid moves
    g1.onKeyEvent("up");
    g1.onKeyEvent("up");
    g1.onKeyEvent("up");
    g1.onKeyEvent("left");
    g1.onKeyEvent("left");
    g1.onKeyEvent("left");
    g1.onKeyEvent("u");
    g1.onKeyEvent("u");
    g1.onKeyEvent("u");
    t.checkExpect(g1.tiles.get(2),
        new ArrayList<>(Arrays.asList(new Tile(8), new Tile(0), new Tile(10), new Tile(11))));

    // Back to the beginning
    g1.onKeyEvent("u");
    g1.onKeyEvent("u");
    t.checkExpect(g1.tiles, tilesCopy);
  }

  void testEndOfWorld(Tester t) {
    setup();
    t.checkExpect(g2.isGameOver(), false);
    g2.onKeyEvent("left");
    t.checkExpect(g2.isGameOver(), true);
  }

  private ArrayList<ArrayList<Tile>> createTilesFromList(ArrayList<Integer> positions) {
    ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
    for (int row = 0; row < 4; row++) {
      ArrayList<Tile> rowTiles = new ArrayList<>();
      for (int col = 0; col < 4; col++) {
        rowTiles.add(new Tile(positions.get(row * 4 + col)));
      }
      tiles.add(rowTiles);
    }
    return tiles;
  }
}