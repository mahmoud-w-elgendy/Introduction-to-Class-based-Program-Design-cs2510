package lecture6and7;

import tester.Tester;

interface IAT {
  // To compute the number of known ancestors of this ancestor tree
  // (excluding this ancestor tree itself)
  int count();
  int countHelp();
  // To compute how many ancestors of this ancestor tree (excluding this
  // ancestor tree itself) are women older than 40 (in the current year)?
  int femaleAncOver40();
  int femaleAncOver40Helper();

  int numTotalGens();
  int numTotalGensHelp();
  int numPartialGens();
  int numPartialGensHelp();
  boolean isKnown();
  // To return the younger of this ancestor tree and the given ancestor tree
  IAT youngerIAT(IAT other);
  // To return either this ancestor tree (if this ancestor tree is younger
  // than the given yob) or the given ancestry tree
  IAT youngerIATHelp(IAT other, int otherYob);

  // To compute the youngest ancestor in the given generation of this ancestry tree
  IAT youngestAncInGen(int gen);

  // To compute the youngest grandparent
  IAT youngestGrandparent();
}
class Unknown implements IAT {
  Unknown() { }

  public int count() {
    return 0;
  }

  public int countHelp() {
    return 0;
  }

  public int femaleAncOver40() {
    return 0;
  }

  public int femaleAncOver40Helper() {
    return 0;
  }

  public int numTotalGens() {
    return 0;
  }

  public int numTotalGensHelp() {
    return 0;
  }

  public int numPartialGens() {
    return 0;
  }

  public int numPartialGensHelp() {
    return 0;
  }

  public boolean isKnown() {
    return false;
  }

  public IAT youngerIAT(IAT other) { return other; }
  // To return either this Unknown (if this Unknown is younger than the
  // given yob) or the given ancestry tree
  public IAT youngerIATHelp(IAT other, int otherYob) { return other; }

  // To compute the youngest ancestor in the given generation of this Unknown
  public IAT youngestAncInGen(int gen) {
    if (gen == 0) {
      return this;
    }
    else {
      return new Unknown();
    }
  }

  public IAT youngestGrandparent() {
    return this.youngestAncInGen(2);
  }
}
class Person implements IAT {
  String name;
  int yob;
  boolean isMale;
  IAT mom;
  IAT dad;
  Person(String name, int yob, boolean isMale, IAT mom, IAT dad) {
    this.name = name;
    this.yob = yob;
    this.isMale = isMale;
    this.mom = mom;
    this.dad = dad;
  }

  public int count() {
    return this.mom.countHelp() + this.dad.countHelp();
  }

  public int countHelp() {
    return 1 + this.mom.countHelp() + this.dad.countHelp();
  }

  public int femaleAncOver40() {
    return this.mom.femaleAncOver40Helper() + this.dad.femaleAncOver40Helper();
  }

  public int femaleAncOver40Helper() {
    int current = 0;
    if (!this.isMale && 2015 - this.yob > 40) {
      current = 1;
    }
    return current + this.mom.femaleAncOver40Helper() + this.dad.femaleAncOver40Helper();
  }

  public int numTotalGens() {
    return 1 + this.mom.numTotalGensHelp() + this.dad.numTotalGensHelp();
  }

  public int numTotalGensHelp() {
    if (this.mom.isKnown() && this.dad.isKnown()) {
      return 1 + this.mom.numTotalGensHelp() + this.dad.numTotalGensHelp();
    }
    return 0;
  }

  public int numPartialGens() {
    return 1 + this.mom.numPartialGensHelp() + this.dad.numPartialGensHelp();
  }

  public int numPartialGensHelp() {
    if (this.mom.isKnown() || this.dad.isKnown()) {
      return 1 + this.mom.numPartialGensHelp() + this.dad.numPartialGensHelp();
    }
    return 0;
  }

  public boolean isKnown() {
    return true;
  }

  // To return the younger of this Person and the given ancestor tree
  public IAT youngerIAT(IAT other) {
    /* Template
     * Fields:
     * this.yob -- int
     * ... others as before
     * Methods:
     * this.youngerIAT(IAT) -- IAT
     * this.youngerIATHelp(IAT, int) -- IAT
     * ... others as before
     * Methods on fields
     * ... others as before
     * Parameters
     * other -- IAT
     * Methods on parameters
     * other.youngerIAT(IAT) -- IAT
     * other.youngerIATHelp(IAT, int) -- IAT
     */
    return other.youngerIATHelp(this, this.yob);
  }
  // To return either this Person (if this Person is younger than the
  // given yob) or the given ancestry tree
  public IAT youngerIATHelp(IAT other, int otherYob) {
    /* same template as above */
    if (this.yob > otherYob) {
      return this;
    }
    else {
      return other;
    }
  }

  // To compute the youngest ancestor in the given generation of this Person
  public IAT youngestAncInGen(int gen) {
    /* Template:
     * Fields:
     * this.mom -- IAT
     * this.dad -- IAT
     * ... others as before
     * Methods:
     * this.youngestAncInGen(int gen) -- IAT
     * this.youngerIAT(IAT other) --- IAT
     * this.youngerIATHelp(IAT other, int otherYob) --- IAT
     * Methods of fields:
     * this.mom.youngestAncInGen(int gen) -- IAT
     * this.mom.youngerIAT(IAT other) --- IAT
     * this.mom.youngerIATHelp(IAT other, int otherYob) --- IAT
     * this.dad.youngestAncInGen(int gen) -- IAT
     * this.dad.youngerIAT(IAT other) --- IAT
     * this.dad.youngerIATHelp(IAT other, int otherYob) --- IAT
     * Parameters:
     * gen -- int
     */
    if (gen == 0) {
      return this;
    }
    else {
      return this.mom.youngestAncInGen(gen - 1).youngerIAT(this.dad.youngestAncInGen(gen - 1));
    }
  }

