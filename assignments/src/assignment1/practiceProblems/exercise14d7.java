package practiceProblems;

interface IGroceryItem {
  int unitPrice();
  boolean lowerUnitPrice(int amount);
  boolean cheaperThan(IGroceryItem that);
}

class IceCream implements IGroceryItem {
  String brandName;
  int weight;
  int price;
  String flavor;

  public IceCream(String brandName, int weight, int price, String flavor) {
    this.brandName = brandName;
    this.weight = weight;
    this.price = price;
    this.flavor = flavor;
  }

  public int unitPrice() {
    return this.price * this.weight;
  }

  public boolean lowerUnitPrice(int amount) {
    return this.unitPrice() < amount;
  }

  public boolean cheaperThan(IGroceryItem that) {
    return this.unitPrice() < that.unitPrice();
  }
}

class CoffeeItem implements IGroceryItem {
  String brandName;
  int weight;
  int price;
  boolean isDecaff;

  public CoffeeItem(String brandName, int weight, int price, boolean isDecaff) {
    this.brandName = brandName;
    this.weight = weight;
    this.price = price;
    this.isDecaff = isDecaff;
  }

  public int unitPrice() {
    return this.price * this.weight;
  }

  public boolean lowerUnitPrice(int amount) {
    return this.unitPrice() < amount;
  }

  public boolean cheaperThan(IGroceryItem that) {
    return this.unitPrice() < that.unitPrice();
  }
}

class Juice implements IGroceryItem {
  String brandName;
  int weight;
  int price;
  String flavor;
  String packaging;

  public Juice(String brandName, int weight, int price, String flavor, String packaging) {
    this.brandName = brandName;
    this.weight = weight;
    this.price = price;
    this.flavor = flavor;
    this.packaging = packaging;
  }

  public int unitPrice() {
    return this.price * this.weight;
  }

  public boolean lowerUnitPrice(int amount) {
    return this.unitPrice() < amount;
  }

  public boolean cheaperThan(IGroceryItem that) {
    return this.unitPrice() < that.unitPrice();
  }
}

public class exercise14d7 {
}
