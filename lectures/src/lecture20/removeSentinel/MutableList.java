// Remove the sentinel and modify the solution accordingly, per the last exercise

package lecture20.removeSentinel;
import lecture20.Person;

abstract class APersonList {
  abstract void removePersonHelp(String name, ConsLoPerson prev);
  APersonList() { } // nothing to do here

  abstract public boolean firstNameEquals(String name);

  // Access the rest of the list "safely" when we only know it's an APersonList
  abstract public APersonList restList();

  abstract public void removePersonOptionOne(String name);

  abstract public APersonList removePersonOptionTwo(String name);
}

// Represents the empty end of the list
class MtLoPerson extends APersonList {
  MtLoPerson() { } // nothing to do

  public boolean firstNameEquals(String name) {
    return false;
  }

  public APersonList restList() { return this; }

  public void removePersonOptionOne(String name) { return; }

  public APersonList removePersonOptionTwo(String name) {
    return this;
  }

  void removePersonHelp(String name, ConsLoPerson prev) { return; }
}

// Represents a data node in the list
class ConsLoPerson extends APersonList {
  Person data;
  APersonList rest;
  ConsLoPerson(Person data, APersonList rest) {
    this.rest = rest;
    this.data = data;
  }
  void removePersonHelp(String name, ConsLoPerson prev) {
    if (this.data.name.equals(name)) {
      prev.rest = this.rest;
    }
    else {
      this.rest.removePersonHelp(name, this);
    }
  }

  public boolean firstNameEquals(String name) {
    return data.name.equals(name);
  }

  public APersonList restList() {
    return this.rest;
  }

  public void removePersonOptionOne(String name) {
    rest.removePersonHelp(name, this);
  }

  public APersonList removePersonOptionTwo(String name) {
    if (data.name.equals(name)) {
      return rest;
    } else {
      return new ConsLoPerson(data, rest.removePersonOptionTwo(name));
    }
  }
}

class MutablePersonList {
  APersonList list;

  MutablePersonList() {
    this.list = new MtLoPerson();
  }

  // Handle the edge case where the person to remove is the first in the list directly inside
  // MutablePersonList
  void removePersonOptionOne(String name) {
    if (this.list.firstNameEquals(name)) {
      this.list = this.list.restList();
    } else {
      // We can assume that the person is not in the first node
      this.list.removePersonOptionOne(name);
    }
  }

  // No mutation on the actual list - returns a new list with the person removed,
  // which we then set to the this.list field.
  void removePersonOptionTwo(String name) {
    this.list = this.list.removePersonOptionTwo(name);
  }

  void addPerson(String name, int phoneNum) {
    this.list = new ConsLoPerson(new Person(name, phoneNum), this.list);
  }
}
