package lab1;

interface IMenuItem {
}

class Soup implements IMenuItem {
  String name;
  int price;
  boolean isVegetarian;

  Soup(String name, int price, boolean isVegetarian) {
    this.name = name;
    this.price = price;
    this.isVegetarian = isVegetarian;
  }
}

class Salad implements IMenuItem {
  String name;
  int price;
  boolean isVegetarian;
  String dressing;

  Salad(String name, int price, boolean isVegetarian, String dressing) {
    this.name = name;
    this.price = price;
    this.isVegetarian = isVegetarian;
    this.dressing = dressing;
  }
}

class Sandwich implements IMenuItem {
  String name;
  int price;
  String bread;
  String filling1;
  String filling2;

  Sandwich(String name, int price, String bread, String filling1, String filling2) {
    this.name = name;
    this.price = price;
    this.bread = bread;
    this.filling1 = filling1;
    this.filling2 = filling2;
  }
}


// The Examples class
class ExamplesMenu {
  IMenuItem chickenSoup = new Soup("Chicken soup", 300, false);
  IMenuItem orangeSoup = new Soup("Orange soup", 340, true);

  IMenuItem eggAndPotatoSalad = new Salad("Egg and Potato lab1.Salad", 200, true, "mayonnaise");
  IMenuItem vegetablesSalad = new Salad("Vegetables lab1.Salad", 180, true, "Olive Oil");

  IMenuItem toastSandwich = new Sandwich("Toast lab1.Sandwich", 290, "Standard", "Yellow Cheese",
      "Ketchup");
  IMenuItem omeletteSandwich = new Sandwich("Omelette lab1.Sandwich", 300, "Spelt Sourdough",
      "Omelette",
      "Pesto");
}
