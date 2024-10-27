package assignment5;

import javalib.funworld.*;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.Color;
import java.util.Random;

/*
  A couple of notes about my code:
  - Because there's no mutation, each class needs to have a constructor of all changing fields,
    but I also found it convenient to have a constructor for creating the object for the first time.
    This means that there are generally 2 constructors:
      One for actually creating a new object, and one for transitioning to a new state.

  - I was conflicted at first about whether I should keep the list of ships and the list of bullets
    separated, or just have one list of entities. I ended up deciding that splitting them would be
    better for various reason(such as not having to traverse the whole list to find whether there
     are no bullets left), but also ended up using only one set of classes for both lists.
     I'm not sure if creating class for a list of bullets and a class for a list of ships would
     be better, but it could at least have some benefits:
        1. be able to add a spawn method for ships and bullets (right now I would need to have a
        spawnShips method and a spawnBullets method, and that seems like a bad idea).
        2. Be able to make a more customized collision detection for each.
 */

class MyPosn extends Posn {
  public MyPosn(int x, int y) {
    super(x, y);
  }

  MyPosn add(MyPosn other) {
    return new MyPosn(x + other.x, y + other.y);
  }

  boolean isOffscreen(int width, int height, int radius) {
    return x + radius < 0 || x - radius > width || y + radius < 0 || y - radius > height;
  }

  int distanceFrom(MyPosn other) {
    int x = this.x - other.x;
    int y = this.y - other.y;
    return (int) Math.round(Math.hypot(x, y));
  }
}

interface ILoEntity {
  ILoEntity moveAll();
  WorldScene placeAll(WorldScene scene);
  ILoEntity collideAll(ILoEntity bullets);
  ILoEntity collideHelper(ILoEntity other, ILoEntity acc);
  boolean isEmpty();
  boolean isColliding(AEntity ent);
  ILoEntity append(ILoEntity other);
  int length();
  ILoEntity removeOffscreen(int width, int height);
}

class MtLoEntity implements ILoEntity {
  public ILoEntity moveAll() { return this; }

  public WorldScene placeAll(WorldScene scene) { return scene; }

  public ILoEntity collideAll(ILoEntity bullets) { return this; }

  public ILoEntity collideHelper(ILoEntity other, ILoEntity acc) { return acc; }

  public boolean isEmpty() { return true; }

  public boolean isColliding(AEntity ent) { return false; }

  public ILoEntity append(ILoEntity other) { return other; }

  public int length() { return 0; }

  public ILoEntity removeOffscreen(int width, int height) { return this; }
}

class ConsLoEntity implements ILoEntity {
  AEntity first;
  ILoEntity rest;

  public ConsLoEntity(AEntity first, ILoEntity rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoEntity moveAll() {
    return new ConsLoEntity(first.move(), rest.moveAll());
  }

  public WorldScene placeAll(WorldScene scene) {
    return rest.placeAll(first.place(scene));
  }

  public ILoEntity collideAll(ILoEntity other) {
    return collideHelper(other, new MtLoEntity());
  }

  public ILoEntity collideHelper(ILoEntity other, ILoEntity acc) {
    if (other.isColliding(first)) {
      // each collided entity is "contributing" to the collision by adding entities
      return rest.collideHelper(other, first.onExplode().append(acc));
    }
    else {
      return rest.collideHelper(other, new ConsLoEntity(first, acc));
    }
  }

  public boolean isEmpty() {
    return false;
  }

  public boolean isColliding(AEntity ent) {
    return first.isColliding(ent) || rest.isColliding(ent);
  }

  public ILoEntity append(ILoEntity other) {
    return new ConsLoEntity(first, rest.append(other));
  }

  public int length() {
    return 1 + rest.length();
  }

  public ILoEntity removeOffscreen(int width, int height) {
    if (first.isOffscreen(width, height)) {
      return rest.removeOffscreen(width, height);
    }
    return new ConsLoEntity(first, rest.removeOffscreen(width, height));
  }
}

abstract class AEntity {
  Color color;
  int radius;
  MyPosn pos;
  MyPosn velocity;

  abstract AEntity move();

