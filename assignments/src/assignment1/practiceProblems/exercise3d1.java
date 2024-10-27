package practiceProblems;

class House {
  String kind;
  int rooms;
  Address address;
  int price; // In dollars

  House(String kind, int rooms, Address address, int price) {
    this.kind = kind;
    this.rooms = rooms;
    this.address = address;
    this.price = price;
  }
}

class Address {
  int streetNumber;
  String streetName;
  String city;

  Address(int streetNumber, String streetName, String city) {
    this.streetNumber = streetNumber;
    this.streetName = streetName;
    this.city = city;
  }
}

// exercise 5.3
interface IHListing {}

class MtHListing implements IHListing {}

class ConsHListing implements IHListing {
  House first;
  IHListing rest;

  public ConsHListing(House first, IHListing rest) {
    this.first = first;
    this.rest = rest;
  }
}

public class exercise3d1 {
  Address a1 = new Address(23, "Maple Street", "Brookline");
  Address a2 = new Address(5, "Joye Road", "Newton");
  Address a3 = new Address(83, "Winslow Road", "Waltham");

  House h1 = new House("Ranch", 7, a1, 375000);
  House h2 = new House("Colonial", 9, a2, 450000);
  House h3 = new House("Cape", 6, a3, 235000);

  IHListing hl1 = new MtHListing();
  IHListing hl2 = new ConsHListing(h1, hl1);
  IHListing hl3 = new ConsHListing(h2, hl2);
  IHListing hl4 = new ConsHListing(h3, hl3);
}

