package lab2;

import tester.Tester;

// Represents a mode of transportation
interface IMOT {
  // returns true if this mode of transportation is at least
  // as efficient as the given mpg, false otherwise
  boolean isMoreFuelEfficientThan(int mpg);

  boolean isMoreFuelEfficientThan(IMOT that);

}

// Represents a bicycle as a mode of transportation
class Bicycle implements IMOT {
  String brand;

  Bicycle(String brand) {
    this.brand = brand;
  }

  // a bicycle does not consume fuel, so it will always be more fuel efficient
  public boolean isMoreFuelEfficientThan(int mpg) {
    return true;
  }

  public boolean isMoreFuelEfficientThan(IMOT that) {
    return !that.isMoreFuelEfficientThan(this);
  }
}

// Represents a car as a mode of transportation
class Car implements IMOT {
  String make;
  int mpg; // represents the fuel efficiency in miles per gallon

  Car(String make, int mpg) {
    this.make = make;
    this.mpg = mpg;
  }

  // compare this car's fuel efficiency to the given fuel efficiency
  public boolean isMoreFuelEfficientThan(int mpg) {
    return this.mpg >= mpg;
  }

  public boolean isMoreFuelEfficientThan(IMOT that) {
    return !that.isMoreFuelEfficientThan(this.mpg);
  }
}

// Keeps track of how a person is transported
class Person {
  String name;
  IMOT mot;

  Person(String name, IMOT mot) {
    this.name = name;
    this.mot = mot;
  }

  boolean motMeetsFuelEfficiency(int mpg) {
    return this.mot.isMoreFuelEfficientThan(mpg);
  }

  boolean motIsMoreFuelEfficientThan(IMOT mot) {
    return this.mot.isMoreFuelEfficientThan(mot);
  }
}


class ExamplesPerson {
  IMOT diamondback = new Bicycle("Diamondback");
  IMOT toyota = new Car("Toyota", 30);
  IMOT lamborghini = new Car("Lamborghini", 17);

  Person bob = new Person("Bob", diamondback);
  Person ben = new Person("Ben", toyota);
  Person becca = new Person("Becca", lamborghini);

  boolean testMotMeetsFuelEfficiency(Tester t) {
    return t.checkExpect(bob.motMeetsFuelEfficiency(0), true) &&
        t.checkExpect(ben.motMeetsFuelEfficiency(20), true) &&
        t.checkExpect(becca.motMeetsFuelEfficiency(20), false) &&
        t.checkExpect(becca.motMeetsFuelEfficiency(15), true) &&
        t.checkExpect(becca.motMeetsFuelEfficiency(25), false);
  }

  boolean testMotMoreEfficientThan(Tester t) {
    return t.checkExpect(bob.motIsMoreFuelEfficientThan(toyota), true) &&
        t.checkExpect(ben.motIsMoreFuelEfficientThan(toyota), false) &&
        t.checkExpect(becca.motIsMoreFuelEfficientThan(diamondback), false);
  }
}