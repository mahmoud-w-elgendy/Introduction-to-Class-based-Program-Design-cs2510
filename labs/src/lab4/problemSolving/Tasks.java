package lab4.problemSolving;

/*
  This one seemed above me at first. This is basically filtering and sorting a list of graph nodes,
  and I only did this sort of thing in the later part of the HTC courses, and not at all since.
  That's why I decided to make it easier for myself and not opt for the best solution:
  There's probably a way to combine the checking for cycles with the checking of the completeness
  of the list, while also sorting it. In my solution its 3 seperate traversals.
 */

import tester.Tester;

class Task {
  int id;
  ILoPrereqs prereqs; // list of ids for tasks that need to be completed before the current one

  public Task(int id, ILoPrereqs prereqs) {
    this.id = id;
    this.prereqs = prereqs;
  }

  public boolean canBeCompletedIn(ILoTask lot) {
    ILoTask prereqsTasks = prereqs.toTasksIn(lot);
    /*
      A task can be completed if:
      1. All of its prerequisites are in the list
      2. No of its prerequistes requires itself, or requires a task that requires itself, etc
      3. All of its prerequisites can be completed
     */
    return ((prereqsTasks.equalsPrereqs(prereqs)) && // All prereqs are included in lot
            !prereqsTasks.isSublistLeadsTo(lot, id) && // No cycles
            prereqsTasks.canBeCompletedHelper(lot).equals(prereqsTasks) // All prereqs can be
        // completed
    );
  }
}

interface ILoPrereqs {
  // produce a list of tasks from the given list where each task has an id of an item from this
  // If no such task exists, that item is skipped
  ILoTask toTasksIn(ILoTask lot);
  ILoTask toTasksInHelper(ILoTask lot, ILoTask rsf);

  boolean equalsConsLot(Task first, ILoTask rest);
  boolean equalsMtLot();
}

class MtLoPrereqs implements ILoPrereqs {
  public ILoTask toTasksIn(ILoTask lot) {
    return new MtLoTask();
  }

  public ILoTask toTasksInHelper(ILoTask lot, ILoTask rsf) {
    return rsf.reverse(); // For the tests, becasue the accumulator reverses the list
  }

  public boolean equalsConsLot(Task first, ILoTask rest) {
    return false;
  }

  public boolean equalsMtLot() {
    return true;
  }
}

class ConsLoPrereqs implements ILoPrereqs {
  int first;
  ILoPrereqs rest;

  public ConsLoPrereqs(int first, ILoPrereqs rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoTask toTasksIn(ILoTask lot) {
    return toTasksInHelper(lot, new MtLoTask());
  }

  public ILoTask toTasksInHelper(ILoTask lot, ILoTask rsf) {
    Task currentTask = lot.getTaskById(first);
    if (currentTask == null) return rest.toTasksInHelper(lot, rsf);

    return rest.toTasksInHelper(lot, new ConsLoTask(currentTask, rsf));
  }

  public boolean equalsConsLot(Task first, ILoTask rest) {
    return (this.first == first.id) && rest.equalsPrereqs(this.rest);
  }

  public boolean equalsMtLot() {
    return false;
  }
}

interface ILoTask {
  /*
    From the problem definition:
    Design a method on a list of tasks which produces the list of the tasks one can complete from
    these tasks. You can assume all task ids are unique.
   */
  ILoTask canBeCompleted();
  ILoTask canBeCompletedHelper(ILoTask wholeLot);

  /*
    Sort the list of tasks that can be completed to be in the order that one would need to complete
    the tasks in.
   */
  ILoTask sort();
  ILoTask sortHelper(ILoTask sortedLot);

  Task getTaskById(int id);

  ILoTask reverse();
  ILoTask reverseHelper(ILoTask rsf);

  ILoTask append(ILoTask lot);

  /*
    produce true if this includes a task with the given id somewhere inside it, or any of the
    prerequisites of its members, or any of their prerequisites etc. False otherwise.
   */
  boolean isSublistLeadsTo(ILoTask wholeLot, int id);
  boolean isSublistLeadsToHelper(ILoTask wholeLot, int id, ILoTask visited);

  boolean contains(Task t);
  
  boolean equals(ILoTask that);
  boolean equalsEmpty();
  boolean equalsCons(ConsLoTask that);

