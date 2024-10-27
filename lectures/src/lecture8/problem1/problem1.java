package lecture8.problem1;

import tester.Tester;

interface IDocument {
  // produce a properly formatted bibliography
  //
  String formatted();

  IBibliography getBibliography();

  boolean isBefore(IDocument that);
  boolean isBeforeHelper(String lastName, String firstName, String title);

  boolean isWArticle();
}

interface IBibliography {
  IFormattedBibliography format();

  IFormattedBibliography convertEachToFormatted();
  IBibliography flatten();
  IBibliography removeWArticles();
  IBibliography sort();
  IBibliography removeDuplicates();

  IBibliography join(IBibliography that);

  IBibliography insertSort(IDocument first);

  boolean contains(IDocument doc);
}
interface IFormattedBibliography {}


class Book implements IDocument {
  String authorFirstName;
  String authorLastName;
  String title;
  IBibliography bibliography;
  String publisher;

  public Book(String authorFirstName, String authorLastName, String title,
      IBibliography bibliography, String publisher) {
    this.authorFirstName = authorFirstName;
    this.authorLastName = authorLastName;
    this.title = title;
    this.bibliography = bibliography;
    this.publisher = publisher;
  }

  public String formatted() {
    return String.format("%s, %s. \"%s\".", this.authorLastName, this.authorFirstName,
        this.title);
  }

  public IBibliography getBibliography() {
    return this.bibliography;
  }

  public boolean isBefore(IDocument that) {
    return that.isBeforeHelper(this.authorLastName, this.authorFirstName, this.title);
  }

  public boolean isBeforeHelper(String lastName, String firstName, String title) {
    int lastNameComparison = lastName.compareTo(this.authorLastName);
    if (lastNameComparison != 0) return lastNameComparison < 0;

    int firstNameComparison = firstName.compareTo(this.authorFirstName);
    if (firstNameComparison != 0) return firstNameComparison < 0;

    return title.compareTo(this.title) < 0;
  }

  public boolean isWArticle() {
    return false;
  }

}

class WArticle implements IDocument {
  String authorFirstName;
  String authorLastName;
  String title;
  IBibliography bibliography;
  String url;

  public WArticle(String authorFirstName, String authorLastName, String title,
      IBibliography bibliography, String url) {
    this.authorFirstName = authorFirstName;
    this.authorLastName = authorLastName;
    this.title = title;
    this.bibliography = bibliography;
    this.url = url;
  }

  public String formatted() {
    return String.format("%s, %s. \"%s\".", this.authorLastName, this.authorFirstName,
        this.title);
  }

  public IBibliography getBibliography() {
    return this.bibliography;
  }

  public boolean isBefore(IDocument that) {
    return that.isBeforeHelper(this.authorLastName, this.authorFirstName, this.title);
  }

  public boolean isBeforeHelper(String lastName, String firstName, String title) {
    int lastNameComparison = lastName.compareTo(this.authorLastName);
    if (lastNameComparison != 0) return lastNameComparison < 0;

    int firstNameComparison = firstName.compareTo(this.authorFirstName);
    if (firstNameComparison != 0) return firstNameComparison < 0;

    return title.compareTo(this.title) < 0;
  }

  public boolean isWArticle() {
    return true;
  }
}



class MtBibliography implements IBibliography {
  public IFormattedBibliography format() {
    return this.flatten().removeWArticles().removeDuplicates().sort().convertEachToFormatted();
  }

  public IFormattedBibliography convertEachToFormatted() {
    return new MtFormattedBibliography();
  }

  public IBibliography flatten() {
    return this;
  }

  public IBibliography removeWArticles() {
    return this;
  }

  public IBibliography sort() {
    return this;
  }

  public IBibliography removeDuplicates() {
    return this;
  }

  public IBibliography join(IBibliography that) {
    return that;
  }

  public IBibliography insertSort(IDocument first) {
    return new ConsBibliography(first, this);
  }

  public boolean contains(IDocument doc) {
    return false;
  }
}

class ConsBibliography implements IBibliography {
  IDocument first;
  IBibliography rest;

  public ConsBibliography(IDocument first, IBibliography rest) {
    this.first = first;
    this.rest = rest;
  }

  public IFormattedBibliography format() {
    return this.flatten().removeWArticles().removeDuplicates().sort().convertEachToFormatted();
  }