  public IAT youngestGrandparent() {
    return this.youngestAncInGen(2);
  }
}


interface ILoString {
  ILoString appendDirect(String item);
  ILoString appendAcc(String item);
  ILoString appendAccHelp(String item, ILoString los);
  boolean isEmpty();
}

class ConsLoString implements ILoString {
  String first;
  ILoString rest;
  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoString appendDirect(String item) {
    return new ConsLoString(this.first, this.rest.appendDirect(item));
  }

  public ILoString appendAcc(String item) {
    return this.appendAccHelp(item, new MtLoString());
  }

  public ILoString appendAccHelp(String item, ILoString los) {
    return this.rest.appendAccHelp(item, new ConsLoString(this.first, los));
  }

  public boolean isEmpty() {
    return false;
  }
}

class MtLoString implements ILoString {
  MtLoString() { }

  public ILoString appendDirect(String item) {
    return new ConsLoString(item, this);
  }

  public ILoString appendAcc(String item) {
    return this.appendAccHelp(item, this);
  }

  public ILoString appendAccHelp(String item, ILoString los) {
    return new ConsLoString(item, los);
  }

  public boolean isEmpty() {
    return true;
  }
}



/*
// To compute whether this ancestor tree is well-formed: are all known
// people younger than their parents?
boolean wellFormed();

// To compute the names of all the known ancestors in this ancestor tree
// (including this ancestor tree itself)
ILoString ancNames();

// To compute this ancestor tree's youngest grandparent
IAT youngestGrandparent();


 */

class ExamplesIAT {
  IAT enid = new Person("Enid", 1904, false, new Unknown(), new Unknown());
  IAT edward = new Person("Edward", 1902, true, new Unknown(), new Unknown());
  IAT emma = new Person("Emma", 1906, false, new Unknown(), new Unknown());
  IAT eustace = new Person("Eustace", 1907, true, new Unknown(), new Unknown());

  IAT david = new Person("David", 1925, true, new Unknown(), this.edward);
  IAT daisy = new Person("Daisy", 1927, false, new Unknown(), new Unknown());
  IAT dana = new Person("Dana", 1933, false, new Unknown(), new Unknown());
  IAT darcy = new Person("Darcy", 1930, false, this.emma, this.eustace);
  IAT darren = new Person("Darren", 1935, true, this.enid, new Unknown());
  IAT dixon = new Person("Dixon", 1936, true, new Unknown(), new Unknown());

  IAT clyde = new Person("Clyde", 1955, true, this.daisy, this.david);
  IAT candace = new Person("Candace", 1960, false, this.dana, this.darren);
  IAT cameron = new Person("Cameron", 1959, true, new Unknown(), this.dixon);
  IAT claire = new Person("Claire", 1956, false, this.darcy, new Unknown());

  IAT bill = new Person("Bill", 1980, true, this.candace, this.clyde);
  IAT bree = new Person("Bree", 1981, false, this.claire, this.cameron);

  IAT andrew = new Person("Andrew", 2001, true, this.bree, this.bill);

  ILoString stringList0 = new MtLoString();
  ILoString stringList1 = new ConsLoString("Cookie", stringList0);
  ILoString stringList2 = new ConsLoString("Cake", stringList1);
  ILoString stringList3 = new ConsLoString("Ice Cream", stringList2);
  ILoString getStringListAppendAcc = stringList3.appendAcc("Banana");


  boolean testCount(Tester t) {
    return
        t.checkExpect(this.andrew.count(), 16) &&
            t.checkExpect(this.david.count(), 1) &&
            t.checkExpect(this.enid.count(), 0) &&
            t.checkExpect(new Unknown().count(), 0);
  }
  boolean testFemaleAncOver40(Tester t) {
    return
        t.checkExpect(this.andrew.femaleAncOver40(), 7) &&
            t.checkExpect(this.bree.femaleAncOver40(), 3) &&
            t.checkExpect(this.darcy.femaleAncOver40(), 1) &&
            t.checkExpect(this.enid.femaleAncOver40(), 0) &&
            t.checkExpect(new Unknown().femaleAncOver40(), 0);
  }

  boolean testNumGens(Tester t) {
    return
        t.checkExpect(this.enid.numTotalGens(), 1) &&
            t.checkExpect(this.enid.numPartialGens(), 1) &&
            t.checkExpect(this.andrew.numTotalGens(), 5) &&
            t.checkExpect(this.andrew.numPartialGens(), 10) &&
            t.checkExpect(new Unknown().numTotalGens(), 0) &&
            t.checkExpect(new Unknown().numPartialGens(), 0);
  }

  boolean testAppend(Tester t) {
    return
    t.checkExpect(this.stringList0.appendDirect("Hi"), new ConsLoString("Hi", stringList0)) &&
        t.checkExpect(this.stringList2.appendDirect("Ello"), new ConsLoString("Cake",
            new ConsLoString("Cookie", new ConsLoString("Ello", stringList0))));
  }
  boolean testYoungestGrandparent(Tester t) {
    return
        t.checkExpect(this.emma.youngestGrandparent(), new Unknown()) &&
            t.checkExpect(this.david.youngestGrandparent(), new Unknown()) &&
            t.checkExpect(this.claire.youngestGrandparent(), this.eustace) &&
            t.checkExpect(this.bree.youngestGrandparent(), this.dixon) &&
            t.checkExpect(this.andrew.youngestGrandparent(), this.candace) &&
            t.checkExpect(new Unknown().youngestGrandparent(), new Unknown());
  }
}