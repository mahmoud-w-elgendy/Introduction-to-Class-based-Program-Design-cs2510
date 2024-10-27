package lab1;

interface IATNode {}

class UnknownTree implements IATNode {};

class Tree implements IATNode {
  Person person;
  IATNode leftNode;
  IATNode rightNode;

  Tree(Person person, IATNode leftNode, IATNode rightNode) {
    this.person = person;
    this.leftNode = leftNode;
    this.rightNode = rightNode;
  }
}


// The examples class
class ExamplesAncestors {
  Address bostonMA = new Address("Boston", "MA");
  Address warwickRI = new Address("Warwick", "RI");
  Person sarah = new Person("Sarah", 40, "Female", warwickRI);
  Person tim = new Person("Tim", 23, "Male", bostonMA);
  Person kate = new Person("Kate", 22, "Female", warwickRI);
  Person lola = new Person("Lola", 2, "Female", bostonMA);

  IATNode sarahTree = new Tree(sarah, new UnknownTree(), new UnknownTree());
  IATNode timTree = new Tree(tim, sarahTree, new UnknownTree());
  IATNode kateTree = new Tree(kate, new UnknownTree(), sarahTree);
  IATNode lolaTree = new Tree(lola, new UnknownTree(), timTree);
}
