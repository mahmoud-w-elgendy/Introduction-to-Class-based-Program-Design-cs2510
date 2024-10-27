package assignment4.problem1;

import tester.Tester;

class BagelRecipe {
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;

  double CUP_OF_TEASPOONS = 48;
  double CUP_OF_FLOUR = 4.25;
  // A teaspoon is _weight_of_cup_of_ingredient / CUP_OF_TEASPOONS
  double TEASPOON_OF_YEAST = 5.0 / CUP_OF_TEASPOONS;
  double TEASPOON_OF_SALT = 10.0 / CUP_OF_TEASPOONS;

  public BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    checkPerfect(flour, water, yeast, salt, malt);

    this.flour = flour;
    this.water = water;
    this.yeast = yeast;
    this.salt = salt;
    this.malt = malt;
  }

  public BagelRecipe(double flour, double yeast) {
    // Use the given arguments to compute the other fields
    // and produce a perfect bagel recipe
    this.flour = flour;
    this.water = flour;
    this.yeast = yeast;
    this.salt = flour/20 - yeast;
    this.malt = yeast;
    checkPerfect(flour, water, yeast, salt, malt);
  }

  public BagelRecipe(double flourVolume, double yeastVolume, double saltVolume) {
    this.flour = CUP_OF_FLOUR * flourVolume;
    this.water = flour;
    this.yeast = TEASPOON_OF_YEAST * yeastVolume;
    this.salt = TEASPOON_OF_SALT * saltVolume;
    this.malt = yeast;

    checkPerfect(flour, water, yeast, salt, malt);
  }

  private void checkPerfect(double flour, double water, double yeast, double salt, double malt) {
    Util u = new Util();
    boolean isPerfect = (u.equalsTo(flour, water) &&
        u.equalsTo(yeast, malt) &&
        u.equalsTo(salt + yeast, flour/20) &&
        !(flour < 0.0 || water < 0.0 || yeast < 0.0 || salt < 0.0 || malt < 0.0)
    );

    if (!isPerfect) {
      throw new IllegalArgumentException("Invalid arguments: not a perfect bagel recipe");
    }
  }

  public boolean sameRecipe(BagelRecipe other) {
    return other.sameRecipe(flour, water, yeast, salt, malt);
  }

  public boolean sameRecipe(double flour, double water, double yeast, double salt, double malt) {
    Util u = new Util();
    return u.equalsTo(this.flour, flour) && u.equalsTo(this.water, water) &&
        u.equalsTo(this.yeast, yeast) && u.equalsTo(this.salt, salt) && u.equalsTo(this.malt, malt);
  }

}


class Util {
  double PRECISION = 0.001;
  boolean equalsTo(double a, double b) {
    return Math.abs(a - b) <= PRECISION;
  }
}


class ExamplesBagel {
  boolean testMainConstructor(Tester t) {
    // Valid perfect bagel recipes
    return t.checkExpect(new BagelRecipe(20, 20, 1, 0, 1), new BagelRecipe(20, 20, 1, 0, 1)) &&
    t.checkExpect(new BagelRecipe(10, 10, 0.5, 0, 0.5), new BagelRecipe(10, 10, 0.5, 0, 0.5)) &&

    // Invalid recipes
    t.checkConstructorException(
        new IllegalArgumentException("Invalid arguments: not a perfect bagel recipe"),
        "assignment4.problem1.BagelRecipe", 20.0, 18.0, 1.0, 0.0, 1.0) &&
    t.checkConstructorException(
        new IllegalArgumentException("Invalid arguments: not a perfect bagel recipe"),
        "assignment4.problem1.BagelRecipe", 20.0, 20.0, 1.0, 1.0, 1.0);
  }

  boolean testFlourYeastConstructor(Tester t) {
    // Valid perfect bagel recipes
    return t.checkExpect(new BagelRecipe(20, 1), new BagelRecipe(20, 20, 1, 0, 1)) &&
    t.checkExpect(new BagelRecipe(10, 0.5), new BagelRecipe(10, 10, 0.5, 0, 0.5)) &&

    // Invalid recipe: salt + yeast exceeds flour/20
    t.checkConstructorException(
        new IllegalArgumentException("Invalid arguments: not a perfect bagel recipe"),
        "assignment4.problem1.BagelRecipe", 20.0, 1.5);
  }

  boolean testVolumeConstructor(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Invalid arguments: not a perfect bagel recipe"),
        "assignment4.problem1.BagelRecipe", 5.0, 2.0, 1.0);
  }

  boolean testSameRecipe(Tester t) {
    BagelRecipe recipe1 = new BagelRecipe(20, 20, 1, 0, 1);
    BagelRecipe recipe2 = new BagelRecipe(20, 20, 1.001, 0, 1);

    // Checking that recipes are equal within precision
    return t.checkExpect(recipe1.sameRecipe(recipe2), true);
  }

}