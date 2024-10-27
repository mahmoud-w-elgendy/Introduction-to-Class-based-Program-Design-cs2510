package lab8;

// Represents a credit line account
public class Credit extends Account{

    int creditLine;  // Maximum amount accessible
    double interest; // The interest rate charged
    
    public Credit(int accountNum, int balance, String name, int creditLine, double interest){
        super(accountNum, balance, name);
        this.creditLine = creditLine;
        this.interest = interest;
    }

    int withdraw(int amount) {
        this.balance = this.balance + amount;
        if (balance > creditLine) {
            throw new RuntimeException(String.format("%s is not available", amount));
        }
        return this.balance;
    }

    int deposit(int funds) {
        this.balance = this.balance - funds;
        if (this.balance < 0) {
            throw new RuntimeException(String.format("%s is not available for deposit", funds));
        }
        return this.balance;
    }
}
