package lecture8.problem2;

import tester.Tester;

interface IIntList {
  boolean variantA();
  boolean variantAHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC);
  boolean variantB();
  boolean variantBHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC);
  boolean variantC();
  boolean variantCHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC);
}

class MtIntList implements IIntList {

  public boolean variantA() {
    return false;
  }

  public boolean variantAHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC) {
    return hasPropA && hasPropB && hasPropC;
  }

  public boolean variantB() {
    return false;
  }

  public boolean variantBHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC) {
    return hasPropA && hasPropB && hasPropC;
  }

  public boolean variantC() {
    return false;
  }

  public boolean variantCHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC) {
    return hasPropA && hasPropB && hasPropC;
  }
}

class ConsIntList implements IIntList {
  int first;
  IIntList rest;

  public ConsIntList(int first, IIntList rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean variantA() {
    return this.variantAHelper(false, false, false);
  }

  public boolean variantAHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC) {
    boolean newHasPropA = propA(first) || hasPropA;
    boolean newHasPropB = propB(first) || hasPropB;
    boolean newHasPropC = propC(first) || hasPropB;

    return this.rest.variantAHelper(newHasPropA, newHasPropB, newHasPropC);
  }

  public boolean variantB() {
    return this.variantBHelper(false, false, false);
  }

  public boolean variantBHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC) {
    if (!hasPropA && propA(first)) {
      return rest.variantBHelper(true, hasPropB, hasPropC);
    } else if (!hasPropB && propB(first)) {
      return rest.variantBHelper(hasPropA, true, hasPropC);
    } else if (!hasPropC && propC(first)) {
      return rest.variantBHelper(hasPropA, hasPropB, true);
    }
    return rest.variantBHelper(hasPropA, hasPropB, hasPropC);
  }

  public boolean variantC() {
    return this.variantCHelper(false, false, false);
  }

  public boolean variantCHelper(boolean hasPropA, boolean hasPropB, boolean hasPropC) {
    if (!hasPropA && propA(first)) {
      return rest.variantCHelper(true, hasPropB, hasPropC);
    } else if (!hasPropB && propB(first)) {
      return rest.variantCHelper(hasPropA, true, hasPropC);
    } else if (!hasPropC && propC(first)) {
      return rest.variantCHelper(hasPropA, hasPropB, true);
    }
    return false;
  }

  private boolean propC(int first) {
    return (first >= 5 && first <= 10);
  }

  private boolean propB(int first) {
    return (first % 2 == 1);
  }

  private boolean propA(int first) {
    return (first % 2) == 0;
  }
}


class ExamplesIntList {
  IIntList l0 = new MtIntList();
  IIntList l1 = new ConsIntList(5, l0);
  IIntList l2 = new ConsIntList(6, l1);
  IIntList l3 = new ConsIntList(5, l2);
  IIntList l4 = new ConsIntList(4, new ConsIntList(3, l0));
  IIntList l5 = new ConsIntList(6,
      new ConsIntList(5,
          new ConsIntList(42,
              new ConsIntList(6, l0))));

  boolean testVariantA(Tester t) {
    return t.checkExpect(l0.variantA(), false) &&
        t.checkExpect(l1.variantA(), false) &&
        t.checkExpect(l2.variantA(), true) &&
        t.checkExpect(l4.variantA(), false);
  }

  boolean testVariantB(Tester t) {
    return t.checkExpect(l0.variantB(), false) &&
        t.checkExpect(l1.variantB(), false) &&
        t.checkExpect(l2.variantB(), false) &&
        t.checkExpect(l3.variantB(), true);
  }

  boolean testVariantC(Tester t) {
    return t.checkExpect(l0.variantC(), false) &&
        t.checkExpect(l1.variantC(), false) &&
        t.checkExpect(l2.variantC(), false) &&
        t.checkExpect(l3.variantC(), true) &&
        t.checkExpect(l5.variantC(), false);
  }
}