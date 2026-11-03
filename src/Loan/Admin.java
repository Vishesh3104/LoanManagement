
package src.Loan;

public class Admin {

    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String enteredUsername, String enteredPassword) {
        return username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    public String getUsername() {
        return username;
    }
}
