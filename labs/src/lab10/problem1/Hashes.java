package lab10.problem1;

import tester.Tester;

class Runner{
  int age;
  String name;

  Runner(int age, String name){
    this.age = age;
    this.name = name;
  }

  public boolean equals(Object o) {
    if ((!(o instanceof Runner))) return false;

    Runner that = (Runner) o;
    return this.age == that.age && this.name.equals(that.name);
  }

  public int hashCode() {
    return this.age * 10000 + this.name.hashCode();
  }
}


class ExamplesHashes {
  Runner r1 = new Runner(18, "Ran");
  Runner r1Dup = new Runner(18, "Ran");
  Runner r2 = new Runner(18, "Run");
  Runner r3 = new Runner(19, "Run");

  void testEquals(Tester t) {
    t.checkExpect(r1.equals(r1), true);
    t.checkExpect(r1.equals(r1Dup), true);
    t.checkExpect(r1.equals(r2), false);
    t.checkExpect(r2.equals(r3), false);
  }

  void testHashCode(Tester t) {
    t.checkExpect(r1.hashCode(), r1.hashCode());
    t.checkExpect(r1.hashCode(), r1Dup.hashCode());
    t.checkFail(r1.hashCode(), r2.hashCode());
    t.checkFail(r1.hashCode(), r2.hashCode());
  }
}