  public IFormattedBibliography convertEachToFormatted() {
    return new ConsFormattedBibliography(this.first.formatted(),
        this.rest.convertEachToFormatted());
  }

  public IBibliography flatten() {
    return new ConsBibliography(this.first,
        this.first.getBibliography().flatten().join(this.rest.flatten()));
  }

  public IBibliography removeWArticles() {
    if (this.first.isWArticle()) {
      return this.rest.removeWArticles();
    } else {
      return new ConsBibliography(this.first, this.rest.removeWArticles());
    }
  }

  public IBibliography sort() {

    return this.rest.sort().insertSort(this.first);
  }

  public IBibliography removeDuplicates() {
    if (this.rest.contains(this.first)) {
      return this.rest.removeDuplicates();
    } else {
      return new ConsBibliography(this.first, this.rest.removeDuplicates());
    }
  }

  public IBibliography join(IBibliography that) {
    return new ConsBibliography(this.first, this.rest.join(that));
  }

  public IBibliography insertSort(IDocument first) {
    if (first.isBefore(this.first)) {
      return new ConsBibliography(first, this);
    } else {
      return new ConsBibliography(this.first, this.rest.insertSort(first));
    }
  }

  public boolean contains(IDocument doc) {
    if (this.first == doc) {
      return true;
    } else {
      return this.rest.contains(doc);
    }
  }
}

/*
  From what I could gather from the problem description, it is not clear
  whether a Bibliography is a proper data definition with a reference to a document,
  or if it is just a list of books formatted according to the specifications,
  so I decided to do both, so that the formatted bibliography includes only the strings,
  and is produced only at the end of the process.
 */
class MtFormattedBibliography implements IFormattedBibliography {

}

class ConsFormattedBibliography implements IFormattedBibliography {
  String first;
  IFormattedBibliography rest;

  public ConsFormattedBibliography(String first, IFormattedBibliography rest) {
    this.first = first;
    this.rest = rest;
  }
}

class ExamplesFormatting {
  IBibliography bil0 = new MtBibliography();
  IDocument book1 = new Book("Brandon", "Sanderson", "Warbreaker", bil0, "Random House");
  IDocument book2 = new Book("Brandon", "Sanderson", "Elantris", bil0, "Random House");
  IDocument book3 = new Book("Brandon", "Sanderson", "Mistborn: The Final Empire", bil0,
      "Random House");
  IDocument book4 = new Book("Robert", "Jordan", "The Wheel Of Time: Book 1", bil0, "Dunno");

  IBibliography bil1 = new ConsBibliography(book1, bil0);
  IBibliography bil2 = new ConsBibliography(book2, bil1);
  IBibliography bil3 = new ConsBibliography(book3, bil2);

  IDocument wa1 = new WArticle("Someone", "Random", "The magic systems of the Cosmere", bil3,
      "cosmere-fandom.com/magic-systems");
  IDocument wa2 = new WArticle("no", "one", "About Brandon Sanderson", bil0,
      "cosmere-fandom.com/about-brandon");
  IBibliography bil4 = new ConsBibliography(wa1,
      new ConsBibliography(wa2, new ConsBibliography(book4, bil0)));
  IDocument b5 = new Book("Abig", "Nerd",
      "A study of different authors's approches to magic systems", bil4, "Fantasy Academy");

  IBibliography bil5 = new MtBibliography();
  // Adding books to the bibliography
  IDocument book5 = new Book("Patrick", "Rothfuss", "The Name of the Wind", bil5, "DAW Books");
  IDocument book6 = new Book("Patrick", "Rothfuss", "The Wise Man's Fear", bil5, "DAW Books");

  // Extending the bibliography
  IBibliography bil6 = new ConsBibliography(book5, new ConsBibliography(book5, bil4));
  IBibliography bil7 = new ConsBibliography(book6, bil6);

  // Creating a self-referential bibliography by including the bibliography itself
  IBibliography bil8 = new ConsBibliography(book1, bil7);
  IBibliography bil9 = new ConsBibliography(book2, bil8);
  IDocument metaArticle = new WArticle("Alice", "Example", "Recursive Bibliographies", bil9,
      "recursion.com");

  // Adding the meta article that references the current bibliography
  IBibliography bil10 = new ConsBibliography(metaArticle, bil9);

