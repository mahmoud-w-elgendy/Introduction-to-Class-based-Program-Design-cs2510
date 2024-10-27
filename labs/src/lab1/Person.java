package lab1;

class Person {
  String name;
  int age;
  String gender;
  Address address;

  Person(String name, int age, String gender, Address address) {
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.address = address;
  }
}

class Address {
  String city;
  String state;

  Address(String city, String state) {
    this.city = city;
    this.state = state;
  }
}

class ExamplesPerson {
  Address bostonMA = new Address("Boston", "MA");
  Address warwickRI = new Address("Warwick", "RI");
  Address nashuaNH = new Address("Nashua", "NH");

  Person tim = new Person("Tim", 23, "Male", bostonMA);
  Person kate = new Person("Kate", 22, "Female", warwickRI);
  Person rebecca = new Person("Rebecca", 31, "Female", nashuaNH);
  Person rona = new Person("Rona", 31, "Female", bostonMA);
  Person lucy = new Person("Lucy", 19, "Female", nashuaNH);


}