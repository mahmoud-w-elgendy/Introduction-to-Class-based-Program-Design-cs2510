package assignment3.problem2;

import javalib.funworld.World;
import javalib.funworld.WorldScene;
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages

import java.awt.Color;

interface ITree {
  WorldImage draw();
  boolean isDrooping();
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree);
  ITree adjust(double theta);
  double getWidth();
}

class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  public Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  public WorldImage draw() {
    return new CircleImage(size, "solid", color);
  }

  public boolean isDrooping() {
    return false;
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this,
        otherTree.adjust(rightTheta));
  }

  public ITree adjust(double theta) {
    return this;
  }

  public double getWidth() {
    return size * 2;
  }
}

class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  public Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta % 360;
    this.tree = tree;
  }

  public WorldImage draw() {
    int x = (int) (length * Math.cos(Math.toRadians(theta)));
    int y = (int) (length * Math.sin(Math.toRadians(theta)));
    WorldImage line = new LineImage(new Posn(x, -y), Color.BLACK).movePinhole(x / 2.0, -y / 2.0);
    return new OverlayImage(tree.draw(), line).movePinhole(-x, y);
  }

  public boolean isDrooping() {
    return angleIsDownward() || tree.isDrooping();
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.adjust(leftTheta),
        otherTree.adjust(rightTheta));
  }

  public ITree adjust(double theta) {
    return new Stem(length, this.theta + theta, tree.adjust(theta));
  }

  public double getWidth() {
    // Not sure if this is cheating XD
    return draw().getWidth();
  }

  private boolean angleIsDownward() {
    double t = theta;
    return t > 180 || (t < 0 && t > -180);
  }
}

class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  public Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left,
      ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta % 360;
    this.rightTheta = rightTheta % 360;
    this.left = left;
    this.right = right;
  }

  public WorldImage draw() {
    return new OverlayImage(new Stem(leftLength, leftTheta, left).draw(),
        new Stem(rightLength, rightTheta, right).draw());
  }

  public boolean isDrooping() {
    ITree leftImage = new Stem(leftLength, leftTheta, left);
    ITree rightImage = new Stem(rightLength, rightTheta, right);
    return leftImage.isDrooping() || rightImage.isDrooping();
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    // substract 90 to align upward from the source of the new banch
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.adjust(leftTheta - 90),
        otherTree.adjust(rightTheta - 90));
  }

  public ITree adjust(double theta) {
    return new Branch(leftLength, rightLength, leftTheta + theta, rightTheta + theta,
        left.adjust(theta), right.adjust(theta));
  }

  public double getWidth() {
    return draw().getWidth();
  }
}

class TreeWorld extends World {
  WorldImage tree;
  int WIDTH = 800;
  int HEIGHT = 600;

  TreeWorld(ITree tree) {
    this.tree = tree.draw();
  }

  public WorldScene makeScene() {
    return getEmptyScene().placeImageXY(new VisiblePinholeImage(tree, Color.RED),
        WIDTH / 2 - (int) tree.getWidth() / 2, HEIGHT / 2 - (int) tree.getHeight() / 2);
  }

  public boolean bigBang() {
    return bigBang(WIDTH, HEIGHT);
  }
}

class ExamplesTree {
  ITree l1 = new Leaf(10, Color.GREEN);
  ITree s1 = new Stem(20, 90, l1);
  ITree s2 = new Stem(30, 45, s1);

  ITree tree1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
  ITree tree2 = new Branch(30, 30, 115, 65, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));
  ITree combined = tree1.combine(40, 50, 150, 30, tree2);

  // for visually seeing results; plug whatever tree you want instead
  boolean testShowTree(Tester t) {
    ITree toDraw = combined;
    new TreeWorld(toDraw).bigBang();
    return true;
  }

  boolean testDraw(Tester t) {
    int s2x = (int) (30 * Math.cos(Math.toRadians(45)));
    int s2y = (int) (30 * Math.sin(Math.toRadians(45)));

    return t.checkExpect(l1.draw(), new CircleImage(10, "solid", Color.GREEN)) &&
        // Test drawing a stem with a 90-degree angle
        t.checkExpect(s1.draw(), new OverlayImage(l1.draw(),
            new LineImage(new Posn(0, -20), Color.BLACK).movePinhole(0, -10)).movePinhole(0, 20)) &&
        // Test drawing a stem with a 45-degree angle
        t.checkExpect(s2.draw(), new OverlayImage(s1.draw(),
            new LineImage(new Posn(s2x, -s2y), Color.BLACK).movePinhole(s2x / 2.0,
                -s2y / 2.0)).movePinhole(-s2x, s2y));
  }

  boolean testIsDrooping(Tester t) {
    return t.checkExpect(l1.isDrooping(), false) && t.checkExpect(s2.isDrooping(), false)
        && t.checkExpect(new Stem(20, 225, l1).isDrooping(), true) && t.checkExpect(
        new Branch(20, 25, 145, -35, s1, s2).isDrooping(), true);
  }

  boolean testCombine(Tester t) {
    return t.checkExpect(combined, new Branch(40, 50, 150, 30,
        new Branch(30, 30, 135 + 150 - 90, 40 + 150 - 90, new Leaf(10, Color.RED),
            new Leaf(15, Color.BLUE)),
        new Branch(30, 30, 115 + 30 - 90, 65 + 30 - 90, new Leaf(15, Color.GREEN),
            new Leaf(8, Color.ORANGE))));
  }

  boolean testGetWidth(Tester t) {
    return t.checkExpect(l1.getWidth(), 20.0) &&
        t.checkExpect(s1.getWidth(), 20.0) &&
        t.checkExpect(tree1.getWidth(), tree1.draw().getWidth());
  }
}