  // Returns true if every task correlates to an id in prereqs, and vice versa, and in the same
  // order. false otherwise.
  boolean equalsPrereqs(ILoPrereqs p);

  boolean isSorted();
}

class MtLoTask implements ILoTask {
  public ILoTask canBeCompleted() {
    return this;
  }

  public ILoTask canBeCompletedHelper(ILoTask wholeLot) {
    return this;
  }

  public ILoTask sort() {
    return this;
  }

  public ILoTask sortHelper(ILoTask sortedLot) {
    if (sortedLot.isSorted()) {
      return sortedLot;
    }
    return sortedLot.sort();
  }

  public Task getTaskById(int id) {
    return null;
  }

  public ILoTask reverse() {
    return this;
  }

  public ILoTask reverseHelper(ILoTask rsf) {
    return rsf;
  }

  public boolean isSublistLeadsTo(ILoTask wholeLot, int id) {
    return false;
  }

  public boolean isSublistLeadsToHelper(ILoTask wholeLot, int id, ILoTask visited) {
    return false;
  }

  public boolean contains(Task t) {
    return false;
  }

  public ILoTask append(ILoTask lot) {
    return lot;
  }

  public boolean equals(ILoTask that) {
    return that.equalsEmpty();
  }

  public boolean equalsEmpty() {
    return true;
  }

  public boolean equalsCons(ConsLoTask that) {
    return false;
  }

  public boolean equalsPrereqs(ILoPrereqs p) {
    return p.equalsMtLot();
  }

  public boolean isSorted() {
    return true;
  }

}

class ConsLoTask implements ILoTask {
  Task first;
  ILoTask rest;

  public ConsLoTask(Task first, ILoTask rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoTask canBeCompleted() {
    return canBeCompletedHelper(this);
  }

  public ILoTask canBeCompletedHelper(ILoTask wholeLot) {
    if (first.canBeCompletedIn(wholeLot)) {
      return new ConsLoTask(first, rest.canBeCompletedHelper(wholeLot));
    } else {
      return rest.canBeCompletedHelper(wholeLot);
    }
  }

  public ILoTask sort() {
    return sortHelper(new MtLoTask());
  }

  public ILoTask sortHelper(ILoTask sortedLot) {
    if (sortedLot.contains(first)) {
      return rest.sortHelper(sortedLot);
    }
    ILoTask prereqs = first.prereqs.toTasksIn(rest);
    return rest.sortHelper(sortedLot.append(prereqs.append(new ConsLoTask(first,
        new MtLoTask()))));
  }

  public Task getTaskById(int id) {
    if (first.id == id) return first;

    return rest.getTaskById(id);
  }

  public ILoTask reverse() {
    return reverseHelper(new MtLoTask());
  }

  public ILoTask reverseHelper(ILoTask rsf) {
    return rest.reverseHelper(new ConsLoTask(first, rsf));
  }

  public boolean isSublistLeadsTo(ILoTask wholeLot, int id) {
    return isSublistLeadsToHelper(wholeLot, id, new MtLoTask());
  }

  public boolean isSublistLeadsToHelper(ILoTask wholeLot, int id, ILoTask visited) {
    if (first.id == id) { // Is a cycle
      return true;
    } else if (visited.contains(first)) {
      return rest.isSublistLeadsToHelper(wholeLot, id, visited);
    }

    ILoTask currentTasks = first.prereqs.toTasksIn(wholeLot);
    ILoTask newVisited = new ConsLoTask(first, visited);
    return
        (currentTasks.isSublistLeadsToHelper(wholeLot, id, newVisited)) || // travel "down" the list
            // of prereqs
        rest.isSublistLeadsToHelper(wholeLot, id, newVisited.append(currentTasks)); // travel "To
    // the side", in the original list
  }

  public boolean contains(Task t) {
    return (first.id == t.id || rest.contains(t));
  }

  public ILoTask append(ILoTask lot) {
    return new ConsLoTask(first, rest.append(lot));
  }

  public boolean equals(ILoTask that) {
    return that.equalsCons(this);
  }

  public boolean equalsEmpty() {
    return false;
  }

  public boolean equalsCons(ConsLoTask that) {
    return (this.first.id == that.first.id) && rest.equals(that.rest);
  }

  public boolean equalsPrereqs(ILoPrereqs p) {
    return p.equalsConsLot(first, rest);
  }

  public boolean isSorted() {
    boolean noPrereqsAppearAfter = first.prereqs.toTasksIn(rest).equals(new MtLoTask());
    return noPrereqsAppearAfter && rest.isSorted();
  }
}

class ExamplesTask {
  ILoPrereqs p0 = new MtLoPrereqs();

