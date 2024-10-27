package lab7;

// represents a list of Person's buddies
interface ILoBuddy {
  boolean contains(Person buddy);
  ILoBuddy flatten(ILoBuddy rsf);
  int getLength();

  // A helper method for debugging
  String toString();
  ILoBuddy inCommon(ILoBuddy buddies);
}
