package lab3;

import tester.Tester;

interface IGamePiece {
  int getValue();
  IGamePiece merge(IGamePiece other);
  boolean isValid();
}

class BaseTile implements IGamePiece {
  int value;

  public BaseTile(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public IGamePiece merge(IGamePiece other) {
    return new MergeTile(this, other);
  }

  public boolean isValid() {
    return true;
  }
}

class MergeTile implements IGamePiece {
  IGamePiece piece1;
  IGamePiece piece2;

  public MergeTile(IGamePiece piece1, IGamePiece piece2) {
    this.piece1 = piece1;
    this.piece2 = piece2;
  }

  public int getValue() {
    return piece1.getValue() + piece2.getValue();
  }

  public IGamePiece merge(IGamePiece other) {
    return new MergeTile(this, other);
  }

  // Only equal-valued pieces can merge
  public boolean isValid() {
    return piece1.isValid() && piece2.isValid() && (piece1.getValue() == piece2.getValue());
  }
}

class ExamplesGamePiece {
  IGamePiece four = new MergeTile(new BaseTile(2), new BaseTile(2));
  boolean testGetValue(Tester t) {
    return t.checkExpect(four.getValue(), 4);
  }
}