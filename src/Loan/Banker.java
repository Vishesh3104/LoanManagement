
package src.Loan;

public class Banker {

    private String banker_name;
    private int bankerId;
    private String username;
    private String password;

    private static int nextBankerId = 101;

   public Banker(String banker_name, String username, String password) {
    this.bankerId = nextBankerId++;
    this.banker_name = banker_name;
    this.username = username;
    this.password = password;
}

    public int getBankerId() {
        return bankerId;
    }

    public String getBanker_name() {
        return banker_name;
    }

    public void setBanker_name(String banker_name) {
        this.banker_name = banker_name;
    }

    public String getUsername() {
        return username;
    }

    public boolean login(String enteredUsername,
                         String enteredPassword) {
        return username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }
}
