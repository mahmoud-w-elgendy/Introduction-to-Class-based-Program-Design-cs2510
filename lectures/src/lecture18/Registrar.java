package lecture18;
import tester.Tester;

class Course {
  String department;
  int id;
  Instructor instructor;
  IList<Student> enrollment;

  Course(String department, int id) {
    this.department = department;
    this.id = id;
    this.instructor = null;
    this.enrollment = new MtList<>();
  }

  void enrollStudent(Student student) {
    // Ideally, we want to throw an error here if the student is already enrolled in the course
    this.enrollment = new ConsList<>(student, this.enrollment);
  }

  void assignInstructor(Instructor instructor) {
    if (this.instructor != null) {
      throw new RuntimeException("A course can only have one instructor");
    }
    this.instructor = instructor;
  }
}

class Instructor {
  String firstName;
  String lastName;
  IList<Course> courses;

  public Instructor(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.courses = new MtList<>();
  }

void assignCourse(Course course) {
    course.assignInstructor(this);
    this.courses = new ConsList<>(course, this.courses);
  }
}

class Student {
  String firstName;
  String lastName;
  IList<Course> courses;

  public Student(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.courses = new MtList<>();
  }

  void enrollIn(Course course) {
    this.courses = new ConsList<Course>(course, this.courses);
    course.enrollStudent(this);
  }
}


class ExamplesCourses {
  Course htc, plabc, linearAlgebra1;
  Instructor danGrossman, gregorKiczales;
  Student davidGold, adamSmith, lauraHeart, laceyBeiler;

  void initTestConditions() {
    this.htc = new Course("Computer Science", 0);
    this.plabc = new Course("Computer science", 1);
    this.linearAlgebra1 = new Course("Mathematics", 2);

    this.danGrossman = new Instructor("Dan", "Grossman");
    this.gregorKiczales = new Instructor("Gregor", "Kiczales");

    this.davidGold = new Student("David", "Gold");
    this.adamSmith = new Student("Adam", "Smith");
    this.lauraHeart = new Student("Laura", "heart");
    this.laceyBeiler = new Student("Lacey", "Beiler");

    this.danGrossman.assignCourse(plabc);
    this.gregorKiczales.assignCourse(htc);
    this.gregorKiczales.assignCourse(linearAlgebra1);

    this.davidGold.enrollIn(htc);
    this.davidGold.enrollIn(plabc);
    this.davidGold.enrollIn(linearAlgebra1);
    this.adamSmith.enrollIn(plabc);
    this.lauraHeart.enrollIn(linearAlgebra1);
    this.lauraHeart.enrollIn(htc);
    this.laceyBeiler.enrollIn(linearAlgebra1);
  }

  void testConditions(Tester t) {
    this.initTestConditions();

    t.checkExpect(plabc.instructor, danGrossman);
    t.checkExpect(htc.instructor, gregorKiczales);
    t.checkExpect(linearAlgebra1.instructor, gregorKiczales);

    t.checkExpect(danGrossman.courses, new ConsList<>(plabc, new MtList<>()));
    t.checkExpect(gregorKiczales.courses, new ConsList<>(linearAlgebra1, new ConsList<>(htc,
        new MtList<>())));

    t.checkExpect(htc.enrollment, new ConsList<>(lauraHeart, new ConsList<Student>(davidGold,
        new MtList<>())));
    t.checkExpect(plabc.enrollment, new ConsList<>(adamSmith, new ConsList<Student>(davidGold,
        new MtList<>())));

    t.checkExpect(davidGold.courses, new ConsList<>(linearAlgebra1, new ConsList<Course>(plabc,
        new ConsList<>(htc, new MtList<>()))));
    t.checkExpect(adamSmith.courses, new ConsList<>(plabc, new MtList<>()));
    t.checkExpect(lauraHeart.courses, new ConsList<>(htc, new ConsList<Course>(linearAlgebra1,
        new MtList<>())));
    t.checkExpect(laceyBeiler.courses, new ConsList<>(linearAlgebra1, new MtList<>()));
  }

  void testEnrollIn(Tester t) {
    this.initTestConditions();

    this.adamSmith.enrollIn(htc);
    t.checkExpect(adamSmith.courses, new ConsList<>(htc, new ConsList<Course>(plabc,
        new MtList<>())));
    this.adamSmith.enrollIn(linearAlgebra1);
    t.checkExpect(adamSmith.courses, new ConsList<>(linearAlgebra1, new ConsList<Course>(htc,
        new ConsList<Course>(plabc, new MtList<>()))));
  }
}
