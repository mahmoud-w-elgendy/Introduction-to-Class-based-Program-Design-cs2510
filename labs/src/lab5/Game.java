package lab5;

import java.util.Random;
import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;


class MyPosn extends Posn {

  // standard constructor
  MyPosn(int x, int y) {
    super(x, y);
  }
  // constructor to convert from a Posn to a MyPosn
  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  MyPosn add(MyPosn other) {
    return new MyPosn(x + other.x, y + other.y);
  }

  boolean isOffscreen(int width, int height) {
    return x < 0 || x > width || y < 0 || y > height;
  }
  boolean isOffscreenRadius(int width, int height, int radius) {
    // given a radius from the Posn, return true if resulting circle is completly offscreen
    return x + radius < 0 || x - radius > width || y + radius  < 0 || y - radius > height;
  }
}

class Circle {
  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick
  Color color;
  int radius;

  public Circle(MyPosn position, MyPosn velocity, Color color, int radius) {
    this.position = position;
    this.velocity = velocity;
    this.color = color;
    this.radius = radius;
  }

  Circle move() {
    MyPosn newPos = position.add(velocity);
    return new Circle(newPos, velocity, color, radius);
  }

  boolean isOffscreen (int width, int height) {
    return position.isOffscreenRadius(width, height, radius);
  }

  WorldImage draw() {
    return new CircleImage(radius, "solid", color);
  }

  WorldScene place(WorldScene scene) {
    return scene.placeImageXY(draw(), position.x, position.y);
  }
}

interface ILoCircle {
  ILoCircle moveAll();
  ILoCircle removeOffscreen(int width, int height);
  WorldScene placeAll(WorldScene scene);
  int getLength();
}

class MtLoCircle implements ILoCircle {

  public ILoCircle moveAll() {
    return this;
  }

  public ILoCircle removeOffscreen(int width, int height) {
    return this;
  }

  public WorldScene placeAll(WorldScene scene) {
    return scene;
  }

  public int getLength() {
    return 0;
  }
}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;

  public ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoCircle moveAll() {
    return new ConsLoCircle(first.move(), rest.moveAll());
  }

  public ILoCircle removeOffscreen(int width, int height) {
    if (first.isOffscreen(width, height)) {
      return rest.removeOffscreen(width, height);
    }
    return new ConsLoCircle(first, rest.removeOffscreen(width, height));
  }

  public WorldScene placeAll(WorldScene scene) {
    return rest.placeAll(first.place(scene));
  }

  public int getLength() {
    return 1 + rest.getLength();
  }
}


class MyWorld extends World {
  int width;
  int height;
  double tickRate;
  int circlesToLeave;
  ILoCircle loc;
  Random rand;
  int RANDOM_RANGE = 20;

  public MyWorld(int width, int height, double ticks, int circlesToLeave, ILoCircle loc) {
    this.width = width;
    this.height = height;
    this.tickRate = ticks;
    this.circlesToLeave = circlesToLeave;
    this.loc = loc;
    this.rand = new Random();
  }

  public MyWorld(int circlesToLeave, Random rand) {
    this(800, 600, 1.0/28.0, circlesToLeave, new MtLoCircle());
    this.rand = rand;
  }



  public WorldScene makeScene() {
    return loc.placeAll(new WorldScene(width, height));
  }

  public WorldEnd worldEnds() {
    if (circlesToLeave <= 0) {
      return new WorldEnd(true, this.makeScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  public World onMouseClicked(Posn mouse) {
    MyPosn velocity = new MyPosn(rand.nextInt(RANDOM_RANGE) - RANDOM_RANGE/2,
        rand.nextInt(RANDOM_RANGE) - RANDOM_RANGE/2);
    // Decided to also add randomness to the radius and color
    int radius = rand.nextInt(RANDOM_RANGE) + 1;
    Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255);
    ILoCircle newLoc = new ConsLoCircle(new Circle(new MyPosn(mouse), velocity, color, radius),
        loc);
    return new MyWorld(width, height, tickRate, circlesToLeave, newLoc);
  }

  public World onTick() {
    int oldLength = loc.getLength();
    ILoCircle newLoc = loc.moveAll().removeOffscreen(width, height);
    int removedCircles = oldLength - newLoc.getLength();
    return new MyWorld(width, height, tickRate, circlesToLeave - removedCircles, newLoc);
  }

  public boolean bigBang() {
    return super.bigBang(width, height, tickRate);
  }
}

class ExamplesMyWorldProgram {
  boolean testBigBang(Tester t) {
    MyWorld w = new MyWorld(100, new Random(100000));
    return w.bigBang();
  }
}