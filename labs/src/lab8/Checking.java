package lab8;

// Represents a checking account
public class Checking extends Account{

    int minimum; // The minimum account balance allowed

    public Checking(int accountNum, int balance, String name, int minimum){
        super(accountNum, balance, name);
        this.minimum = minimum;
    }

    int withdraw(int amount) {
        this.balance = this.balance - amount;
        if (balance < minimum) {
            throw new RuntimeException(String.format("%s is not available", amount));
        }
        return this.balance;
    }

    int deposit(int funds) {
        this.balance = this.balance + funds;
        return this.balance;
    }
}