  Task t1 = new Task(1, p0);
  Task t2 = new Task(2, p0);
  Task t3 = new Task(3, p0);

  ILoPrereqs p1 = new ConsLoPrereqs(1, p0);
  ILoPrereqs p2 = new ConsLoPrereqs(2, p1);
  ILoPrereqs p3 = new ConsLoPrereqs(3, p2);

  Task t4 = new Task(4, p2);
  Task t5 = new Task(5, new ConsLoPrereqs(4, new ConsLoPrereqs(3, p0)));

  ILoTask lot0 = new MtLoTask();
  ILoTask lot1 = new ConsLoTask(t1, lot0);
  ILoTask lot2 = new ConsLoTask(t2, lot1);
  ILoTask lot3 = new ConsLoTask(t3, lot2);
  ILoTask lot4 = new ConsLoTask(t4, lot3);
  ILoTask lot5 = new ConsLoTask(t5, lot4);
  ILoTask lotNo4 = new ConsLoTask(t5, lot3);
  ILoTask lotNo1 = new ConsLoTask(t2, new ConsLoTask(t3, new ConsLoTask(t4, new ConsLoTask(t5,
      lot0))));
  ILoTask lot4Sorted = new ConsLoTask(t2, new ConsLoTask(t1, new ConsLoTask(t4, new ConsLoTask(t3,
      lot0))));
  ILoTask lot5Sorted = new ConsLoTask(t2, new ConsLoTask(t1, new ConsLoTask(t4,
      new ConsLoTask(t3, new ConsLoTask(t5, lot0)))));

  Task T1Cycle1 = new Task(6, new ConsLoPrereqs(7, p0));
  Task T2Cycle1 = new Task(7, new ConsLoPrereqs(6, p0));
  Task T1Cycle2 = new Task(8, new ConsLoPrereqs(9, p0));
  Task T2Cycle2 = new Task(9, new ConsLoPrereqs(10, p0));
  Task T3Cycle2 = new Task(10, new ConsLoPrereqs(8, p0));
  ILoTask cycle1 = new ConsLoTask(T1Cycle1, new ConsLoTask(t3, new ConsLoTask(T2Cycle1, lot2)));
  ILoTask cycle2 = new ConsLoTask(T1Cycle2, new ConsLoTask(T2Cycle2, new ConsLoTask(T3Cycle2,
      lot0)));


  // Test cases for empty lists, lists with one task, and lists where all tasks can be completed
  boolean testSimpleCases(Tester t) {
    return t.checkExpect(lot0.canBeCompleted(), lot0) &&
        t.checkExpect(lot1.canBeCompleted(), lot1) &&
        t.checkExpect(lot3.canBeCompleted(), lot3) &&
        t.checkExpect(lot4.canBeCompleted(), lot4) &&
        t.checkExpect(lot5.canBeCompleted(), lot5);
  }

  // Test cases where not all tasks can be completed due to missing dependencies
  boolean testIncompleteTasks(Tester t) {
    return t.checkExpect(lotNo4.canBeCompleted(), lot3) &&
        t.checkExpect(lotNo1.canBeCompleted(), new ConsLoTask(t2, new ConsLoTask(t3, lot0)));
  }


  // Test cases involving cyclic dependencies
  boolean testCyclicDependencies(Tester t) {
    return t.checkExpect(cycle1.canBeCompleted(), lot3) &&
        t.checkExpect(cycle2.canBeCompleted(), lot0);
  }

  // Test cases for empty lists, lists with one task, and lists where all tasks can be completed
  boolean testSimpleCasesSorted(Tester t) {
    return t.checkExpect(lot0.canBeCompleted().sort(), lot0) &&
        t.checkExpect(lot1.canBeCompleted().sort(), lot1) &&
        t.checkExpect(lot3.canBeCompleted().sort(), lot3) &&
        t.checkExpect(lot4.canBeCompleted().sort(), lot4Sorted) &&
        t.checkExpect(lot5.canBeCompleted().sort(), lot5Sorted);
  }

