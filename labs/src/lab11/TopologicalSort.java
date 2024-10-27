package lab11;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;

class Curriculum {
  ArrayList<Course> courses;
  Curriculum() { this.courses = new ArrayList<Course>(); }
  // EFFECT: adds another course to the set of known courses
  void addCourse(Course c) { this.courses.add(c); }

  // No idea why they wanted us to implement these methods in this class...
  boolean comesAfterPrereqs(ArrayList<Course> schedule, Course c) {
    int coursePos = schedule.indexOf(c);
    if (coursePos == -1) { return false; }
    for (Course prereq: c.prereqs) {
      if (schedule.indexOf(prereq) >= coursePos) { return false; }
    }
    return true;
  }

  boolean validSchedule(ArrayList<Course> schedule) {
    for (Course c: schedule) {
      if (!comesAfterPrereqs(schedule, c)) { return false; }
    }
    return true;
  }

  ArrayList<Course> topSort(ArrayList<Course> courses) {
    ArrayList<Course> sortedCourses = new ArrayList<>();
    for (Course c: courses) {
      c.process(sortedCourses);
    }
    return sortedCourses;
  }
}

class Course {
  String name;
  ArrayList<Course> prereqs;
  Course(String name) { this.name = name; this.prereqs = new ArrayList<Course>(); }
  // EFFECT: adds a course as a prereq to this one
  void addPrereq(Course c) { this.prereqs.add(c); }

  void process(ArrayList<Course> processed) {
    if (processed.contains(this)) { return; }
    for (Course prereq: prereqs) {
      prereq.process(processed);
    }
    processed.add(this);
  }
}


class ExamplesTopologicalSort {
  Course fund1, databaseDesign, fund2, toc, ood, progLangs, compilers, ad, compSys, lspdp;
  Curriculum curr;
  ArrayList<Course> courses, sortedCourses;

  void setup() {
    fund1 = new Course("Fundamentals 1");
    databaseDesign = new Course("Database Design");
    fund2 = new Course("Fundamentals 2");
    toc = new Course("Theory of Computation");
    ood = new Course("Object-Oriented Design");
    progLangs = new Course("Programming Languages");
    compilers = new Course("Compilers");
    ad = new Course("Algorithms and Data");
    compSys = new Course("Computer Systems");
    lspdp = new Course("Large-Scale Parallel Data Processing");

    databaseDesign.addPrereq(fund1);
    fund2.addPrereq(fund1);
    toc.addPrereq(fund2);
    ood.addPrereq(fund2);
    progLangs.addPrereq(toc);
    progLangs.addPrereq(ood);
    compilers.addPrereq(progLangs);
    ad.addPrereq(fund2);
    compSys.addPrereq(fund2);
    lspdp.addPrereq(ad);
    lspdp.addPrereq(compSys);

    curr = new Curriculum();
    courses = new ArrayList<>(Arrays.asList(toc, fund1, lspdp, databaseDesign,
        compilers, fund2, ood, progLangs, ad, compSys));
    for (Course c: courses) {
      curr.addCourse(c);
    }

    sortedCourses = new ArrayList<>(Arrays.asList(fund1, databaseDesign, fund2, toc,
        ood, progLangs, compilers, ad, compSys, lspdp));
  }

  void testValidSchedule(Tester t) {
    setup();
    t.checkExpect(curr.validSchedule(sortedCourses), true);
    sortedCourses.add(0, fund2);
    t.checkExpect(curr.validSchedule(sortedCourses), false);
    t.checkExpect(curr.validSchedule(courses), false);
  }

  void testTopSort(Tester t) {
    setup();
    t.checkExpect(curr.validSchedule(curr.topSort(sortedCourses)), true);
    t.checkExpect(curr.validSchedule(curr.topSort(courses)), true);

    // just for fun
    for (Course c: curr.topSort(courses)) {
      System.out.println(c.name);
    }
  }
}