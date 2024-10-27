// CS 2510, Assignment 3

package assignment3.problem1;
import tester.*;

// to represent a list of Strings
interface ILoString {
  // combine all Strings in this list into one
  String combine();

  ILoString sort();
  ILoString sortHelper(String first);

  boolean isSorted();
  boolean isSortedHelper(String first);

  ILoString interleave(ILoString other);

  ILoString merge(ILoString other);
  ILoString mergeHelper(String first, ILoString rest);

  ILoString reverse();
  ILoString reverseHelper(ILoString rsf);

  boolean isDoubledList();

  boolean isDoubledListHelper(String first);

  boolean isPalindromeList();
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString(){}

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  public ILoString sort() {
    return this;
  }

  public ILoString sortHelper(String first) {
    return new ConsLoString(first, this);
  }

  public boolean isSorted() {
    return true;
  }

  public boolean isSortedHelper(String first) {
    return true;
  }

  public ILoString interleave(ILoString other) {
    return other;
  }

  public ILoString merge(ILoString other) {
    return other;
  }

  public ILoString mergeHelper(String first, ILoString rest) {
    return new ConsLoString(first, rest);
  }

  public ILoString reverse() {
    return this;
  }

  public ILoString reverseHelper(ILoString rsf) {
    return rsf;
  }

  public boolean isDoubledList() {
    return true;
  }

  public boolean isDoubledListHelper(String first) {
    return false;
  }

  public boolean isPalindromeList() {
    return true;
  }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest){
    this.first = first;
    this.rest = rest;
  }

    /*
     TEMPLATE
     FIELDS:
     ... this.first ...         -- String
     ... this.rest ...          -- ILoString

     METHODS
     ... this.combine() ...     -- String

     METHODS FOR FIELDS
     ... this.first.concat(String) ...        -- String
     ... this.first.compareTo(String) ...     -- int
     ... this.rest.combine() ...              -- String

     */

  // combine all Strings in this list into one
  public String combine(){
    return this.first.concat(this.rest.combine());
  }

  public ILoString sort() {
    return rest.sort().sortHelper(first);
    }

  public ILoString sortHelper(String first) {
    if (isBefore(first, this.first)) {
      return new ConsLoString(first, rest);
    } else {
      return new ConsLoString(this.first, rest.sortHelper(first));
    }
  }

  public boolean isSorted() {
    return rest.isSortedHelper(first);
  }

  public boolean isSortedHelper(String first) {
    return isBefore(first, this.first) && rest.isSortedHelper(this.first);
  }

  public ILoString interleave(ILoString other) {
    return new ConsLoString(first, other.interleave(rest));
  }

  public ILoString merge(ILoString other) {
    return other.mergeHelper(first, rest);
  }

  public ILoString mergeHelper(String first, ILoString rest) {
    if (isBefore(first, this.first)) {
      return new ConsLoString(first, this.merge(rest));
    }
    return new ConsLoString(this.first, this.rest.merge(new ConsLoString(first, rest)));
  }

  public ILoString reverse() {
    return reverseHelper(new MtLoString());
  }

  public ILoString reverseHelper(ILoString rsf) {
    return rest.reverseHelper(new ConsLoString(first, rsf));
  }

  public boolean isDoubledList() {
    return rest.isDoubledListHelper(first);
  }

  public boolean isDoubledListHelper(String first) {
    return first.equals(this.first) && rest.isDoubledList();
  }

  public boolean isPalindromeList() {
    return this.interleave(this.reverse()).isDoubledList();
  }

  private boolean isBefore(String str1, String str2) {
    return str1.toLowerCase().compareTo(str2.toLowerCase()) <= 0;
  }

}

// to represent examples for lists of strings
class ExamplesStrings{

  ILoString mary = new ConsLoString("Mary ",
      new ConsLoString("had ",
          new ConsLoString("a ",
              new ConsLoString("little ",
                  new ConsLoString("lamb.", new MtLoString())))));
  ILoString los0 = new MtLoString();
  ILoString los1 = new ConsLoString("abba", los0);
  ILoString los2 = new ConsLoString("Baddum!!", los1);
  ILoString los3 = new ConsLoString("Rawwr..?", los2);
  ILoString sortedLos3 = new ConsLoString("abba",
      new ConsLoString("Baddum!!", new ConsLoString("Rawwr..?", los0)));