  WorldScene place(WorldScene scene) {
    return scene.placeImageXY(draw(), pos.x, pos.y);
  }

  WorldImage draw() {
    return new CircleImage(radius, "solid", color);
  }

  boolean isColliding(AEntity other) {
    return pos.distanceFrom(other.pos) < this.radius + other.radius;
  }

  abstract public ILoEntity onExplode(); // The entities produced when this explodes

  public boolean isOffscreen(int width, int height) {
    return pos.isOffscreen(width, height, radius);
  }
}

class Bullet extends AEntity {
  int explosion;

  Bullet(int radius, MyPosn pos, MyPosn velocity, int explosion) {
    this.radius = radius;
    this.pos = pos;
    this.velocity = velocity;
    this.explosion = explosion;
    this.color = Consts.BULLET_COLOR;
  }

  Bullet(MyPosn pos) {
    this(Consts.INITIAL_BULLET_RADIUS, pos, new MyPosn(0, -Consts.BULLET_SPEED), 0);
  }

  AEntity move() {
    return new Bullet(radius, pos.add(velocity), velocity, explosion);
  }

  public ILoEntity onExplode() {
    return generateNewBullets(Math.min((explosion + 2), Consts.MAXIMUM_BULLETS_PER_EXPLOSION),
        (explosion + 1));
  }

  private ILoEntity generateNewBullets(int i, int explosion) {
    if (i == 0) {
      return new MtLoEntity();
    }

    double firingAngle = Math.toRadians(i * (360.0 / (explosion + 1)));
    return new ConsLoEntity(
        new Bullet(Math.min(radius + Consts.BULLET_RADIUS_INCREMENT, Consts.MAXIMUM_BULLET_RADIUS),
            pos, new MyPosn((int) Math.round(Math.cos(firingAngle) * Consts.BULLET_SPEED),
            (int) Math.round(Math.sin(firingAngle)) * Consts.BULLET_SPEED), explosion),
        generateNewBullets(i - 1, explosion));
  }
}

class Ship extends AEntity {
  Ship(MyPosn pos, int velocityX) {
    this.radius = Consts.SHIP_RADIUS;
    this.color = Consts.SHIP_COLOR;
    this.velocity = new MyPosn(velocityX, 0);
    this.pos = pos;
  }

  Ship(Random rand) {
    this.radius = Consts.SHIP_RADIUS;
    this.color = Consts.SHIP_COLOR;

    int y = rand.nextInt(Consts.SHIP_MAX_SPAWN_HEIGHT - Consts.SHIP_MIN_SPAWN_HEIGHT)
        + Consts.SHIP_MIN_SPAWN_HEIGHT;
    if (rand.nextInt(2) == 0) {
      this.velocity = new MyPosn(Consts.SHIP_SPEED, 0);
      this.pos = new MyPosn(0, y);
    }
    else {
      this.velocity = new MyPosn(-Consts.SHIP_SPEED, 0);
      this.pos = new MyPosn(Consts.SCREEN_WIDTH, y);
    }
  }

  AEntity move() {
    return new Ship(pos.add(velocity), velocity.x);
  }

  // a ship produces nothing when it explodes
  public ILoEntity onExplode() {
    return new MtLoEntity();
  }
}

class MyWorld extends World {
  int width;
  int height;
  double tickRate;
  Timer spawnTimer;
  Random rand;
  ILoEntity ships;
  ILoEntity bullets;
  int bulletsLeft;
  int shipsDestroyed;

  public MyWorld(int width, int height, double tickRate, Timer spawnTimer, Random rand,
      ILoEntity ships, ILoEntity bullets, int bulletsLeft, int shipsDestroyed) {
    this.width = width;
    this.height = height;
    this.tickRate = tickRate;
    this.spawnTimer = spawnTimer;
    this.rand = rand;
    this.ships = ships;
    this.bullets = bullets;
    this.bulletsLeft = bulletsLeft;
    this.shipsDestroyed = shipsDestroyed;
  }

  public MyWorld(int bulletsLeft) {
    this(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, Consts.TICK_RATE,
        new Timer(Consts.SHIP_SPAWN_FREQUENCY), new Random(), new MtLoEntity(), new MtLoEntity(),
        bulletsLeft, 0);
  }

