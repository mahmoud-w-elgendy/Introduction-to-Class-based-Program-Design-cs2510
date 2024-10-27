package lab8;

// Represents a savings account
public class Savings extends Account{

    double interest; // The interest rate

    public Savings(int accountNum, int balance, String name, double interest){
        super(accountNum, balance, name);
        this.interest = interest;
    }

    int withdraw(int amount) {
        this.balance = this.balance - amount;
        // Must maintain a positive balance.
        if (this.balance < 1) {
            throw new RuntimeException(String.format("%s is not available", amount));
        }
        return this.balance;
    }

    int deposit(int funds) {
        this.balance = this.balance + funds;
        return this.balance;
    }
}