  // test the method combine for the lists of Strings
  boolean testCombine(Tester t){
    return
        t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
  }

  boolean testSort(Tester t) {
    return t.checkExpect(los0.sort(), los0) &&
        t.checkExpect(los1.sort(), los1) &&
        t.checkExpect(los3.sort(), sortedLos3);
  }

  boolean testIsSorted(Tester t) {
    return t.checkExpect(los0.isSorted(), true) &&
        t.checkExpect(los1.isSorted(), true) &&
        t.checkExpect(los2.isSorted(), false) &&
        t.checkExpect(sortedLos3.isSorted(), true);
  }

  boolean testInterleave(Tester t) {
    return t.checkExpect(los0.interleave(los0), los0) &&
        t.checkExpect(los0.interleave(los3), los3) &&
        t.checkExpect(los1.interleave(los2), new ConsLoString("abba", los2)) &&
        t.checkExpect(mary.interleave(los3), new ConsLoString("Mary ",
            new ConsLoString("Rawwr..?",
                new ConsLoString("had ",
                    new ConsLoString("Baddum!!",
                        new ConsLoString("a ",
                            new ConsLoString("abba",
                                new ConsLoString("little ",
                                    new ConsLoString("lamb.", new MtLoString())))))))));
  }

  boolean testMerge(Tester t) {
    return t.checkExpect(los0.merge(los0), los0) &&
        t.checkExpect(los1.merge(los0), los1) &&
        t.checkExpect(sortedLos3.merge(sortedLos3), new ConsLoString("abba",
            new ConsLoString("abba",
                new ConsLoString("Baddum!!",
                    new ConsLoString("Baddum!!",
                        new ConsLoString("Rawwr..?",
                            new ConsLoString("Rawwr..?", new MtLoString()))))))) &&
        t.checkExpect(sortedLos3.merge(new ConsLoString("aaaa",
            new ConsLoString("aaaargh",
                new ConsLoString("beowulf", new ConsLoString("Zenom", los0))))),
            new ConsLoString("aaaa", new ConsLoString("aaaargh", new ConsLoString("abba", new ConsLoString("Baddum!!",
                new ConsLoString("beowulf",
                    new ConsLoString("Rawwr..?", new ConsLoString("Zenom", los0))))))));
  }

  boolean testReverse(Tester t) {
    return t.checkExpect(los0.reverse(), los0) && // reversing empty list
        t.checkExpect(los1.reverse(), los1) &&    // reversing single-element list
        t.checkExpect(sortedLos3.reverse(),       // reversing multi-element list
            new ConsLoString("Rawwr..?",
                new ConsLoString("Baddum!!",
                    new ConsLoString("abba", new MtLoString()))));
  }

  boolean testIsDoubledList(Tester t) {
    return t.checkExpect(los0.isDoubledList(), true) &&
        t.checkExpect(los1.isDoubledList(), false) &&
        t.checkExpect(sortedLos3.interleave(sortedLos3).isDoubledList(), true) &&
        t.checkExpect(mary.isDoubledList(), false) &&
        t.checkExpect(new ConsLoString("blah", los2.interleave(los2)).isDoubledList(), false);
  }

  boolean testIsPalindromeList(Tester t) {
    ILoString palindromeList2 = new ConsLoString("abba",
        new ConsLoString("abba", los0)); // Two-element palindromic list
    ILoString nonPalindromeList2 = new ConsLoString("abba",
        new ConsLoString("Rawwr..?", los0)); // Two-element non-palindromic list
    ILoString palindromeList3 = new ConsLoString("a",
        new ConsLoString("b",
            new ConsLoString("a", los0))); // Multi-element palindromic list
    ILoString nonPalindromeList3 = new ConsLoString("a",
        new ConsLoString("b",
            new ConsLoString("c", los0))); // Multi-element non-palindromic list

    return t.checkExpect(los0.isPalindromeList(), true) &&           // Empty list
        t.checkExpect(los1.isPalindromeList(), true) &&              // Single-element list
        t.checkExpect(palindromeList2.isPalindromeList(), true) &&   // Two-element palindromic list
        t.checkExpect(nonPalindromeList2.isPalindromeList(), false) && // Two-element non-palindromic list
        t.checkExpect(palindromeList3.isPalindromeList(), true) &&   // Multi-element palindromic list
        t.checkExpect(nonPalindromeList3.isPalindromeList(), false); // Multi-element non-palindromic list
  }
}
