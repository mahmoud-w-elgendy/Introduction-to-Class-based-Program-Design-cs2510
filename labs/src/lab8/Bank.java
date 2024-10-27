package lab8;

// Represents a Bank with list of accounts
public class Bank {
    
    String name;
    ILoA accounts;
    
    public Bank(String name){
        this.name = name;

        // Each bank starts with no accounts
        this.accounts = new MtLoA();
    }

    // EFFECT: Add a new account to this Bank
    void add(Account acct){
        this.accounts = new ConsLoA(acct, this.accounts);
    }

    int deposit(int funds, int accountNum) {
        Account acc = accounts.find(accountNum);
        return acc.deposit(funds);
    }

    int withdraw(int amount, int accountNum) {
        Account acc = accounts.find(accountNum);
        return acc.withdraw(amount);
    }


    // EFFECT: Remove the given account from this Bank
    void removeAccount(int acctNo){
        this.accounts = this.accounts.remove(acctNo);
    }
}
