package lecture14;

import tester.Tester;

// Class representing a runner
class Runner {
  String name;
  int age;
  int bib;
  boolean isMale;
  int pos;
  int time;

  Runner(String name, int age, int bib, boolean isMale, int pos, int time) {
    this.name = name;
    this.age = age;
    this.bib = bib;
    this.isMale = isMale;
    this.pos = pos;
    this.time = time;
  }

  public boolean finishesBefore(Runner r) {
    return time < r.time;
  }
}

interface ILoRunner {
  ILoRunner find(IRunnerPredicate pred);
  ILoRunner sortByTime();
  ILoRunner insertByTime(Runner r);
  ILoRunner sortBy(IRunnerComparator comp);
  ILoRunner insertBy(IRunnerComparator comp, Runner r);
  // Finds the first Runner in this list of Runners
  Runner getFirst();
  // Finds the fastest Runner in this list of Runners
  Runner findWinner();
  Runner findMin(IRunnerComparator comp);
  // Returns the minimum Runner of the given accumulator and every Runner
  // in this list, according to the given comparator
  Runner findMinHelp(IRunnerComparator comp, Runner acc);
  Runner findMax(IRunnerComparator comp);
}

class MtLoRunner implements ILoRunner {
  public ILoRunner find(IRunnerPredicate pred) { return this; }

  public ILoRunner sortByTime() {
    return this;
  }

  public ILoRunner insertByTime(Runner r) {
    return new ConsLoRunner(r, this);
  }

  public ILoRunner sortBy(IRunnerComparator comp) {
    return this;
  }

  public ILoRunner insertBy(IRunnerComparator comp, Runner r) {
    return new ConsLoRunner(r, this);
  }

  public Runner findWinnerOld() {
    throw new RuntimeException("No winner of an empty list of Runners");
  }

  public Runner getFirst() {
    throw new RuntimeException("No first of an empty list of Runners");
  }

  public Runner findWinner() {
    return null;
  }

  public Runner findMin(IRunnerComparator comp) {
    throw new RuntimeException("No minimum runner available in this list!");
  }

  public Runner findMinHelp(IRunnerComparator comp, Runner acc) {
    return acc;
  }

  public Runner findMax(IRunnerComparator comp) {
    return findMin(comp);
  }
}

class ConsLoRunner implements ILoRunner {
  Runner first;
  ILoRunner rest;

  ConsLoRunner(Runner first, ILoRunner rest) {
    this.first = first;
    this.rest = rest;
  }


  public ILoRunner find(IRunnerPredicate pred) {
    if (pred.apply(this.first)) {
      return new ConsLoRunner(this.first, this.rest.find(pred));
    }
    else {
      return this.rest.find(pred);
    }
  }

  public ILoRunner sortByTime() {
    return rest.sortByTime().insertByTime(first);
  }

  public ILoRunner insertByTime(Runner r) {
    if (r.finishesBefore(this.first)) {
      return new ConsLoRunner(r, this);
    }
    return new ConsLoRunner(this.first, rest.insertByTime(r));
  }

  public ILoRunner sortBy(IRunnerComparator comp) {
    return rest.sortBy(comp).insertBy(comp, first);
  }

  public ILoRunner insertBy(IRunnerComparator comp, Runner r) {
    if (comp.compare(r, this.first) < 0) {
      return new ConsLoRunner(r, this);
    }
    return new ConsLoRunner(this.first, rest.insertByTime(r));
  }

  public Runner findWinnerOld() {
    return this.sortBy(new CompareByTime()).getFirst();
  }

  public Runner getFirst() {
    return first;
  }

  public Runner findMin(IRunnerComparator comp) {
    return rest.findMinHelp(comp, first);
  }

  public Runner findMinHelp(IRunnerComparator comp, Runner acc) {
    if (comp.compare(first, acc) < 0) {
      return rest.findMinHelp(comp, first);
    } else {
      return rest.findMinHelp(comp, acc);
    }
  }

