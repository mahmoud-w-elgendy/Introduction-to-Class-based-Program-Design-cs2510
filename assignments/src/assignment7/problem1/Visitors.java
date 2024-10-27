package assignment7.problem1;

import tester.Tester;

import java.util.function.BiFunction;
import java.util.function.Function;

interface IArith {
  <R> R accept(IArithVisitor<R> func);
}

class Const implements IArith {
  double num;

  public Const(double num) {
    this.num = num;
  }

  public <R> R accept(IArithVisitor<R> func) {
    return func.visitConst(this);
  }
}

class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;

  public UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  public <R> R accept(IArithVisitor<R> func) {
    return func.visitUnaryFormula(this);
  }
}

class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;

  public BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left,
      IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  public <R> R accept(IArithVisitor<R> func) {
    return func.visitBinaryformula(this);
  }
}

interface IArithVisitor<R> extends Function<IArith, R> {
  R visitConst(Const c);
  R visitUnaryFormula(UnaryFormula uf);
  R visitBinaryformula(BinaryFormula bf);
}

class EvalVisitor implements IArithVisitor<Double> {
  public Double visitConst(Const c) {
    return c.num;
  }

  public Double visitUnaryFormula(UnaryFormula uf) {
    return uf.func.apply(this.apply(uf.child));
  }

  public Double visitBinaryformula(BinaryFormula bf) {
    double leftVal = this.apply(bf.left);
    double rightVal = this.apply(bf.right);
    return bf.func.apply(leftVal, rightVal);
  }

  public Double apply(IArith iArith) {
    return iArith.accept(this);
  }
}

class DoublerVisitor implements IArithVisitor<IArith> {
  public IArith visitConst(Const c) {
    return new Const(2 * c.num);
  }

  public IArith visitUnaryFormula(UnaryFormula uf) {
    return new UnaryFormula(uf.func, uf.name, this.apply(uf.child));
  }

  public IArith visitBinaryformula(BinaryFormula bf) {
    return new BinaryFormula(bf.func, bf.name, this.apply(bf.left), this.apply(bf.right));
  }

  public IArith apply(IArith iArith) {
    return iArith.accept(this);
  }
}

class PrintVisitor implements IArithVisitor<String> {

  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  public String visitUnaryFormula(UnaryFormula uf) {
    return String.format("(%s %s)", uf.name, this.apply(uf.child));
  }

  public String visitBinaryformula(BinaryFormula bf) {
    return String.format("(%s %s %s)", bf.name, this.apply(bf.left), this.apply(bf.right));
  }

  public String apply(IArith iArith) {
    return iArith.accept(this);
  }
}

class NoNegativeResults implements IArithVisitor<Boolean> {

  public Boolean visitConst(Const c) {
    return notNegative(c.num);
  }

  public Boolean visitUnaryFormula(UnaryFormula uf) {
    EvalVisitor eval = new EvalVisitor();
    return this.apply(uf.child) && notNegative(eval.apply(uf)) && notNegative(eval.apply(uf.child));
  }

  public Boolean visitBinaryformula(BinaryFormula bf) {
    EvalVisitor eval = new EvalVisitor();
    return this.apply(bf.left) && this.apply(bf.right) && notNegative(eval.apply(bf))
        && notNegative(eval.apply(bf.left)) && notNegative(eval.apply(bf.right));
  }

  public Boolean apply(IArith iArith) {
    return iArith.accept(this);
  }

  Boolean notNegative(Double d) {
    return Math.signum(d) >= 0.0;
  }
}

class Plus implements BiFunction<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 + d2;
  }
}

class Minus implements BiFunction<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 - d2;
  }
}

class Mul implements BiFunction<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 * d2;
  }
}

class Div implements BiFunction<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 / d2;
  }
}

class Sqr implements Function<Double, Double> {
  public Double apply(Double d) {
    return d * d;
  }
}

class Neg implements Function<Double, Double> {
  public Double apply(Double d) {
    return -d;
  }
}

class ExamplesVisitors {
  IArith one = new Const(1.0);
  IArith neg5 = new UnaryFormula(new Neg(), "neg", new Const(5.0));
  IArith sqr4 = new UnaryFormula(new Sqr(), "sqr", new Const(4.0));

  IArith add5And3 = new BinaryFormula(new Plus(), "plus", new Const(5.0), new Const(3.0));

  IArith sub7And10 = new BinaryFormula(new Minus(), "minus", new Const(7.0), new Const(10.0));

  IArith mul2And3 = new BinaryFormula(new Mul(), "mul", new Const(2.0), new Const(3.0));

  IArith div8By2 = new BinaryFormula(new Div(), "div", new Const(8.0), new Const(2.0));

  // Nested example: (x + y) * (z - w)
  IArith x = new Const(4.0);
  IArith y = new Const(6.0);
  IArith z = new Const(8.0);
  IArith w = new Const(2.0);

  IArith xPlusY = new BinaryFormula(new Plus(), "plus", x, y);

