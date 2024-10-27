package lecture19;

import tester.Tester;

class Person {
  String name;
  int phone;
  Person(String name, int phone) {
    this.name = name;
    this.phone = phone;
  }
  // Returns true when the given person has the same name and phone number as this person
  boolean samePerson(Person that) {
    return this.name.equals(that.name) && this.phone == that.phone;
  }

  void changeNum(int newNum) {
    this.phone = newNum;
  }
}

// By making this interface and classes extend the respective generic classes,
// we avoid the need of having to recreate the examples later
interface ILoPerson extends IList<Person> {
  // Finds the person in this list with the given name and returns their phone number,
  // or -1 if no such person is found
  int findPhoneNum(String name);

  // Change the phone number for the person in this list with the given name
  void changePhone(String name, int newNum);
}

class MtLoPerson extends MtList<Person> implements ILoPerson {
  public int findPhoneNum(String name) { return -1; }
  public void changePhone(String name, int newNum) { return; }
}

class ConsLoPerson extends ConsList<Person> implements ILoPerson {
  Person first;
  ILoPerson rest;

  public ConsLoPerson(Person first, ILoPerson rest) {
    super(first, rest);
    this.first = first;
    this.rest = rest;
  }

  public int findPhoneNum(String name) {
    if (this.first.name.equals(name)) {
      return this.first.phone;
    }
    else {
      return this.rest.findPhoneNum(name);
    }
  }

  public void changePhone(String name, int newNum) {
    if (this.first.name.equals(name)) {
      this.first.changeNum(newNum); // do the update
    }
    else {
      this.rest.changePhone(name, newNum); // keep searching the rest of the list
    }

  }
}

// Function object class for finding a Person by name
class findByName implements IPred<Person> {
  String toFind;

  public findByName(String toFind) {
    this.toFind = toFind;
  }

  public boolean apply(Person person) {
    return person.name.equals(toFind);
  }
}

// Function object class for modifying a phone number
class changeNumTo implements IFunc<Person, Void> {
  int newNum;

  public changeNumTo(int newNum) {
    this.newNum = newNum;
  }

  public Void apply(Person p) {
    p.phone = newNum;
    return null;
  }
}

class ExamplePhoneLists {
  Person anne, bob, clyde, dana, eric, frank, gail, henry, irene, jenny;

  ILoPerson friends, family, work;
  void initData() {
    anne = new Person("Anne", 1234);
    bob = new Person("Bob", 3456);
    clyde = new Person("Clyde", 6789);
    dana = new Person("Dana", 1357);
    eric = new Person("Eric", 12469);
    frank = new Person("Frank", 7294);
    gail = new Person("Gail", 9345);
    henry = new Person("Henry", 8602);
    irene = new Person("Irene", 91302);
    jenny = new Person("Jenny", 8675309);

    this.friends =
        new ConsLoPerson(this.anne, new ConsLoPerson(this.clyde,
            new ConsLoPerson(this.gail, new ConsLoPerson(this.frank,
                new ConsLoPerson(this.jenny, new MtLoPerson())))));
    this.family =
        new ConsLoPerson(this.anne, new ConsLoPerson(this.dana,
            new ConsLoPerson(this.frank, new MtLoPerson())));
    this.work =
        new ConsLoPerson(this.bob, new ConsLoPerson(this.clyde,
            new ConsLoPerson(this.dana, new ConsLoPerson(this.eric,
                new ConsLoPerson(this.henry, new ConsLoPerson(this.irene,
                    new MtLoPerson()))))));
  }

  void testFindPhoneNum(Tester t) {
    this.initData();
    t.checkExpect(this.friends.findPhoneNum("Frank"), 7294);
    t.checkExpect(this.family.findPhoneNum("Frank"),
        this.friends.findPhoneNum("Frank"));
    t.checkExpect(this.frank.phone, 7294);
    this.family.changePhone("Frank", 9021);
    t.checkExpect(this.friends.findPhoneNum("Frank"), 9021);
    t.checkExpect(this.family.findPhoneNum("Frank"),
        this.friends.findPhoneNum("Frank"));
    t.checkExpect(this.frank.phone, 9021);
  }

  void testChangeNum(Tester t) {
    this.initData();
    t.checkExpect(this.frank.phone, 7294);
    this.frank.changeNum(9021);
    t.checkExpect(this.frank.phone, 9021);
  }

  void testFindPhoneNumGeneric(Tester t) {
    this.initData();
    IPred<Person> findFrank = new findByName("Frank");

    t.checkExpect(this.friends.find(findFrank).phone, 7294);
    t.checkExpect(this.family.find(findFrank).phone,
        this.friends.find(findFrank).phone);
    t.checkExpect(this.frank.phone, 7294);
    this.family.find(findFrank, new changeNumTo(9021));
    t.checkExpect(this.friends.find(findFrank).phone, 9021);
    t.checkExpect(this.family.find(findFrank).phone,
        this.friends.find(findFrank).phone);
    t.checkExpect(this.frank.phone, 9021);
  }

  void testChangePhoneGeneric(Tester t) {
    this.initData();
    IPred<Person> findFrank = new findByName("Frank");

    t.checkExpect(this.frank.phone, 7294);
    this.friends.find(findFrank, new changeNumTo(9021));
    t.checkExpect(this.frank.phone, 9021);
  }
}