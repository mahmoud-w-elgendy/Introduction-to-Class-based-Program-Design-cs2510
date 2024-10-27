package lecture20;

public class Person {
  public String name;
  public int phone;
  public Person(String name, int phone) {
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