  public Runner findMax(IRunnerComparator comp) {
    return findMin(new ReverseComparator(comp));
  }

  public Runner findWinner() { return this.findMin(new CompareByTime()); }
}

interface IRunnerPredicate {
  boolean apply(Runner r);
}
class RunnerIsMale implements IRunnerPredicate {
  public boolean apply(Runner r) { return r.isMale; }
}
class RunnerIsFemale implements IRunnerPredicate {
  public boolean apply(Runner r) { return !r.isMale; }
}
class RunnerIsInFirst50 implements IRunnerPredicate {
  public boolean apply(Runner r) { return r.pos <= 50; }
}

class FinishIn4Hours implements  IRunnerPredicate {
  public boolean apply(Runner r) { return r.time < 240; }
}

class RunnerIsYounger40 implements  IRunnerPredicate {
  public boolean apply(Runner r) { return r.age < 40; }
}

class AndPredicate implements IRunnerPredicate {
  IRunnerPredicate left, right;
  AndPredicate(IRunnerPredicate left, IRunnerPredicate right) {
    this.left = left;
    this.right = right;
  }
  public boolean apply(Runner r) {
    return this.left.apply(r) && this.right.apply(r);
  }
}

class OrPredicate implements IRunnerPredicate {
  IRunnerPredicate left, right;
  OrPredicate(IRunnerPredicate left, IRunnerPredicate right) {
    this.left = left;
    this.right = right;
  }
  public boolean apply(Runner r) {
    return this.left.apply(r) || this.right.apply(r);
  }
}


interface ICompareRunners {
  boolean comesBefore(Runner r1, Runner r2);
}

class CompareByTimeOld implements ICompareRunners {
  public boolean comesBefore(Runner r1, Runner r2) {
    return r1.time < r2.time;
  }
}

// To compute a three-way comparison between two Runners
interface IRunnerComparator {
  // Returns a negative number if r1 comes before r2 in this order
  // Returns zero              if r1 is tied with r2 in this order
  // Returns a positive number if r1 comes after  r2 in this order
  int compare(Runner r1, Runner r2);
}

class CompareByTime implements IRunnerComparator {
  public int compare(Runner r1, Runner r2) {
    return r1.time - r2.time;
  }
}

class ReverseComparator implements IRunnerComparator{
  IRunnerComparator comp;

  public ReverseComparator(IRunnerComparator comp) {
    this.comp = comp;
  }

  public int compare(Runner r1, Runner r2) {
    return comp.compare(r2, r1);
  }
}

class CompareByName implements IRunnerComparator {
  public int compare(Runner r1, Runner r2) {
    return r1.name.compareTo(r2.name);
  }
}

class ExamplesRunner {
  Runner johnny = new Runner("Kelly", 97, 999, true, 30, 360);
  Runner frank  = new Runner("Shorter", 32, 888, true, 245, 130);
  Runner bill = new Runner("Rogers", 36, 777, true, 119, 129);
  Runner joan = new Runner("Benoit", 29, 444, false, 18, 155);

  ILoRunner mtlist = new MtLoRunner();
  ILoRunner list1 = new ConsLoRunner(johnny, new ConsLoRunner(joan, mtlist));
  ILoRunner list2 = new ConsLoRunner(frank, new ConsLoRunner(bill, list1));

  IRunnerPredicate femaleOrFinishIn4Hours = new OrPredicate(
      new RunnerIsFemale(), new FinishIn4Hours()
  );

  boolean testSortByTime(Tester t) {
    return t.checkExpect(mtlist.sortByTime(), mtlist) &&
        t.checkExpect(list1.sortByTime(), new ConsLoRunner(joan,
            new ConsLoRunner(johnny, mtlist))) &&
        t.checkExpect(list2.sortByTime(), new ConsLoRunner(bill, new ConsLoRunner(frank,
            new ConsLoRunner(joan, new ConsLoRunner(johnny, mtlist)))));
  }
}