package lecture13;

import tester.Tester;

import java.util.TreeSet;

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

  public boolean isMaleRunner() { return this.isMale; }

  public boolean posUnder50() { return pos <= 50; }
}

interface ILoRunner {
  ILoRunner findAllMaleRunners();
  ILoRunner findAllFemaleRunners();
  ILoRunner findRunnersInFirst50();
  ILoRunner find(IRunnerPredicate pred);
}

class MtLoRunner implements ILoRunner {
  public ILoRunner findAllMaleRunners() { return this; }
  public ILoRunner findAllFemaleRunners() { return this; }
  public ILoRunner findRunnersInFirst50() { return this; }
  public ILoRunner find(IRunnerPredicate pred) { return this; }
}

class ConsLoRunner implements ILoRunner {
  Runner first;
  ILoRunner rest;

  ConsLoRunner(Runner first, ILoRunner rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoRunner findAllMaleRunners() {
    if (this.first.isMaleRunner()) {
      return new ConsLoRunner(this.first, this.rest.findAllMaleRunners());
    }
    else {
      return this.rest.findAllMaleRunners();
    }
  }
  public ILoRunner findAllFemaleRunners() {
    if (!this.first.isMaleRunner()) {
      return new ConsLoRunner(this.first, this.rest.findAllFemaleRunners());
    }
    else {
      return this.rest.findAllFemaleRunners();
    }
  }

  public ILoRunner findRunnersInFirst50() {
    if (this.first.posUnder50()) {
      return new ConsLoRunner(this.first, this.rest.findRunnersInFirst50());
    }
    else {
      return this.rest.findRunnersInFirst50();
    }
  }

  public ILoRunner find(IRunnerPredicate pred) {
    if (pred.apply(this.first)) {
      return new ConsLoRunner(this.first, this.rest.find(pred));
    }
    else {
      return this.rest.find(pred);
    }
  }
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

  boolean testFindMethodsOld(Tester t) {
    return
        t.checkExpect(this.list2.findAllFemaleRunners(),
            new ConsLoRunner(this.joan, new MtLoRunner())) &&
            t.checkExpect(this.list2.findAllMaleRunners(),
                new ConsLoRunner(this.frank,
                    new ConsLoRunner(this.bill,
                        new ConsLoRunner(this.johnny, new MtLoRunner()))));
  }

  boolean testFirst50(Tester t) {
    return t.checkExpect(this.mtlist.findRunnersInFirst50(), mtlist) &&
        t.checkExpect(list1.findRunnersInFirst50(), list1) &&
        t.checkExpect(list2.findRunnersInFirst50(), list1);
  }

  boolean testFindMethods(Tester t) {
    return
        t.checkExpect(this.list2.find(new RunnerIsFemale()),
            new ConsLoRunner(this.joan, new MtLoRunner())) &&
            t.checkExpect(this.list2.find(new RunnerIsMale()),
                new ConsLoRunner(this.frank,
                    new ConsLoRunner(this.bill,
                        new ConsLoRunner(this.johnny, new MtLoRunner()))));
  }

  boolean testFindUnder4Hours(Tester t) {
    return
        t.checkExpect(this.list2.find(new FinishIn4Hours()),
            new ConsLoRunner(this.frank,
                new ConsLoRunner(this.bill,
                    new ConsLoRunner(this.joan, new MtLoRunner()))));
  }

  boolean testCombinedQuestions(Tester t) {
    return
        t.checkExpect(this.list2.find(
                new AndPredicate(new RunnerIsMale(), new FinishIn4Hours())),
            new ConsLoRunner(this.frank,
                new ConsLoRunner(this.bill, new MtLoRunner()))) &&
            t.checkExpect(this.list2.find(
                    new AndPredicate(new RunnerIsFemale(),
                        new AndPredicate(new RunnerIsYounger40(),
                            new RunnerIsInFirst50()))),
                new ConsLoRunner(this.joan, new MtLoRunner()));
  }

  boolean testFemaleOrFinishIn4Hours(Tester t) {
    return t.checkExpect(list1.find(femaleOrFinishIn4Hours),
        new ConsLoRunner(joan, mtlist));
  }
}