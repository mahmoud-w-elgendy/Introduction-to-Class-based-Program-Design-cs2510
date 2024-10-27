package assignment7.problem2;

import tester.Tester;

import java.util.function.Predicate;

interface IList<T> {
  boolean includes(T t);
  int countOccurences(Predicate<T> pred);
}

class MtList<T> implements IList<T> {
  public boolean includes(T t) { return false; }

  public int countOccurences(Predicate<T> pred) { return 0; }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  public ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean includes(T t) {
    return (first == t) || rest.includes(t);
  }

  public int countOccurences(Predicate<T> pred) {
    if (pred.test(first)) {
      return 1 + rest.countOccurences(pred);
    }
    else {
      return rest.countOccurences(pred);
    }
  }
}

class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  public Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.prof.addCourse(this);
    this.students = new MtList<Student>();
  }

  public void addStudent(Student student) {
    this.students = new ConsList<>(student, this.students);
  }
}

class Instructor {
  String name;
  IList<Course> courses;

  public Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  public void addCourse(Course course) {
    this.courses = new ConsList<>(course, this.courses);
  }

  boolean dejavu(Student c) {
    return this.courses.countOccurences(new CourseContainsStudent(c)) > 1;
  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;

  public Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  void enroll(Course c) {
    this.courses = new ConsList<>(c, this.courses);
    c.addStudent(this);
  }

  boolean classmates(Student c) {
    return courses.countOccurences(new CourseContainsStudent(c)) > 0;
  }
}

class CourseContainsStudent implements Predicate<Course> {
  Student student;

  public CourseContainsStudent(Student student) {
    this.student = student;
  }

  public boolean test(Course course) {
    return course.students.includes(student);
  }
}

class ExamplesRegistrar {
  Course htc, plabc, linearAlgebra1, artHistory;
  Instructor danGrossman, gregorKiczales, michelPicasso;
  Student davidGold, adamSmith, lauraHeart, laceyBeiler, jenniferBeiler;

  void initTestConditions() {
    this.danGrossman = new Instructor("Dan Grossman");
    this.gregorKiczales = new Instructor("Gregor Kiczales");
    this.michelPicasso = new Instructor("Michel picasso");

    this.htc = new Course("How To Code", this.gregorKiczales);
    this.plabc = new Course("Programming Languages", this.danGrossman);
    this.linearAlgebra1 = new Course("Linear Algebra 1", this.gregorKiczales);
    this.artHistory = new Course("A Gentle Introduction To The History of The Visual Arts",
        this.michelPicasso);

    this.davidGold = new Student("David Gold", 0);
    this.adamSmith = new Student("Adam Smith", 1);
    this.lauraHeart = new Student("Laura heart", 2);
    this.laceyBeiler = new Student("Lacey Beiler", 3);
    this.jenniferBeiler = new Student("Jennifer Beiler", 4);

    this.davidGold.enroll(htc);
    this.davidGold.enroll(plabc);
    this.davidGold.enroll(linearAlgebra1);
    this.adamSmith.enroll(plabc);
    this.lauraHeart.enroll(linearAlgebra1);
    this.lauraHeart.enroll(htc);
    this.laceyBeiler.enroll(linearAlgebra1);
    this.jenniferBeiler.enroll(artHistory);
  }

  void testConditions(Tester t) {
    this.initTestConditions();

    t.checkExpect(plabc.prof, danGrossman);
    t.checkExpect(htc.prof, gregorKiczales);
    t.checkExpect(linearAlgebra1.prof, gregorKiczales);

    t.checkExpect(danGrossman.courses, new ConsList<>(plabc, new MtList<>()));
    t.checkExpect(gregorKiczales.courses,
        new ConsList<>(linearAlgebra1, new ConsList<>(htc, new MtList<>())));

    t.checkExpect(htc.students,
        new ConsList<>(lauraHeart, new ConsList<Student>(davidGold, new MtList<>())));
    t.checkExpect(plabc.students,
        new ConsList<>(adamSmith, new ConsList<Student>(davidGold, new MtList<>())));

    t.checkExpect(davidGold.courses, new ConsList<>(linearAlgebra1,
        new ConsList<Course>(plabc, new ConsList<>(htc, new MtList<>()))));
    t.checkExpect(adamSmith.courses, new ConsList<>(plabc, new MtList<>()));
    t.checkExpect(lauraHeart.courses,
        new ConsList<>(htc, new ConsList<Course>(linearAlgebra1, new MtList<>())));
    t.checkExpect(laceyBeiler.courses, new ConsList<>(linearAlgebra1, new MtList<>()));
  }

  void testEnroll(Tester t) {
    this.initTestConditions();

    this.adamSmith.enroll(htc);
    t.checkExpect(adamSmith.courses,
        new ConsList<>(htc, new ConsList<Course>(plabc, new MtList<>())));
    this.adamSmith.enroll(linearAlgebra1);
    t.checkExpect(adamSmith.courses, new ConsList<>(linearAlgebra1,
        new ConsList<Course>(htc, new ConsList<Course>(plabc, new MtList<>()))));
  }

  void testClassmates(Tester t) {
    this.initTestConditions();

    // Based on the problem definition, there's nothing preventing a student from being a classmate
    // of himself/herself, since he/she shares a class with himself/herself.
    t.checkExpect(davidGold.classmates(davidGold), true);

    t.checkExpect(davidGold.classmates(adamSmith), true);
    t.checkExpect(adamSmith.classmates(davidGold), true);
    t.checkExpect(davidGold.classmates(laceyBeiler), true);
    t.checkExpect(davidGold.classmates(jenniferBeiler), false);
    t.checkExpect(lauraHeart.classmates(adamSmith), false);
    t.checkExpect(laceyBeiler.classmates(lauraHeart), true);
  }

  void testDejavu(Tester t) {
    this.initTestConditions();

    t.checkExpect(danGrossman.dejavu(davidGold), false);
    t.checkExpect(danGrossman.dejavu(lauraHeart), false);
    t.checkExpect(michelPicasso.dejavu(davidGold), false);
    t.checkExpect(michelPicasso.dejavu(jenniferBeiler), false);
    t.checkExpect(gregorKiczales.dejavu(davidGold), true);
    t.checkExpect(gregorKiczales.dejavu(lauraHeart), true);
    t.checkExpect(gregorKiczales.dejavu(adamSmith), false);
    t.checkExpect(gregorKiczales.dejavu(laceyBeiler), false);
    t.checkExpect(gregorKiczales.dejavu(jenniferBeiler), false);
  }
}
