package lab7;

// represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {

    Person first;
    ILoBuddy rest;

    ConsLoBuddy(Person first, ILoBuddy rest) {
        this.first = first;
        this.rest = rest;
    }

    public boolean contains(Person buddy) {
        return (first.samePerson(buddy)) ||
            rest.contains(buddy);
    }

    public ILoBuddy flatten(ILoBuddy rsf) {
        if (rsf.contains(first)) {
            return rest.flatten(rsf);
        }
        return rest.flatten(first.getCompleteBuddyNetworkHelper(rsf));
    }

    public int getLength() {
        return 1 + rest.getLength();
    }

    public String toString() {
        return first.username + " " + rest.toString();
    }

    public ILoBuddy inCommon(ILoBuddy buddies) {
        if (buddies.contains(first)) {
            return new ConsLoBuddy(first, rest.inCommon(buddies));
        } else {
            return rest.inCommon(buddies);
        }
    }
}
