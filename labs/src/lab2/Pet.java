package lab2;

import tester.Tester;


interface IPerson {
  // is this Person older than the given Person?
  boolean isOlder(Owner other);
  boolean isYoungerThan(int age);

  // does the name of this person's pet match the given name?
  boolean sameNamePet(String petName);
}

// to represent a pet owner
class Owner implements IPerson {
  String name;
  IPet pet;
  int age;

  Owner(String name, IPet pet, int age) {
    this.name = name;
    this.pet = pet;
    this.age = age;
  }

  // is this Person older than the given Person?
  public boolean isOlder(Owner other) {
    return other.isYoungerThan(this.age);
  }

  public boolean isYoungerThan(int age) {
    return this.age < age;
  }

  // does the name of this person's pet match the given name?
  public boolean sameNamePet(String petName) {
    return pet.sameName(petName);
  }

  NoPet perish() {
    return new NoPet(name, age);
  }
}

class NoPet implements IPerson {
  String name;
  int age;

  public NoPet(String name, int age) {
    this.name = name;
    this.age = age;
  }

  // is this Person older than the given Person?
  public boolean isOlder(Owner other) {
    return other.isYoungerThan(this.age);
  }

  public boolean isYoungerThan(int age) {
    return this.age < age;
  }

  public boolean sameNamePet(String petName) {
    return false;
  }
}

// to represent a pet
interface IPet {
  boolean sameName(String petName);
}

// to represent a pet cat
class Cat implements IPet {
  String name;
  String kind;
  boolean longhaired;

  Cat(String name, String kind, boolean longhaired) {
    this.name = name;
    this.kind = kind;
    this.longhaired = longhaired;
  }

  public boolean sameName(String petName) {
    return this.name.equals(petName);
  }
}

// to represent a pet dog
class Dog implements IPet {
  String name;
  String kind;
  boolean male;

  Dog(String name, String kind, boolean male) {
    this.name = name;
    this.kind = kind;
    this.male = male;
  }

  public boolean sameName(String petName) {
    return this.name.equals(petName);
  }
}


class ExamplesPet {
  IPet doggy = new Dog("Doggy", "Lavrador", true);
  IPet dogga = new Dog("Dogga", "Something", false);
  IPet catty = new Cat("Catty", "type1", true);
  IPet catnya = new Cat("Catnya", "type2", false);

  Owner bob = new Owner("Bob", doggy, 15);
  Owner alice = new Owner("Alice", dogga, 18);
  Owner lucy = new Owner("Lucy", catty, 24);
  Owner snog = new Owner("Snog", catnya, 30);

  NoPet dan = new NoPet("Dan", 45);
  NoPet sadBob = bob.perish();

  boolean testOlderThan(Tester t) {
    return t.checkExpect(bob.isOlder(alice), false) &&
        t.checkExpect(bob.isOlder(bob), false) &&
        t.checkExpect(snog.isOlder(lucy), true);
  }

  boolean testSameNamePet(Tester t) {
    return t.checkExpect(bob.sameNamePet("Doggy"), true) &&
        t.checkExpect(bob.sameNamePet("Cayy"), false);
  }
}