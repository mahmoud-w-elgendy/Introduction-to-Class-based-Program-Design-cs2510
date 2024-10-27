package lab8;

import tester.*;

// Bank Account Examples and Tests
public class Examples {

    public Examples(){ reset(); }
    
    Account check1;
    Account savings1;
    Account credit1;
    Account check2;
    Account savings2;
    Account credit2;
    Bank emptyBank;
    Bank bank;
    
    // Initializes accounts to use for testing with effects.
    // We place inside reset() so we can "reuse" the accounts
    public void reset(){
        
        // Initialize the account examples
        check1 = new Checking(1, 100, "First Checking Account", 20);
        savings1 = new Savings(4, 200, "First Savings Account", 2.5);
        credit1 = new Credit(2, 300, "Firsr Credit Line Account",
            1000, 1.5);
        check2 = new Checking(3, 20, "Second Checking Account", 20);
        savings2 = new Savings(5, 1, "Second Savings Account", 2.5);
        credit2 = new Credit(6, 30, "Second Credit Line Account",
            90, 1.5);

        emptyBank = new Bank("The Bank");
        bank = new Bank("The Real Bank");
        bank.add(credit1);
        bank.add(check1);
        bank.add(savings1);
        bank.add(credit2);
    }
    
    // Tests the exceptions we expect to be thrown when
    //   performing an "illegal" action.
    public void testExceptions(Tester t){
        reset();
        t.checkException("Test for invalid Checking withdraw",
                         new RuntimeException("1000 is not available"),
                         this.check1,
                         "withdraw",
                         1000);
        t.checkException("Test for invalid Saving withdraw",
            new RuntimeException("300 is not available"),
            this.savings1,
            "withdraw",
            300);
        t.checkException("Test for invalid Credit withdraw",
            new RuntimeException("800 is not available"),
            this.credit1,
            "withdraw",
            800);

        t.checkException("Test for invalid Credit deposit",
            new RuntimeException("100 is not available for deposit"),
            this.credit2,
            "deposit",
            100);

        t.checkException("Test for invalid deposit from Bank",
            new RuntimeException("Couldn't find an account with account number 1"),
            this.emptyBank,
            "deposit",
            100, 1);

        t.checkException("Test for invalid deposit from Bank",
            new RuntimeException("100 is not available for deposit"),
            this.bank,
            "deposit",
            100, 6);

        t.checkException("Test for invalid withdraw from Bank",
            new RuntimeException("Couldn't find an account with account number 1"),
            this.emptyBank,
            "withdraw",
            100, 1);

        t.checkException("Test for invalid withdraw from Bank",
            new RuntimeException("1000 is not available"),
            this.bank,
            "withdraw",
            1000, 6);

        t.checkException("Test for invalid removeAccount from Bank",
            new RuntimeException("Couldn't find an account with account number 1"),
            this.emptyBank,
            "removeAccount",
            1);
        bank.removeAccount(credit2.accountNum);
        t.checkException("Test for invalid removeAccount from Bank",
            new RuntimeException("Couldn't find an account with account number 6"),
            this.bank,
            "removeAccount",
            6);

        reset();
    }
    
    // Test the withdraw method(s)
    public void testWithdraw(Tester t){
        reset();
        t.checkExpect(check1.withdraw(25), 75);
        t.checkExpect(check1, new Checking(1, 75, "First Checking Account", 20));
        t.checkExpect(savings1.withdraw(100), 100);
        t.checkExpect(savings1, new Savings(4, 100, "First Savings Account", 2.5));
        t.checkExpect(credit1.withdraw(600), 900);
        t.checkExpect(credit1, new Credit(2, 900, "Firsr Credit Line Account",
            1000, 1.5));
        t.checkExpect(credit1.withdraw(50), 950);
        t.checkExpect(credit1, new Credit(2, 950, "Firsr Credit Line Account",
            1000, 1.5));
        reset();
    }

    // Test the deposit method(s)
    public void testDeposit(Tester t){
        reset();
        t.checkExpect(check1.deposit(25), 125);
        t.checkExpect(check1, new Checking(1, 125, "First Checking Account", 20));
        t.checkExpect(savings1.deposit(100), 300);
        t.checkExpect(savings1, new Savings(4, 300, "First Savings Account", 2.5));
        t.checkExpect(credit1.deposit(150), 150);
        t.checkExpect(credit1, new Credit(2, 150, "Firsr Credit Line Account",
            1000, 1.5));
        t.checkExpect(credit1.deposit(50), 100);
        t.checkExpect(credit1, new Credit(2, 100, "Firsr Credit Line Account",
            1000, 1.5));
        reset();
    }

    public void testAdd(Tester t) {
        reset();
        emptyBank.add(check1);
        t.checkExpect(emptyBank.accounts, new ConsLoA(check1, new MtLoA()));
        emptyBank.add(credit1);
        t.checkExpect(emptyBank.accounts, new ConsLoA(credit1, new ConsLoA(check1, new MtLoA())));
        emptyBank.add(savings1);
        t.checkExpect(emptyBank.accounts, new ConsLoA(savings1, new ConsLoA(credit1,
            new ConsLoA(check1, new MtLoA()))));
        reset();
    }

    public void testDepositBank(Tester t) {
        reset();
        t.checkExpect(bank.deposit(25, 1), 125);
        t.checkExpect(check1, new Checking(1, 125, "First Checking Account", 20));
        t.checkExpect(bank.deposit(100, 4), 300);
        t.checkExpect(savings1, new Savings(4, 300, "First Savings Account", 2.5));
        t.checkExpect(bank.deposit(150, 2), 150);
        t.checkExpect(credit1, new Credit(2, 150, "Firsr Credit Line Account",
            1000, 1.5));
        t.checkExpect(bank.deposit(50, 2), 100);
        t.checkExpect(credit1, new Credit(2, 100, "Firsr Credit Line Account",
            1000, 1.5));
        reset();
    }

    public void testWithdrawBank(Tester t){
        reset();
        t.checkExpect(bank.withdraw(25, 1), 75);
        t.checkExpect(check1, new Checking(1, 75, "First Checking Account", 20));
        t.checkExpect(bank.withdraw(100, 4), 100);
        t.checkExpect(savings1, new Savings(4, 100, "First Savings Account", 2.5));
        t.checkExpect(bank.withdraw(600, 2), 900);
        t.checkExpect(credit1, new Credit(2, 900, "Firsr Credit Line Account",
            1000, 1.5));
        t.checkExpect(bank.withdraw(50, 2), 950);
        t.checkExpect(credit1, new Credit(2, 950, "Firsr Credit Line Account",
            1000, 1.5));
        reset();
    }

    void testRemoveAccount(Tester t) {
        reset();
        bank.removeAccount(check1.accountNum);
        bank.removeAccount(credit1.accountNum);
        bank.removeAccount(savings1.accountNum);
        t.checkExpect(bank.accounts, new ConsLoA(credit2, new MtLoA()));
        bank.removeAccount(credit2.accountNum);
        t.checkExpect(bank.accounts, emptyBank.accounts);
        reset();
    }
}
