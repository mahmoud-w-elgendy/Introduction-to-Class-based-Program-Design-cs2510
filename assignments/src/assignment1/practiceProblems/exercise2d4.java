package practiceProblems;

class Automobile {
  String model;
  int price;
  double mileage;
  boolean used;

  Automobile(String model, int price, double mileage, boolean used) {
    this.model = model;
    this.price = price;
    this.mileage = mileage;
    this.used = used;
  }
}

public class exercise2d4 {
  Automobile a1 = new Automobile("As3", 23000, 34.4, true);
  Automobile a2 = new Automobile("BsF3", 28000, 30.1, false);

}
