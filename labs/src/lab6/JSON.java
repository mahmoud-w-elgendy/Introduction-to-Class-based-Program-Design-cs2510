package lab6;

import tester.Tester;

// a json value
interface JSON {
  <T> T accept(JSONVISITOR<T> visitor);
}

// no value
class JSONBlank implements JSON {
  public <T> T accept(JSONVISITOR<T> visitor) {
    return visitor.visitBlank(this);
  }
}

// a number
class JSONNumber implements JSON {
  int number;

  JSONNumber(int number) {
    this.number = number;
  }

  public <T> T accept(JSONVISITOR<T> visitor) {
    return visitor.visitNumber(this);
  }
}

// a boolean
class JSONBool implements JSON {
  boolean bool;

  JSONBool(boolean bool) {
    this.bool = bool;
  }

  public <T> T accept(JSONVISITOR<T> visitor) {
    return visitor.visitBool(this);
  }
}

// a string
class JSONString implements JSON {
  String str;

  JSONString(String str) {
    this.str = str;
  }

  public <T> T accept(JSONVISITOR<T> visitor) {
    return visitor.visitString(this);
  }
}

// a list of JSON values
class JSONList implements JSON {
  IList<JSON> values;

  JSONList(IList<JSON> values) {
    this.values = values;
  }

  public <T> T accept(JSONVISITOR<T> visitor) {
    return visitor.visitList(this);
  }
}

// a list of JSON pairs
class JSONObject implements JSON {
  IList<Pair<String, JSON>> pairs;

  JSONObject(IList<Pair<String, JSON>> pairs) {
    this.pairs = pairs;
  }

  public <T> T accept(JSONVISITOR<T> visitor) {
    return visitor.visitObject(this);
  }
}

// generic pairs
class Pair<X, Y> {
  X x;
  Y y;

  Pair(X x, Y y) {
    this.x = x;
    this.y = y;
  }

  // To avoid accessing the field of a field in FindPairXEquals
  boolean xEquals(X other) {
    return x.equals(other);
  }
}

interface JSONVISITOR<T> extends IFunc<JSON, T> {
  T visitBlank(JSONBlank blank);
  T visitNumber(JSONNumber number);
  T visitBool(JSONBool bool);
  T visitString(JSONString str);
  T visitList(JSONList list);
  T visitObject(JSONObject obj);
}

class JSONToNumber implements JSONVISITOR<Integer> {

  public Integer visitBlank(JSONBlank blank) {
    return 0;
  }

  public Integer visitNumber(JSONNumber number) {
    return number.number;
  }

  public Integer visitBool(JSONBool bool) {
    return (bool.bool) ? 1 : 0;
  }

  public Integer visitString(JSONString str) {
    return str.str.length();
  }

  public Integer visitList(JSONList list) {
    return list.values.map(this)
            .foldr(new SumNumbers(), 0);
  }

  public Integer visitObject(JSONObject obj) {
    return obj.pairs.foldr(new SumPairYJSONToNumber(), 0);
  }

  public Integer apply(JSON input) {
    return input.accept(this);
  }
}

class SumPairYJSONToNumber implements IFunc2<Pair<String, JSON>, Integer, Integer> {
  public Integer apply(Pair<String, JSON> arg1, Integer arg2) {
    return new JSONToNumber().apply(arg1.y) + arg2;
  }
}

class JSONFind implements JSONVISITOR<JSON> {
  String toFind;

  public JSONFind(String toFind) {
    this.toFind = toFind;
  }

  public JSON visitBlank(JSONBlank blank) {
    return blank;
  }

  public JSON visitNumber(JSONNumber number) {
    return new JSONBlank();
  }

  public JSON visitBool(JSONBool bool) {
    return new JSONBlank();
  }

  public JSON visitString(JSONString str) {
    return new JSONBlank();
  }

  public JSON visitList(JSONList list) {
    return list.values.findSolutionOrElse(new JSONFind(toFind), new NotBlank(), new JSONBlank());
  }

  public JSON visitObject(JSONObject obj) {
    class FindPairXEquals implements IFunc<Pair<String, JSON>, JSON> {
      public JSON apply(Pair<String, JSON> input) {
        if (input.xEquals(toFind)) {
          return input.y;
        } else {
          return new JSONFind(toFind).apply(input.y);
        }
      }
    }

    return obj.pairs.findSolutionOrElse(new FindPairXEquals(), new NotBlank(), new JSONBlank());
  }

