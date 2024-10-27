package assignment8.problem2;

import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet =
      new ArrayList<Character>(Arrays.asList(
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
          'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
          't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> alphabetCopy = new ArrayList<>(alphabet);
    ArrayList<Character> code = new ArrayList<>();
    for (int i = alphabet.size(); i > 0; i--) {
      Character randomChar = alphabetCopy.remove(rand.nextInt(i));
      code.add(randomChar);
    }
    return code;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    return convertString(source, alphabet, code);
  }

  // produce a decoded String from the given String
  String decode(String msg) {
    return convertString(msg, code, alphabet);
  }

  String convertString(String s, ArrayList<Character> from, ArrayList<Character> to) {
    // Apparently concatanating strings is not the most efficient solution,
    // but it works, and we didn't learn about StringBuilder
    String newStr = "";
    int maxIndex = to.size() - 1;
    for (Character c: s.toCharArray()) {
      int charPos = from.indexOf(c);
      if (charPos == -1 || charPos > maxIndex) {
        throw new RuntimeException("Character not in bounds");
      }
      newStr =  newStr + to.get(charPos);
    }
    return newStr;
  }
}


class ExamplesPermutation {
  PermutationCode c0 = new PermutationCode(new ArrayList<Character>(Arrays.asList(
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
      't', 'u', 'v', 'w', 'x', 'y', 'z')));
  PermutationCode c1 = new PermutationCode(new ArrayList<Character>(Arrays.asList(
      'z', 'y', 'x', 'w', 'v', 'u', 't', 's', 'r', 'q',
      'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g',
      'f', 'e', 'd', 'c', 'b', 'a')));
  PermutationCode c2 = new PermutationCode(new ArrayList<Character>(Arrays.asList(
      'e', 'd', 'c', 'b', 'a', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z')));
  PermutationCode c3 = new PermutationCode();

  void testDecode(Tester t) {
    t.checkExpect(c2.decode(""), "");
    t.checkExpect(c0.decode("hello"), "hello");
    t.checkExpect(c1.decode("abcde"), "zyxwv");
    t.checkExpect(c2.decode("eda"), "abe");
  }

  void testEncode(Tester t) {
    t.checkExpect(c2.encode(""), "");
    t.checkExpect(c0.encode("hello"), "hello");
    t.checkExpect(c1.encode("zyxwv"), "abcde");
    t.checkExpect(c2.encode("abe"), "eda");
  }

  void testInitDecoder(Tester t) {
    t.checkExpect(c3.encode(""), "");
    t.checkExpect(c3.decode(""), "");
    t.checkExpect(c3.decode(c3.encode("hello")), "hello");
    t.checkExpect(c3.decode(c3.encode("abcdefghijklmnopqrstuvwxyz")),
        "abcdefghijklmnopqrstuvwxyz");


  }

}