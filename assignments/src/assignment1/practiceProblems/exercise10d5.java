package practiceProblems;

import tester.Tester;

// represent a coffee sale:
// at which price how much coffee was sold
class Coffee {
  String kind;
  int price;
  int weight;

  Coffee(String kind, int price, int weight) {
    this.kind = kind;
    this.price = price;
    this.weight = weight;
  }

  // to compute the total cost
  // of this coffee purchase
  int cost() {
    int discount;
    if (this.weight < 5000) {
      discount = 0;
    } else if (this.weight > 5000 && this.weight < 20000) {
      discount = 10;
    } else {
      discount = 25;
    }

    return this.price * this.weight * (100 - discount) / 100;
  }
}

// collect examples of coffee sales
public class exercise10d5 {
  Coffee kona =
      new Coffee("Kona",2095,100);
  Coffee ethi =
      new Coffee("Ethiopian", 800, 6000);
  Coffee colo =
      new Coffee("Colombian", 950, 21000);

  boolean testCoffee(Tester t) {
    return t.checkExpect(this.kona.cost(), 209500) &&
        t.checkExpect(this.ethi.cost(), 4320000) &&
        t.checkExpect(this.colo.cost(), 14962500);
  }
}
