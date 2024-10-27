package practiceProblems;

interface IBankAccount {};

class CheckingAccount implements IBankAccount {
  int id;
  String name;
  int currentBalance;
  int minimumBalance;

  public CheckingAccount(int id, String name, int currentBalance, int minimumBalance) {
    this.id = id;
    this.name = name;
    this.currentBalance = currentBalance;
    this.minimumBalance = minimumBalance;
  }
}

class SavingsAccount implements IBankAccount {
  int id;
  String name;
  int currentBalance;
  double interestRate;

  public SavingsAccount(int id, String name, int currentBalance, double interestRate) {
    this.id = id;
    this.name = name;
    this.currentBalance = currentBalance;
    this.interestRate = interestRate;
  }
}

class CDAccount implements IBankAccount {
  int id;
  String name;
  int currentBalance;
  double interestRate;
  String date;

  public CDAccount(int id, String name, int currentBalance, double interestRate, String date) {
    this.id = id;
    this.name = name;
    this.currentBalance = currentBalance;
    this.interestRate = interestRate;
    this.date = date;
  }
}


public class exercise4d4 {
  IBankAccount ba1 = new CheckingAccount(1729, "Earl Gray", 1250, 500);
  IBankAccount ba2 = new CDAccount(4104, "Ima Flatt", 10123, 4, "June 1 2005");
  IBankAccount ba3 = new SavingsAccount(2992, "Annie Proulx", 800, 3.5);
}
