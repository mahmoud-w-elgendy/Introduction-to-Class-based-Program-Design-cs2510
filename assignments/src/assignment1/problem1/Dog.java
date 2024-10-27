package assignment1.problem1;

public class Dog {
  String name;
  String breed;
  int yob;
  String state;
  boolean hypoallergenic;

  public Dog(String name, String breed, int yob, String state, boolean hypoallergenic) {
    this.name = name;
    this.breed = breed;
    this.yob = yob;
    this.state = state;
    this.hypoallergenic = hypoallergenic;
  }
}

class ExamplesDog {
  Dog huffle = new Dog("Hufflepuff", "Wheaten Terrier", 2012, "TX", true);
  Dog Pearl = new Dog("Pearl", "Labrador Retriever", 2016, "MA", false);
  Dog hufflePearl = new Dog("HufflePearl", "Wheaten Retriever", 2021, "TA", false);
}

