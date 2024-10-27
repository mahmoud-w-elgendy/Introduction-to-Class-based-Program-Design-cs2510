package assignment1.problem3;

interface IHousing {}
interface ITransportation {}

class Hut implements IHousing {
  int capacity;
  int population;

  public Hut(int capacity, int population) {
    this.capacity = capacity;
    this.population = population;
  }
}

class Inn implements IHousing {
  String name;
  int capacity;
  int population;
  int stalls;

  public Inn(String name, int capacity, int population, int stalls) {
    this.name = name;
    this.capacity = capacity;
    this.population = population;
    this.stalls = stalls;
  }
}

class Castle implements IHousing {
  String name;
  String familyName;
  int population;
  int carriagesCapacity;

  public Castle(String name, String familyName, int population, int carriagesCapacity) {
    this.name = name;
    this.familyName = familyName;
    this.population = population;
    this.carriagesCapacity = carriagesCapacity;
  }
}

class Horse implements ITransportation {
  IHousing from;
  IHousing to;
  String name;
  String color;

  public Horse(IHousing from, IHousing to, String name, String color) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.color = color;
  }
}

class Carriage implements ITransportation {
  IHousing from;
  IHousing to;
  int tonnageCapacity;

  public Carriage(IHousing from, IHousing to, int tonnageCapacity) {
    this.from = from;
    this.to = to;
    this.tonnageCapacity = tonnageCapacity;
  }
}

class ExamplesTravel {
  IHousing hovel = new Hut(5, 1);
  IHousing winterfell = new Castle("Winterfell", "Stark", 500, 6);
  IHousing crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);
  IHousing hut2 = new Hut(45, 40);
  IHousing castle2 = new Castle("Black", "Whitener", 350, 12);
  IHousing inn2 = new Inn("Innside", 100, 99, 20);

  ITransportation horse1 = new Horse(this.hovel, this.winterfell, "Horsy", "Gray");
  ITransportation horse2 = new Horse(this.winterfell, this.crossroads, "Horsa", "White");
  ITransportation carriage1 = new Carriage(this.crossroads, this.winterfell, 2);
  ITransportation carriage2 = new Carriage(this.inn2, this.castle2, 8);
}