  boolean testIsSublistLeadsTo(Tester t) {
    return t.checkExpect(lot0.isSublistLeadsTo(lot3, 2), false) &&
        t.checkExpect(lot2.isSublistLeadsTo(lot3, 2), true) &&
        t.checkExpect(lot5.isSublistLeadsTo(lot5, 1), true) &&
    // Test when the specified id is in the middle of the sublist
    t.checkExpect(lot3.isSublistLeadsTo(lot5, 3), true) &&

        // Test when the specified id is the last element in the sublist
        t.checkExpect(lot3.isSublistLeadsTo(lot5, 5), false) &&

        // Test when the sublist leads to the specified id indirectly through dependencies
        t.checkExpect(lot4.isSublistLeadsTo(lot5, 4), true) &&

        t.checkExpect(cycle1.isSublistLeadsTo(cycle1, 3), true) &&
        t.checkExpect(cycle2.isSublistLeadsTo(cycle2, 10), true) &&
        t.checkExpect(cycle2.isSublistLeadsTo(cycle2, 11), false);
  }

  // Test cases for the reverse and reverseHelper methods
  boolean testReverse(Tester t) {
    return t.checkExpect(lot0.reverse(), lot0) &&
        t.checkExpect(lot1.reverse(), new ConsLoTask(t1, lot0)) &&
        t.checkExpect(lot2.reverse(), new ConsLoTask(t1, new ConsLoTask(t2, lot0))) &&
        t.checkExpect(lot3.reverse(), new ConsLoTask(t1, new ConsLoTask(t2, new ConsLoTask(t3, lot0)))) &&
        t.checkExpect(lot4.reverse(), new ConsLoTask(t1, new ConsLoTask(t2, new ConsLoTask(t3, new ConsLoTask(t4, lot0)))));
  }

  // Test cases for the getTaskById method
  boolean testGetTaskById(Tester t) {
    return t.checkExpect(lot0.getTaskById(1), null) &&
        t.checkExpect(lot1.getTaskById(1), t1) &&
        t.checkExpect(lot2.getTaskById(2), t2) &&
        t.checkExpect(lot3.getTaskById(3), t3) &&
        t.checkExpect(lot4.getTaskById(4), t4) &&
        t.checkExpect(lot5.getTaskById(5), t5) &&
        t.checkExpect(lot5.getTaskById(6), null);
  }

  // Test cases for the append method
  boolean testAppend(Tester t) {
    return t.checkExpect(lot0.append(lot1), lot1) &&
        t.checkExpect(lot1.append(lot2), new ConsLoTask(t1, new ConsLoTask(t2, lot1))) &&
        t.checkExpect(lot3.append(lot2), new ConsLoTask(t3, new ConsLoTask(t2,
            new ConsLoTask(t1, lot2))));
  }

  boolean testEquals(Tester t) {
    return t.checkExpect(lot0.equals(new MtLoTask()), true) &&
        t.checkExpect(lot0.equals(lot3), false) &&
        t.checkExpect(lot3.equals(lot3), true) &&
        t.checkExpect(lot5.equals(lot4), false) &&
        t.checkExpect(lot2.equals(new ConsLoTask(t2, lot1)), true);
  }

  boolean testEqualsPrereqs(Tester t) {
    return t.checkExpect(lot0.equalsPrereqs(p0), true) &&
        t.checkExpect(lot0.equalsPrereqs(p3), false) &&
        t.checkExpect(lot1.equalsPrereqs(p1), true) &&
        t.checkExpect(lot4.equalsPrereqs(new ConsLoPrereqs(4, new ConsLoPrereqs(2, p2))), false) &&
        t.checkExpect(lot3.equalsPrereqs(p3), true) &&
        t.checkExpect(lot3.equalsPrereqs(new ConsLoPrereqs(3, new ConsLoPrereqs(2,
            new ConsLoPrereqs(1, new ConsLoPrereqs(4, p0))))), false) &&
        t.checkExpect(lot3.equalsPrereqs(new ConsLoPrereqs(3, new ConsLoPrereqs(2, p0))), false);
  }
}