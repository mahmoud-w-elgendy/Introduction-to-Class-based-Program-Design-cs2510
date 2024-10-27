package assignment1.problem2;
/*
                ┌───────────┐
      ┌─────────┤ IIceCream ├────┐
      │         └───────────┘    │
      │                          │
      ▼                          ▼
┌──────────────┐        ┌────────────────┐
│ EmptyServing │        │    Scooped     │
|──────────────|        |────────────────|
│ bool cone    │        │ IIceCream more │
└──────────────┘        │ String flavor  │
                        └────────────────┘
 */

interface IIceCream {}

class EmptyServing implements IIceCream {
  boolean cone;

  EmptyServing(boolean cone) {
    this.cone = cone;
  }
}

class Scooped implements IIceCream {
  IIceCream more;
  String flavor;

  public Scooped(IIceCream more, String flavor) {
    this.more = more;
    this.flavor = flavor;
  }
}

class ExamplesIcecream {
  IIceCream ic1 = new EmptyServing(false);
  IIceCream ic2 = new Scooped(this.ic1, "mint chip");
  IIceCream ic3 = new Scooped(this.ic2, "coffee");
  IIceCream ic4 = new Scooped(this.ic3, "black raspberry");
  IIceCream order1 = new Scooped(this.ic4, "caramel swirl");

  IIceCream ic5 = new EmptyServing(true);
  IIceCream ic6 = new Scooped(this.ic5, "chocolate");
  IIceCream ic7 = new Scooped(this.ic6, "vanilla");
  IIceCream order2 = new Scooped(this.ic7, "strawberry");
}