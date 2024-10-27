package lab8;

// Represents a non-empty List of Accounts...
public class ConsLoA implements ILoA {

    Account first;
    ILoA rest;

    public ConsLoA(Account first, ILoA rest){
        this.first = first;
        this.rest = rest;
    }

    public Account find(int accountNum) {
        if (first.accountNum == accountNum) {
            return first;
        }
        return rest.find(accountNum);
    }

    public ILoA remove(int accountNum) {
        if (first.accountNum == accountNum) {
            return rest;
        } else {
            return new ConsLoA(first, rest.remove(accountNum));
        }
    }
}