  IArith zMinusW = new BinaryFormula(new Minus(), "minus", z, w);

  IArith nested = new BinaryFormula(new Mul(), "mul", xPlusY, zMinusW);

  void testEval(Tester t) {
    IArithVisitor<Double> eval = new EvalVisitor();
    t.checkInexact(eval.apply(one), 1.0, 0.0001);
    t.checkInexact(eval.apply(neg5), -5.0, 0.0001);
    t.checkInexact(eval.apply(sqr4), 16.0, 0.0001);
    t.checkInexact(eval.apply(add5And3), 8.0, 0.0001);
    t.checkInexact(eval.apply(sub7And10), -3.0, 0.0001);
    t.checkInexact(eval.apply(mul2And3), 6.0, 0.0001);
    t.checkInexact(eval.apply(div8By2), 4.0, 0.0001);
    t.checkInexact(eval.apply(nested), 60.0, 0.0001);
  }

  void testPrint(Tester t) {
    IArithVisitor<String> print = new PrintVisitor();
    t.checkExpect(print.apply(one), "1.0");
    t.checkExpect(print.apply(neg5), "(neg 5.0)");
    t.checkExpect(print.apply(sqr4), "(sqr 4.0)");
    t.checkExpect(print.apply(add5And3), "(plus 5.0 3.0)");
    t.checkExpect(print.apply(sub7And10), "(minus 7.0 10.0)");
    t.checkExpect(print.apply(mul2And3), "(mul 2.0 3.0)");
    t.checkExpect(print.apply(div8By2), "(div 8.0 2.0)");
    t.checkExpect(print.apply(nested), "(mul (plus 4.0 6.0) (minus 8.0 2.0))");
  }

  void testDoubler(Tester t) {
    IArithVisitor<IArith> doubler = new DoublerVisitor();
    t.checkInexact(doubler.apply(one), new Const(2.0), 0.0001);
    t.checkInexact(doubler.apply(neg5), new UnaryFormula(new Neg(), "neg", new Const(10.0)),
        0.0001);

    t.checkInexact(doubler.apply(sqr4), new UnaryFormula(new Sqr(), "sqr", new Const(8.0)), 0.0001);

    t.checkInexact(doubler.apply(add5And3),
        new BinaryFormula(new Plus(), "plus", new Const(10.0), new Const(6.0)), 0.0001);

    t.checkInexact(doubler.apply(sub7And10),
        new BinaryFormula(new Minus(), "minus", new Const(14.0), new Const(20.0)), 0.0001);

    t.checkInexact(doubler.apply(mul2And3),
        new BinaryFormula(new Mul(), "mul", new Const(4.0), new Const(6.0)), 0.0001);

    t.checkInexact(doubler.apply(div8By2),
        new BinaryFormula(new Div(), "div", new Const(16.0), new Const(4.0)), 0.0001);

    t.checkInexact(doubler.apply(nested), new BinaryFormula(new Mul(), "mul",
        new BinaryFormula(new Plus(), "plus", new Const(8.0), new Const(12.0)),
        new BinaryFormula(new Minus(), "minus", new Const(16.0), new Const(4.0))), 0.0001);
  }

  void testNoNegativeResults(Tester t) {
    IArithVisitor<Boolean> noNegatives = new NoNegativeResults();
    t.checkExpect(noNegatives.apply(one), true);
    t.checkExpect(noNegatives.apply(neg5), false); // Negative value
    t.checkExpect(noNegatives.apply(sqr4), true);
    t.checkExpect(noNegatives.apply(add5And3), true);
    t.checkExpect(noNegatives.apply(sub7And10), false); // Subtraction results in negative
    t.checkExpect(noNegatives.apply(mul2And3), true);
    t.checkExpect(noNegatives.apply(div8By2), true);
    t.checkExpect(noNegatives.apply(nested), true);

    // Expression: (plus (sqr 3.0) (mul 2.0 2.0))
    IArith test1 = new BinaryFormula(new Plus(), "plus",
        new UnaryFormula(new Sqr(), "sqr", new Const(3.0)),
        new BinaryFormula(new Mul(), "mul", new Const(2.0), new Const(2.0)));
    t.checkExpect(noNegatives.apply(test1), true);

    // Expression: (minus (sqr 2.0) (mul 4.0 2.0))
    IArith test2 = new BinaryFormula(new Minus(), "minus",
        new UnaryFormula(new Sqr(), "sqr", new Const(2.0)),
        new BinaryFormula(new Mul(), "mul", new Const(4.0), new Const(2.0)));
    t.checkExpect(noNegatives.apply(test2), false); // Subtraction results in negative

    // Expression: (mul (plus 10.0 10.0) (minus 20.0 10.0))
    IArith test3 = new BinaryFormula(new Mul(), "mul",
        new BinaryFormula(new Plus(), "plus", new Const(10.0), new Const(10.0)),
        new BinaryFormula(new Minus(), "minus", new Const(20.0), new Const(10.0)));
    t.checkExpect(noNegatives.apply(test3), true);
  }

}
