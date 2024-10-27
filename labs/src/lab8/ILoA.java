package lab8;

// Represents a List of Accounts
public interface ILoA{
  Account find(int accountNum);
  ILoA remove(int accountNum);
}

