package lab7;

// represents a Person with a username and a list of buddies
class Person {

    String username;
    ILoBuddy buddies;

    Person(String username) {
        this.username = username;
        this.buddies = new MTLoBuddy();
    }

    void addBuddy(Person buddy){
        if (buddies.contains(buddy)) {
            throw new RuntimeException("Cannot add the same buddy twice");
        }

        this.buddies = new ConsLoBuddy(buddy, this.buddies);
    }

    // returns true if this Person has that as a direct buddy
    boolean hasDirectBuddy(Person that) {
        return buddies.contains(that);
    }

    // returns the number of people who will show up at the party 
    // given by this person
    // Note: the problem definition is not clear on whether to include the host, so I've decided to
    // include him/her
    int partyCount(){
        return this.getCompleteBuddyNetwork().getLength();
    }

    // returns the number of people that are direct buddies 
    // of both this and that person
    int countCommonBuddies(Person that) {
        return buddies.inCommon(that.buddies).getLength();
    }

    // will the given person be invited to a party 
    // organized by this person?
    boolean hasExtendedBuddy(Person that) {
        return !this.samePerson(that) && this.getCompleteBuddyNetwork().contains(that);
    }

    // Get the complete buddy network, including this and all buddies and extended buddies
    public ILoBuddy getCompleteBuddyNetwork() {
        return buddies.flatten(new ConsLoBuddy(this, new MTLoBuddy()));
    }

    public ILoBuddy getCompleteBuddyNetworkHelper(ILoBuddy rsf) {
        return buddies.flatten(new ConsLoBuddy(this, rsf));
    }

    public boolean samePerson(Person that) {
        return this == that; // Intensional equality. The problem definiton doesn't specify which
        // notion of equality to use.
    }
}