  IFormattedBibliography bilOut = bil7.flatten().sort().convertEachToFormatted();
  IFormattedBibliography duplicateBil = new ConsBibliography(book1,
      // Brandon Sanderson, "Warbreaker"
      new ConsBibliography(book1, // Brandon Sanderson, "Warbreaker" (duplicate)
          new ConsBibliography(book2, // Brandon Sanderson, "Elantris"
              new ConsBibliography(book2, // Brandon Sanderson, "Elantris" (duplicate)
                  new MtBibliography())))).removeDuplicates().convertEachToFormatted();

  boolean testFlatten(Tester t) {
    return t.checkExpect(bil0.flatten(), bil0) && t.checkExpect(bil3.flatten(), bil3)
        && t.checkExpect(bil4.flatten(), new ConsBibliography(wa1, new ConsBibliography(book3,
        new ConsBibliography(book2, new ConsBibliography(book1,
            new ConsBibliography(wa2, new ConsBibliography(book4, new MtBibliography())))))));

  }

  boolean testSort(Tester t) {
    return t.checkExpect(bil0.sort(), bil0) && t.checkExpect(bil1.sort(), bil1) && t.checkExpect(
        bil3.sort(), new ConsBibliography(book2,
            new ConsBibliography(book3, new ConsBibliography(book1, bil0))));
  }

  boolean testRemoveWArticles(Tester t) {
    return t.checkExpect(bil0.removeWArticles(), bil0) && t.checkExpect(bil3.removeWArticles(),
        bil3) && t.checkExpect(new ConsBibliography(wa1, bil3).removeWArticles(), bil3)
        && t.checkExpect(bil4.removeWArticles(), new ConsBibliography(book4, bil0));
  }

  boolean testRemoveDuplicates(Tester t) {
    // Creating duplicate bibliographies
    IBibliography duplicateBil = new ConsBibliography(book1, // Brandon Sanderson, "Warbreaker"
        new ConsBibliography(book1, // Brandon Sanderson, "Warbreaker" (duplicate)
            new ConsBibliography(book2, // Brandon Sanderson, "Elantris"
                new ConsBibliography(book2, // Brandon Sanderson, "Elantris" (duplicate)
                    new MtBibliography()))));

    // Expected result after removing duplicates
    IBibliography noDuplicatesBil = new ConsBibliography(book1, // Brandon Sanderson, "Warbreaker"
        new ConsBibliography(book2, // Brandon Sanderson, "Elantris"
            new MtBibliography()));

    return t.checkExpect(duplicateBil.removeDuplicates(), noDuplicatesBil) && t.checkExpect(
        bil0.removeDuplicates(), bil0) && t.checkExpect(bil3.removeDuplicates(), bil3)
        && t.checkExpect(
        new ConsBibliography(book1, new ConsBibliography(book1, bil0)).removeDuplicates(),
        new ConsBibliography(book1, bil0)) && t.checkExpect(new ConsBibliography(book1,
            new ConsBibliography(book2, new ConsBibliography(book1, bil0))).removeDuplicates().sort(),
        new ConsBibliography(book1, new ConsBibliography(book2, bil0)).sort()) && t.checkExpect(
        new ConsBibliography(book1,
            new ConsBibliography(book2, new ConsBibliography(book2, bil4))).removeDuplicates()
            .sort(), new ConsBibliography(book1, new ConsBibliography(book2, bil4)).sort());
  }

  boolean testFormat(Tester t) {
    return
        t.checkExpect(bil0.format(), new MtFormattedBibliography()) &&
        t.checkExpect(bil3.format(),
            new ConsFormattedBibliography(
                "Sanderson, Brandon. \"Elantris\".",
                new ConsFormattedBibliography(
                    "Sanderson, Brandon. \"Mistborn: The Final Empire\".",
                    new ConsFormattedBibliography(
                        "Sanderson, Brandon. \"Warbreaker\".",
                        new MtFormattedBibliography()
                    )
                )
            )
        ) &&
        t.checkExpect(bil6.format(),
            new ConsFormattedBibliography("Jordan, Robert. \"The Wheel Of Time: Book 1\".",
                new ConsFormattedBibliography("Rothfuss, Patrick. \"The Name of the Wind\".",
                    new ConsFormattedBibliography("Sanderson, Brandon. \"Elantris\".",
                        new ConsFormattedBibliography("Sanderson, Brandon. \"Mistborn: The Final "
                            + "Empire\".",
                        new ConsFormattedBibliography("Sanderson, Brandon. \"Warbreaker\".",
                            new MtFormattedBibliography()))))));
  }

}


