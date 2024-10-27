package assignment2.problem2;

import tester.Tester;

interface IPicture {
  int getWidth();

  int countShapes();

  int comboDepth();

  IPicture mirror();

  String pictureRecipe(int depth);
}

interface IOperation {
  int getWidth();

  int countShapes();

  int comboDepth();

  IOperation mirror();

  String pictureRecipe(int depth);
}

class Shape implements IPicture {
  String kind;
  int size;

  public Shape(String kind, int size) {
    this.kind = kind;
    this.size = size;
  }

  public int getWidth() {
    return size;
  }

  public int countShapes() {
    return 1;
  }

  public int comboDepth() {
    return 0;
  }

  public IPicture mirror() {
    return this;
  }

  public String pictureRecipe(int depth) {
    return kind;
  }
}

class Combo implements IPicture {
  String name;
  IOperation operation;

  public Combo(String name, IOperation operation) {
    this.name = name;
    this.operation = operation;
  }

  public int getWidth() {
    return operation.getWidth();
  }

  public int countShapes() {
    return operation.countShapes();
  }

  public int comboDepth() {
    return 1 + operation.comboDepth();
  }

  public IPicture mirror() {
    return new Combo(name, operation.mirror());
  }

  public String pictureRecipe(int depth) {
    if (depth == 0) {
      return name;
    }
    return operation.pictureRecipe(depth - 1);
  }
}

class Scale implements IOperation {
  IPicture picture;

  public Scale(IPicture picture) {
    this.picture = picture;
  }

  public int getWidth() {
    return picture.getWidth() * 2;
  }

  public int countShapes() {
    return picture.countShapes();
  }

  public int comboDepth() {
    return picture.comboDepth();
  }

  public IOperation mirror() {
    return new Scale(picture.mirror());
  }

  public String pictureRecipe(int depth) {
    return "scale(" + picture.pictureRecipe(depth) + ")";
  }
}

class Beside implements IOperation {
  IPicture picture1;
  IPicture picture2;

  public Beside(IPicture picture1, IPicture picture2) {
    this.picture1 = picture1;
    this.picture2 = picture2;
  }

  public int getWidth() {
    return picture1.getWidth() + picture2.getWidth();
  }

  public int countShapes() {
    return picture1.countShapes() + picture2.countShapes();
  }

  public int comboDepth() {
    return Math.max(picture1.comboDepth(), picture2.comboDepth());
  }

  public IOperation mirror() {
    return new Beside(picture2, picture1);
  }

  public String pictureRecipe(int depth) {
    return String.format("beside(%s, %s)", picture1.pictureRecipe(depth),
        picture2.pictureRecipe(depth));
  }
}

class Overlay implements IOperation {
  IPicture topPicture;
  IPicture bottomPicture;

  public Overlay(IPicture topPicture, IPicture bottomPicture) {
    this.topPicture = topPicture;
    this.bottomPicture = bottomPicture;
  }

  public int getWidth() {
    return Math.max(bottomPicture.getWidth(), topPicture.getWidth());
  }

  public int countShapes() {
    return bottomPicture.countShapes() + topPicture.countShapes();
  }

  public int comboDepth() {
    return Math.max(topPicture.comboDepth(), bottomPicture.comboDepth());
  }

  public IOperation mirror() {
    return new Overlay(topPicture.mirror(), bottomPicture.mirror());
  }

  public String pictureRecipe(int depth) {
    return String.format("overlay(%s, %s)", topPicture.pictureRecipe(depth),
        bottomPicture.pictureRecipe(depth));
  }
}


class ExamplesPicture {
  IPicture circle = new Shape("circle", 20);
  IPicture square = new Shape("square", 30);
  IPicture bigCircle = new Combo("big circle",
      new Scale(circle));
  IPicture squareOnCircle = new Combo("square on a circle",
      new Overlay(square, bigCircle));
  IPicture doubledSquareOnCircle = new Combo("doubled square on circle",
      new Beside(squareOnCircle, squareOnCircle));
  // One more example for each shape
  IPicture giantCircle = new Combo("giant circle",
      new Scale(bigCircle));
  IPicture squareOnCircleOnCircle = new Combo("square on circle on circle",
      new Overlay(squareOnCircle, giantCircle));
  IPicture squareAndCircle = new Combo("square and circle",
      new Beside(square, circle));

  boolean testGetWidth(Tester t) {
    return t.checkExpect(circle.getWidth(), 20) &&
        t.checkExpect(squareOnCircle.getWidth(), 40) &&
        t.checkExpect(bigCircle.getWidth(), 40) &&
        t.checkExpect(doubledSquareOnCircle.getWidth(), 80);
  }

  boolean testCountShapes(Tester t) {
    return t.checkExpect(circle.countShapes(), 1) &&
        t.checkExpect(bigCircle.countShapes(), 1) &&
        t.checkExpect(squareOnCircle.countShapes(), 2) &&
        t.checkExpect(doubledSquareOnCircle.countShapes(), 4);
  }

  boolean testComboDepth(Tester t) {
    return t.checkExpect(circle.comboDepth(), 0) &&
        t.checkExpect(bigCircle.comboDepth(), 1) &&
        t.checkExpect(squareOnCircle.comboDepth(), 2) &&
        t.checkExpect(doubledSquareOnCircle.comboDepth(), 3);
  }

  boolean testMirror(Tester t) {
    return t.checkExpect(circle.mirror(), circle) &&
        t.checkExpect(squareOnCircle.mirror(), squareOnCircle) &&
        t.checkExpect(doubledSquareOnCircle.mirror(), doubledSquareOnCircle) &&
        t.checkExpect(squareAndCircle.mirror(), new Combo("square and circle",
            new Beside(circle, square))) &&
        t.checkExpect(new Combo("n", new Overlay(circle, squareAndCircle)).mirror(),
            new Combo("n", new Overlay(circle, new Combo("square and circle",
                new Beside(circle, square)))));
  }

  boolean testPictureRecipe(Tester t) {
    return t.checkExpect(circle.pictureRecipe(2), "circle") &&
        t.checkExpect(doubledSquareOnCircle.pictureRecipe(0),
            "doubled square on circle") &&
        t.checkExpect(doubledSquareOnCircle.pictureRecipe(2),
            "beside(overlay(square, big circle), overlay(square, big circle))") &&
        t.checkExpect(doubledSquareOnCircle.pictureRecipe(3),
            "beside(overlay(square, scale(circle)), overlay(square, scale(circle)))");
  }
}