package lab12;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExamplesShapesInJUnit {
  public ExamplesShapesInJUnit() {}

  CartPt pt1 = new CartPt(0, 0);
  CartPt pt2 = new CartPt(3, 4);
  CartPt pt3 = new CartPt(7, 1);

  IShape c1 = new Circle(new CartPt(50, 50), 10, "red");
  IShape c2 = new Circle(new CartPt(50, 50), 30, "red");
  IShape c3 = new Circle(new CartPt(30, 100), 30, "blue");

  IShape s1 = new Square(new CartPt(50, 50), 30, "red");
  IShape s2 = new Square(new CartPt(50, 50), 50, "red");
  IShape s3 = new Square(new CartPt(20, 40), 10, "green");

  // test the method distToOrigin in the class CartPt
  @Test
  public void testDistToOrigin() {
    assertEquals(0.0, this.pt1.distToOrigin(), 0.001);
    assertEquals(5.0, this.pt2.distToOrigin(), 0.001);
  }

  // test the method distTo in the class CartPt
  @Test
  public void testDistTo() {
    assertEquals(5.0, this.pt1.distTo(this.pt2), 0.001);
    assertEquals(5.0, this.pt2.distTo(this.pt3), 0.001);
  }

  // test the method area in the class Circle
  @Test
  public void testCircleArea() {
    assertEquals(314.15, this.c1.area(), 0.01);
  }

  // test the method grow in the class Circle
  @Test
  public void testSquareArea() {
    assertEquals(900.0, this.s1.area(), 0.01);
  }

  // test the method distToOrigin in the class Circle
  @Test
  public void testCircleDistToOrigin() {
    assertEquals(60.71, this.c1.distToOrigin(), 0.01);
    assertEquals(74.40, this.c3.distToOrigin(), 0.01);
  }

  // test the method distTo in the class Circle
  @Test
  public void testSquareDistToOrigin() {
    assertEquals(70.71, this.s1.distToOrigin(), 0.01);
    assertEquals(44.72, this.s3.distToOrigin(), 0.01);
  }

  // test the method grow in the class Circle
  @Test
  public void testCircleGrow() {
    assertEquals(this.c2, this.c1.grow(20));
  }

  // test the method grow in the class Circle
  @Test
  public void testSquareGrow() {
    assertEquals(this.s2, this.s1.grow(20));
  }

  // test the method biggerThan in the class Circle
  @Test
  public void testCircleBiggerThan() {
    assertFalse(this.c1.biggerThan(this.c2));
    assertTrue(this.c2.biggerThan(this.c1));
    assertFalse(this.c1.biggerThan(this.s1));
    assertTrue(this.c1.biggerThan(this.s3));
  }

  // test the method biggerThan in the class Square
  @Test
  public void testSquareBiggerThan() {
    assertFalse(this.s1.biggerThan(this.s2));
    assertTrue(this.s2.biggerThan(this.s1));
    assertTrue(this.s1.biggerThan(this.c1));
    assertFalse(this.s3.biggerThan(this.c1));
  }

  // test the method contains in the class Circle
  @Test
  public void testCircleContains() {
    assertFalse(this.c1.contains(new CartPt(100, 100)));
    assertTrue(this.c2.contains(new CartPt(40, 60)));
  }


  // test the method contains in the class Square
  @Test
  public void testSquareContains() {
    assertFalse(this.s1.contains(new CartPt(100, 100)));
    assertTrue(this.s2.contains(new CartPt(55, 60)));
  }

  // test the constructor in the class circle
  @Test(expected=IllegalArgumentException.class)
  public void testCircleException() {
    IShape c = new Circle(new CartPt(30, 30), -10, "blue");
  }
}
