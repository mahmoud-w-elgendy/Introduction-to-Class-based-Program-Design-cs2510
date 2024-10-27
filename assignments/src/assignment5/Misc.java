package assignment5;

import java.awt.*;

// Using static is cheating a bit since we didn't learn it yet,
// but its really just a little matter of convenience here
class Consts {
  static int SCREEN_WIDTH = 1000;
  static int SCREEN_HEIGHT = 600;
  static double TICK_RATE = 1.0 / 28.0;
  static double SHIP_SPAWN_FREQUENCY = 1.0;
  static int SHIP_SPAWN_RANGE = 3;
  static int INITIAL_BULLET_RADIUS = 20;
  static int BULLET_RADIUS_INCREMENT = 5;
  static int MAXIMUM_BULLET_RADIUS = 50;
  static int MAXIMUM_BULLETS_PER_EXPLOSION = 3;
  static Color BULLET_COLOR = Color.PINK;
  static int BULLET_SPEED = 8; // In pixels/tick
  static int SHIP_RADIUS = SCREEN_WIDTH / 30;
  static Color SHIP_COLOR = Color.CYAN;
  static int SHIP_SPEED = BULLET_SPEED / 2;
  static int SHIP_MIN_SPAWN_HEIGHT = SCREEN_HEIGHT / 7;
  static int SHIP_MAX_SPAWN_HEIGHT = (SCREEN_HEIGHT * 6) / 7;
  static Color FONT_COLOR = Color.BLACK;
  static int FONT_SIZE = 30;
  static int FONT_SIZE_BIG = 80;
}

// A little helper class for managing the incrementing and resetting of time
class Timer {
  double startTime;
  double currentTime;

  public Timer(double startTime) {
    this.startTime = startTime;
    this.currentTime = startTime;
  }

  public Timer(double startTime, double currentTime) {
    this.startTime = startTime;
    this.currentTime = currentTime;
  }

  boolean isFinished() {
    return currentTime <= 0.0001;
  }

  Timer next(double tickRate) {
    if (currentTime <= 0.0001) {
      return new Timer(startTime, startTime - currentTime);
    }
    return new Timer(startTime, currentTime - tickRate);
  }
}
