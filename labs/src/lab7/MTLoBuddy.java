package lab7;

// represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {
    MTLoBuddy() {}

    public boolean contains(Person buddy) { return false; }

    public ILoBuddy flatten(ILoBuddy rsf) { return rsf; }

    public int getLength() { return 0; }

    public String toString() { return ""; }

    public ILoBuddy inCommon(ILoBuddy buddies) {
        return this;
    }
}