  public JSON apply(JSON input) {
    return input.accept(this);
  }
}

class NotBlank implements IPred<JSON> {
  public Boolean apply(JSON input) {
    // Could've added an isBlank method to JSON instead
    // but then, to prevent code duplication, you'd also need to add an abstract JSON class...
    return !(input instanceof JSONBlank);
  }
}


class ExamplesJSON {
  JSON blank = new JSONBlank();
  JSON num3 = new JSONNumber(3);
  JSON numMinus5 = new JSONNumber(-5);
  JSON boolTrue = new JSONBool(true);
  JSON boolFalse = new JSONBool(false);
  JSON hello = new JSONString("Hello");
  JSON obj = new JSONObject(new ConsList<>(new Pair<>("x", new JSONBlank()),
      new ConsList<>(new Pair<>("seven", new JSONNumber(7)), new ConsList<>(
          new Pair<>("string", new JSONString("nowergh")), new MtList<>())
      )));

  IList<JSON> loj0 = new MtList<JSON>();
  IList<JSON> loj1 = new ConsList<JSON>(blank, loj0);
  IList<JSON> loj2 = new ConsList<JSON>(num3, loj1);
  IList<JSON> loj3 = new ConsList<JSON>(boolFalse, loj2);
  IList<JSON> loj4 = new ConsList<JSON>(hello, loj3);
  IList<JSON> loj5 = new ConsList<JSON>(boolTrue, loj4);
  IList<JSON> loj6 = new ConsList<JSON>(numMinus5, loj5);
  IList<JSON> loj7 = new ConsList<JSON>(blank, loj6);
  IList<JSON> loj8 = new ConsList<JSON>(new JSONList(loj3), loj0);
  IList<JSON> loj9 = new ConsList<>(num3, new ConsList<>(obj, loj0));

  boolean testJSONToNumber(Tester t) {
    JSONToNumber funcObj = new JSONToNumber();
    return t.checkExpect(loj0.map(funcObj), new MtList<Integer>()) &&
        t.checkExpect(loj1.map(funcObj), new ConsList<Integer>(0, new MtList<Integer>())) &&
        t.checkExpect(loj7.map(funcObj),
            new ConsList<Integer>(0, new ConsList<Integer>(-5, new ConsList<Integer>(1,
                new ConsList<Integer>(5, new ConsList<>(0, new ConsList<Integer>(3,
                    new ConsList<Integer>(0, new MtList<Integer>())))
                ))))
            ) &&
        t.checkExpect(loj8.map(funcObj), new ConsList<Integer>(3, new MtList<Integer>())) &&
        t.checkExpect(loj9.map(funcObj), new ConsList<>(3, new ConsList<Integer>(14,
            new MtList<>())));
  }

  boolean testJSONFind(Tester t) {
    JSON lstWithObj = new JSONList(new ConsList<>(hello, new ConsList<>(blank,
        new ConsList<>(obj,
        new MtList<>()))));
    JSON nestedObjXBefore = new JSONObject(new ConsList<>(new Pair<>("Aha!", blank),
        new ConsList<>(new Pair<>("seven", boolFalse), new ConsList<>(new Pair<>("obj", obj),
            new MtList<>()))));
    JSON nestedObjXAfter = new JSONObject(new ConsList<>(new Pair<>("obj", obj),
        new ConsList<>(new Pair<>("string", boolFalse), new ConsList<>(new Pair<>("Aha!", blank),
            new MtList<>()))));
    JSON nestedObjXInside = new JSONObject(new ConsList<>(new Pair<>("Aha!", blank),
        new ConsList<>(new Pair<>("string", obj), new ConsList<>(new Pair<>("seven", boolFalse),
            new MtList<>()))));

    return t.checkExpect(new JSONFind("x").apply(boolTrue), new JSONBlank()) &&
        t.checkExpect(new JSONFind("string").apply(obj), new JSONString("nowergh")) &&
        t.checkExpect(new JSONFind("string").apply(lstWithObj), new JSONString("nowergh")) &&
        t.checkExpect(new JSONFind("seven").apply(nestedObjXBefore), boolFalse) &&
        t.checkExpect(new JSONFind("string").apply(nestedObjXAfter), new JSONString("nowergh")) &&
        t.checkExpect(new JSONFind("string").apply(nestedObjXInside), obj);

  }
}