  public WorldScene makeScene() {
    return placeInfo(bullets.placeAll(ships.placeAll(new WorldScene(width, height))));
  }

  public WorldScene placeInfo(WorldScene scene) {
    WorldImage scoreText = new TextImage(String.format("Score: %s", shipsDestroyed),
        Consts.FONT_SIZE, Consts.FONT_COLOR);
    WorldImage bulletsLeftText = new TextImage(String.format("Bullets left: %s", bulletsLeft),
        Consts.FONT_SIZE, Consts.FONT_COLOR);
    return scene.placeImageXY(new AboveAlignImage(AlignModeX.LEFT, scoreText, bulletsLeftText),
        width / 8, height / 10);
  }

  WorldScene finalScene() {
    WorldImage scoreText = new TextImage(String.format("Score: %s", shipsDestroyed),
        Consts.FONT_SIZE_BIG, Consts.FONT_COLOR);
    WorldImage gameOverText = new TextImage("Game Over!", Consts.FONT_SIZE_BIG, Consts.FONT_COLOR);
    return new WorldScene(width, height).placeImageXY(new AboveImage(gameOverText, scoreText),
        width / 2, height / 2);
  }

  /*
  Alternatively, without a "fluent interface":

  ILoEntity collidedShips = ships.collideAll(bullets);
    int newShipsDestroyed = shipsDestroyed + ships.length() - collidedShips.length();

    return new MyWorld(width, height, tickRate, spawnTimer.next(tickRate), rand,
        spawnShips(collidedShips.moveAll().removeOffscreen(width, height)),
        bullets.collideAll(ships).moveAll().removeOffscreen(width, height), bulletsLeft,
        newShipsDestroyed);
   */
  public MyWorld onTick() {
    return this.collide().move().removeOffscreen().spawnShips().nextTimer();
  }

  public MyWorld collide() {
    ILoEntity collidedShips = ships.collideAll(bullets);
    return new MyWorld(width, height, tickRate, spawnTimer, rand, collidedShips,
        bullets.collideAll(ships), bulletsLeft,
        shipsDestroyed + ships.length() - collidedShips.length());
  }

  public MyWorld move() {
    return new MyWorld(width, height, tickRate, spawnTimer, rand, ships.moveAll(),
        bullets.moveAll(), bulletsLeft, shipsDestroyed);
  }

  public MyWorld removeOffscreen() {
    return new MyWorld(width, height, tickRate, spawnTimer, rand,
        ships.removeOffscreen(width, height), bullets.removeOffscreen(width, height), bulletsLeft,
        shipsDestroyed);
  }

  public MyWorld spawnShips() {
    return new MyWorld(width, height, tickRate, spawnTimer, rand, spawnShips(ships), bullets,
        bulletsLeft, shipsDestroyed);
  }

  public MyWorld nextTimer() {
    return new MyWorld(width, height, tickRate, spawnTimer.next(tickRate), rand, ships, bullets,
        bulletsLeft, shipsDestroyed);
  }

  public ILoEntity spawnShips(ILoEntity ships) {
    if (spawnTimer.isFinished()) {
      return spawnShipsHelper(ships, rand.nextInt(Consts.SHIP_SPAWN_RANGE) + 1);
    }
    return ships;
  }

  ILoEntity spawnShipsHelper(ILoEntity ships, int count) {
    if (count == 0) {
      return ships;
    }
    else {
      return spawnShipsHelper(new ConsLoEntity(new Ship(rand), ships), count - 1);
    }
  }

  public MyWorld onKeyEvent(String key) {
    if (key.equals(" ") && bulletsLeft > 0) {
      return new MyWorld(width, height, tickRate, spawnTimer, rand, ships,
          new ConsLoEntity(new Bullet(new MyPosn(width / 2, height)), bullets), bulletsLeft - 1,
          shipsDestroyed);
    }
    else {
      return this;
    }
  }

