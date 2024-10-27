package lab10.problem2;

public class StringCreator {
  String string;
  Stack<String> history;

  public StringCreator() {
    string = "";
    history = new Stack<>();
  }

  void add(Character c) {
    history.push(string);
    string = string + c;
  }

  void remove() {
    if (string.isEmpty()) {
      throw new RuntimeException("Cannot remove from an empty string");
    }
    history.push(string);
    string = string.substring(0, string.length() - 1);
  }

  String getString() {
    return string;
  }

  void undo() {
    if (!history.isEmpty()) {
      string = history.pop();
    }
  }
}
