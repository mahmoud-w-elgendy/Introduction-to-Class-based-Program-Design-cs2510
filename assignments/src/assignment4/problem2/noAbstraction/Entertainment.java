// The non-abstracted code, after completing the methods


package assignment4.problem2.noAbstraction;

import tester.*;

interface IEntertainment {
  //compute the total price of this Entertainment
  double totalPrice();
  //computes the minutes of entertainment of this IEntertainment
  int duration();
  //produce a String that shows the name and price of this IEntertainment
  String format();
  //is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);
  boolean sameMagazine(Magazine that);
  boolean sameTVSeries(TVSeries that);
  boolean samePodcast(Podcast that);
}

class Magazine implements IEntertainment {
  String name;
  double price; //represents price per issue
  String genre;
  int pages;
  int installments; //number of issues per year
  int MINUTES_PER_UNIT = 5;

  Magazine(String name, double price, String genre, int pages, int installments) {
    this.name = name;
    this.price = price;
    this.genre = genre;
    this.pages = pages;
    this.installments = installments;
  }

  //computes the price of a yearly subscription to this Magazine
  public double totalPrice() {
    return this.price * this.installments;
  }

  //computes the minutes of entertainment of this Magazine, (includes all installments)
  public int duration() {
    return MINUTES_PER_UNIT * pages * installments;
  }

  //is this Magazine the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMagazine(this);
  }

  public boolean sameMagazine(Magazine that) {
    return name.equals(that.name) && (Math.abs(price - that.price) <= .0001) &&
        genre.equals(that.genre) && pages == that.pages && installments == that.installments;
  }

  public boolean sameTVSeries(TVSeries that) {
    return false;
  }

  public boolean samePodcast(Podcast that) {
    return false;
  }

  //produce a String that shows the name and price of this Magazine
  public String format() {
    return String.format("%s, %s.", name, price);
  }
}

class TVSeries implements IEntertainment {
  String name;
  double price; //represents price per episode
  int installments; //number of episodes of this series
  String corporation;
  int MINUTES_PER_UNIT = 50;

  TVSeries(String name, double price, int installments, String corporation) {
    this.name = name;
    this.price = price;
    this.installments = installments;
    this.corporation = corporation;
  }

  //computes the price of a yearly subscription to this TVSeries
  public double totalPrice() {
    return this.price * this.installments;
  }

  //computes the minutes of entertainment of this TVSeries
  public int duration() {
    return MINUTES_PER_UNIT * installments;
  }

  //is this TVSeries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVSeries(this);
  }

  public boolean sameMagazine(Magazine that) {
    return false;
  }

  public boolean sameTVSeries(TVSeries that) {
    return name.equals(that.name) && (Math.abs(price - that.price) <= .0001) &&
        installments == that.installments && corporation.equals(that.corporation);
  }

  public boolean samePodcast(Podcast that) {
    return false;
  }

  //produce a String that shows the name and price of this TVSeries
  public String format() {
    return String.format("%s, %s.", name, price);
  }
}

class Podcast implements IEntertainment {
  String name;
  double price; //represents price per issue
  int installments; //number of episodes in this Podcast
  int MINUTES_PER_UNIT = 50;

  Podcast(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }

  //computes the price of a yearly subscription to this Podcast
  public double totalPrice() {
    return this.price * this.installments;
  }

  //computes the minutes of entertainment of this Podcast
  public int duration() {
    return MINUTES_PER_UNIT * installments;
  }

  //is this Podcast the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }

  public boolean sameMagazine(Magazine that) {
    return false;
  }

  public boolean sameTVSeries(TVSeries that) {
    return false;
  }

  public boolean samePodcast(Podcast that) {
    return name.equals(that.name) && (Math.abs(price - that.price) <= .0001) &&
        installments == that.installments;
  }

  //produce a String that shows the name and price of this Podcast
  public String format() {
    return String.format("%s, %s.", name, price);
  }
}

class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);

  IEntertainment elle = new Magazine("Elle", 2.0, "Fashion", 80, 12);
  IEntertainment bestians = new TVSeries("Bestians", 4.5, 3, "NoOne");
  IEntertainment scieneNews = new Podcast("Science News", 0.0, 20);

  //testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55*12, .0001)
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25*13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001) &&
        t.checkInexact(this.elle.totalPrice(), 2.0*12, .0001) &&
        t.checkInexact(this.bestians.totalPrice(), 4.5*3, .0001) &&
        t.checkInexact(this.scieneNews.totalPrice(), 0.0, .0001);
  }

  boolean testDuration(Tester t) {
    return t.checkExpect(rollingStone.duration(), 5*60*12) &&
        t.checkExpect(houseOfCards.duration(), 50*13) &&
        t.checkExpect(serial.duration(), 50*8);
  }

  boolean testFormat(Tester t) {
    return t.checkExpect(rollingStone.format(), "Rolling Stone, 2.55.") &&
        t.checkExpect(houseOfCards.format(), "House of Cards, 5.25.") &&
        t.checkExpect(serial.format(), "Serial, 0.0.");
  }

  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(rollingStone.sameEntertainment(rollingStone), true) &&
        t.checkExpect(rollingStone.sameEntertainment(elle), false) &&
        t.checkExpect(houseOfCards.sameEntertainment(rollingStone), false) &&
        t.checkExpect(houseOfCards.sameEntertainment(houseOfCards), true) &&
        t.checkExpect(houseOfCards.sameEntertainment(bestians), false) &&
        t.checkExpect(serial.sameEntertainment(serial), true) &&
        t.checkExpect(serial.sameEntertainment(houseOfCards), false) &&
        t.checkExpect(serial.sameEntertainment(scieneNews), false) &&
        t.checkExpect(scieneNews.sameEntertainment(new Podcast("Science News", 0.0, 3)), false);
  }

}