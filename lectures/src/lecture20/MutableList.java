package lecture20;

// Represents a sentinel at the start, a node in the middle,
// or the empty end of a list
abstract class APersonList {
  abstract void removePersonHelp(String name, ANode prev);
  APersonList() { } // nothing to do here
}

// Represents a node in a list that has some list after it
abstract class ANode extends APersonList {
  APersonList rest;
  ANode(APersonList rest) {
    this.rest = rest;
  }
}

// Represents the empty end of the list
class MtLoPerson extends APersonList {
  MtLoPerson() { } // nothing to do
  void removePersonHelp(String name, ANode prev) { return; }
}

// Represents a data node in the list
class ConsLoPerson extends ANode {
  Person data;
  ConsLoPerson(Person data, APersonList rest) {
    super(rest);
    this.data = data;
  }
  void removePersonHelp(String name, ANode prev) {
    if (this.data.name.equals(name)) {
      prev.rest = this.rest;
    }
    else {
      this.rest.removePersonHelp(name, this);
    }
  }
}

// Represents the dummy node before the first actual node of the list
class Sentinel extends ANode {
  Sentinel(APersonList rest) {
    super(rest);
  }
  void removePersonHelp(String name, ANode prev) {
    throw new RuntimeException("Can't try to remove on a Sentinel!");
  }
}

class MutablePersonList {
  Sentinel sentinel;
  MutablePersonList() {
    this.sentinel = new Sentinel(new MtLoPerson());
  }

  void removePerson(String name) {
    this.sentinel.rest.removePersonHelp(name, this.sentinel);
  }

  void addPerson(String name, int phoneNum) {
    this.sentinel.rest = new ConsLoPerson(new Person(name, phoneNum), this.sentinel.rest);
  }
}
