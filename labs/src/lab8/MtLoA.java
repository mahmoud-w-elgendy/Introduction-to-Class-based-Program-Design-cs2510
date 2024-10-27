package lab8;

// Represents the empty List of Accounts
public class MtLoA implements ILoA{
    
    MtLoA(){ }

    public Account find(int accountNum) {
        throw new RuntimeException(String.format("Couldn't find an account with account number %s",
            accountNum));
    }

    public ILoA remove(int accountNum) {
        throw new RuntimeException(String.format("Couldn't find an account with account number %s",
            accountNum));
    }
}