  public WorldEnd worldEnds() {
    if (bulletsLeft <= 0 && bullets.isEmpty()) {
      return new WorldEnd(true, this.finalScene());
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  public boolean bigBang() {
    return super.bigBang(width, height, tickRate);
  }
}

class ExamplesGame {
  MyPosn pCenter = new MyPosn(Consts.SCREEN_WIDTH / 2, Consts.SCREEN_HEIGHT / 2);
  MyPosn pTopLeft = new MyPosn(Consts.SCREEN_WIDTH / 4, (Consts.SCREEN_HEIGHT * 6) / 7);
  MyPosn pBottomRight = new MyPosn((Consts.SCREEN_WIDTH * 3) / 4, Consts.SCREEN_HEIGHT / 7);
  MyPosn pInBorderTop = new MyPosn(Consts.SCREEN_WIDTH / 2, 0);
  MyPosn pInBorderRight = new MyPosn(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT / 2);

  MyPosn shipV = new MyPosn(0, Consts.SHIP_SPEED);
  Ship s1 = new Ship(pCenter, Consts.SHIP_SPEED);
  Ship s2 = new Ship(pTopLeft, -Consts.SHIP_SPEED);
  Ship s3 = new Ship(pBottomRight, Consts.SHIP_SPEED);
  ILoEntity loe0 = new MtLoEntity();
  ILoEntity los1 = new ConsLoEntity(s1, loe0);
  ILoEntity los2 = new ConsLoEntity(s2, los1);
  ILoEntity los3 = new ConsLoEntity(s3, los2);

  Bullet b1 = new Bullet(pCenter);
  Bullet b2 = new Bullet(pInBorderTop);
  ILoEntity lob1 = new ConsLoEntity(b1, loe0);
  ILoEntity lob2 = new ConsLoEntity(b2, lob1);

  Random randTest = new Random(1000);
  MyWorld startWorld = new MyWorld(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, Consts.TICK_RATE,
      new Timer(Consts.SHIP_SPAWN_FREQUENCY), randTest, loe0, loe0, 10, 0);
  MyWorld noCollisionsWorld = new MyWorld(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT,
      Consts.TICK_RATE, new Timer(Consts.SHIP_SPAWN_FREQUENCY, 0), randTest, los2,
      new ConsLoEntity(b2, loe0), 9, 1);
  MyWorld collisionsWorld = new MyWorld(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, Consts.TICK_RATE,
      new Timer(Consts.SHIP_SPAWN_FREQUENCY, Consts.TICK_RATE), randTest, los2, lob2, 8, 1);
  MyWorld gameOver = new MyWorld(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, Consts.TICK_RATE,
      new Timer(Consts.SHIP_SPAWN_FREQUENCY, Consts.TICK_RATE), randTest, los2, loe0, 0, 565);

  boolean testBigBang(Tester t) {
    MyWorld world = new MyWorld(10);
    return world.bigBang();
  }

  boolean testMove(Tester t) {
    return t.checkExpect(s1.move(), new Ship(pCenter.add(shipV), s1.velocity.x)) && t.checkExpect(
        loe0.moveAll(), loe0) && t.checkExpect(los3.moveAll(), new ConsLoEntity(s3.move(),
        new ConsLoEntity(s2.move(), new ConsLoEntity(s1.move(), loe0))));
  }

  boolean testTimer(Tester t) {
    Timer finishedTimer = new Timer(2.0, -0.01);
    return t.checkExpect(new Timer(12.0).isFinished(), false) && t.checkExpect(
        finishedTimer.isFinished(), true) && t.checkExpect(finishedTimer.next(0.1).isFinished(),
        false);
  }

  boolean testMakeScene(Tester t) {
    WorldScene startScene = new WorldScene(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT);
    return t.checkExpect(startWorld.makeScene(), startWorld.placeInfo(startScene)) && t.checkExpect(
        noCollisionsWorld.makeScene(), noCollisionsWorld.placeInfo(
            startScene.placeImageXY(s1.draw(), s1.pos.x, s1.pos.y)
                .placeImageXY(s2.draw(), s2.pos.x, s2.pos.y)
                .placeImageXY(b2.draw(), b2.pos.x, b2.pos.y)));
  }

  boolean testWorldEnds(Tester t) {
    return t.checkExpect(startWorld.worldEnds(), new WorldEnd(false, startWorld.makeScene()))
        && t.checkExpect(collisionsWorld.worldEnds(),
        new WorldEnd(false, collisionsWorld.makeScene())) && t.checkExpect(gameOver.worldEnds(),
        new WorldEnd(true, gameOver.finalScene()));
  }

  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(startWorld.onKeyEvent("x"), startWorld) && t.checkExpect(
        startWorld.onKeyEvent(" "),
        new MyWorld(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, Consts.TICK_RATE,
            new Timer(Consts.SHIP_SPAWN_FREQUENCY), randTest, loe0,
            new ConsLoEntity(new Bullet(new MyPosn(Consts.SCREEN_WIDTH / 2, Consts.SCREEN_HEIGHT)),
                loe0), 9, 0)) && t.checkExpect(collisionsWorld.onKeyEvent(" "),
        new MyWorld(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, Consts.TICK_RATE,
            new Timer(Consts.SHIP_SPAWN_FREQUENCY, Consts.TICK_RATE), randTest, los2,
            new ConsLoEntity(new Bullet(new MyPosn(Consts.SCREEN_WIDTH / 2, Consts.SCREEN_HEIGHT)),
                lob2), 7, collisionsWorld.shipsDestroyed));
  }

  boolean testIsCollision(Tester t) {
    return t.checkExpect(s1.isColliding(s1), true) && t.checkExpect(s1.isColliding(b1), true)
        && t.checkExpect(b1.isColliding(s1), true) && t.checkExpect(s2.isColliding(b1), false)
        && t.checkExpect(loe0.isColliding(b1), false) && t.checkExpect(los3.isColliding(b1), true)
        && t.checkExpect(los3.isColliding(b2), false);
  }

  boolean testRemoveOffscreen(Tester t) {
    int w = Consts.SCREEN_WIDTH;
    int h = Consts.SCREEN_HEIGHT;

    return t.checkExpect(loe0.removeOffscreen(w, h), loe0) && t.checkExpect(
        los3.removeOffscreen(0, 0), loe0) && t.checkExpect(los1.removeOffscreen(w, h), los1)
        && t.checkExpect(
        new ConsLoEntity(b2.move().move().move().move().move().move(), loe0).removeOffscreen(w, h),
        loe0) && t.checkExpect(new ConsLoEntity(b2,
        new ConsLoEntity(new Ship(pInBorderRight.add(new MyPosn(Consts.SHIP_RADIUS + 1, 0)), 0),
            lob1)).removeOffscreen(w, h), lob2);
  }

  boolean testCollide(Tester t) {
    int newRadius = Consts.INITIAL_BULLET_RADIUS + Consts.BULLET_RADIUS_INCREMENT;
    return t.checkExpect(loe0.collideAll(lob2), loe0) && t.checkExpect(
        los2.collideAll(new ConsLoEntity(b2, loe0)), los2) && t.checkExpect(los3.collideAll(lob2),
        new ConsLoEntity(s3, new ConsLoEntity(s2, loe0))) && t.checkExpect(lob2.collideAll(los3),
        new ConsLoEntity(new Bullet(newRadius, b1.pos,
            new MyPosn((int) Math.round(Math.cos(Math.toRadians(2 * 360.0)) * Consts.BULLET_SPEED),
                (int) Math.round(Math.sin(Math.toRadians(2 * 360.0))) * Consts.BULLET_SPEED), 1),
            new ConsLoEntity(new Bullet(newRadius, b1.pos, new MyPosn(
                (int) Math.round(Math.cos(Math.toRadians(2 * 180.0)) * Consts.BULLET_SPEED),
                (int) Math.round(Math.sin(Math.toRadians(2 * 180.0))) * Consts.BULLET_SPEED), 1),
                new ConsLoEntity(b2, loe0))

        ));
  }

  boolean testOnTick(Tester t) {
    return t.checkExpect(startWorld.onTick(),
        new MyWorld(startWorld.width, startWorld.height, startWorld.tickRate,
            startWorld.spawnTimer.next(startWorld.tickRate), randTest, startWorld.spawnShips(loe0),
            loe0, startWorld.bulletsLeft, startWorld.shipsDestroyed));
  }
}