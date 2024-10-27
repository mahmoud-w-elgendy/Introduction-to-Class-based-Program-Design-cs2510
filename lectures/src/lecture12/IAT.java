package lecture12;

import tester.Tester;

interface IAT {
  boolean sameIAT(IAT that);

  boolean sameUnknown(Unknown that);

  boolean samePerson(Person person);
}
class Unknown implements IAT {
  Unknown() { }

  public boolean sameIAT(IAT that) {
    return that.sameUnknown(this);
  }

  public boolean sameUnknown(Unknown that) {
    return true;
  }

  public boolean samePerson(Person person) {
    return false;
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

  public boolean sameIAT(IAT that) {
    return that.samePerson(this);
  }

  public boolean sameUnknown(Unknown that) {
    return false;
  }

  public boolean samePerson(Person that) {
    return name.equals(that.name) && yob == that.yob && isMale == that.isMale &&
        mom.sameIAT(that.mom) && dad.sameIAT(that.dad);
  }
}


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
  boolean testSameIAT(Tester t) {
    return t.checkExpect(enid.sameIAT(new Unknown()), false) &&
        t.checkExpect(enid.sameIAT(enid), true) &&
        t.checkExpect(new Unknown().sameIAT(bill), false) &&
        t.checkExpect(bill.sameIAT(bill), true) &&
        t.checkExpect(new Unknown().sameIAT(new Unknown()), true);
  }

}