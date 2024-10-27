package lab7;

import tester.*;


// runs tests for the buddies problem
public class ExamplesBuddies{
  Person ann, bob, cole, dan, ed, fay, gabi, hank, jan, kim, len;

  void initExamples () {
    this.ann = new Person("Ann");
    this.bob = new Person("Bob");
    this.cole = new Person("Cole");
    this.dan = new Person("Dan");
    this.ed = new Person("Ed");
    this.fay = new Person("Fay");
    this.gabi = new Person("Gabi");
    this.hank = new Person("Hank");
    this.jan = new Person("Jan");
    this.kim = new Person("Kim");
    this.len = new Person("Len");

    ann.addBuddy(bob);
    ann.addBuddy(cole);
    bob.addBuddy(ann);
    bob.addBuddy(ed);
    bob.addBuddy(hank);
    cole.addBuddy(dan);
    dan.addBuddy(cole);
    ed.addBuddy(fay);
    fay.addBuddy(ed);
    fay.addBuddy(gabi);
    gabi.addBuddy(ed);
    gabi.addBuddy(fay);
    jan.addBuddy(kim);
    jan.addBuddy(len);
    kim.addBuddy(jan);
    kim.addBuddy(len);
    len.addBuddy(jan);
    len.addBuddy(kim);
  }

  void testHasDirectBuddy(Tester t) {
    this.initExamples();

    t.checkExpect(ann.hasDirectBuddy(bob), true);
    t.checkExpect(ann.hasDirectBuddy(len), false);
    t.checkExpect(ann.hasDirectBuddy(dan), false);
    t.checkExpect(hank.hasDirectBuddy(fay), false);
    t.checkExpect(jan.hasDirectBuddy(len), true);
    t.checkExpect(ed.hasDirectBuddy(gabi), false);
  }

  void testCountCommonBuddies(Tester t) {
    this.initExamples();

    t.checkExpect(ann.countCommonBuddies(bob), 0);
    t.checkExpect(fay.countCommonBuddies(gabi), 1);
    t.checkExpect(hank.countCommonBuddies(jan), 0);
    t.checkExpect(jan.countCommonBuddies(kim), 1);
    jan.addBuddy(hank);
    len.addBuddy(hank);
    t.checkExpect(len.countCommonBuddies(jan), 2);
  }

  void testGetCompleteBuddyNetwork(Tester t) {
    this.initExamples();

    t.checkExpect(hank.getCompleteBuddyNetwork(), new ConsLoBuddy(hank, new MTLoBuddy()));
    t.checkExpect(cole.getCompleteBuddyNetwork(), new ConsLoBuddy(dan, new ConsLoBuddy(cole,
        new MTLoBuddy())));
    t.checkExpect(jan.getCompleteBuddyNetwork(), new ConsLoBuddy(kim, new ConsLoBuddy(len,
        new ConsLoBuddy(jan, new MTLoBuddy()))));
    t.checkExpect(ed.getCompleteBuddyNetwork(), new ConsLoBuddy(gabi, new ConsLoBuddy(fay,
        new ConsLoBuddy(ed, new MTLoBuddy()))));
    t.checkExpect(ann.getCompleteBuddyNetwork(), new ConsLoBuddy(gabi, new ConsLoBuddy(fay,
        new ConsLoBuddy(ed,
            new ConsLoBuddy(hank,
                new ConsLoBuddy(bob, new ConsLoBuddy(dan,
                    new ConsLoBuddy(cole, new ConsLoBuddy(ann, new MTLoBuddy())))))))));
  }

  void testHasExtendedBuddy(Tester t) {
    this.initExamples();

    t.checkExpect(ann.hasExtendedBuddy(hank), true);
    t.checkExpect(ann.hasExtendedBuddy(bob), true);
    t.checkExpect(ann.hasExtendedBuddy(gabi), true);
    t.checkExpect(ann.hasExtendedBuddy(ed), true);
    t.checkExpect(ann.hasExtendedBuddy(jan), false);
    t.checkExpect(ann.hasExtendedBuddy(kim), false);
    t.checkExpect(jan.hasExtendedBuddy(jan), false);
    t.checkExpect(jan.hasExtendedBuddy(ann), false);
    t.checkExpect(jan.hasExtendedBuddy(kim), true);
    t.checkExpect(gabi.hasExtendedBuddy(bob), false);
  }

  void testPartyCount(Tester t) {
    this.initExamples();

    t.checkExpect(hank.partyCount(), 1);
    t.checkExpect(jan.partyCount(), 3);
    t.checkExpect(dan.partyCount(), 2);
    t.checkExpect(ann.partyCount(), 8);
    t.checkExpect(bob.partyCount(), 8);
  }
}