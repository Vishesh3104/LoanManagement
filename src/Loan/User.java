package src.Loan;

public class User {

   private String name;
   private int userId;
private static int nextUserId = 1;

    public User(String name) {
    this.name = name;
    this.userId = nextUserId++;
    }
    public String getName() {
        return name;
    }
  
    public void setName(String name) {
        this.name = name;
    }
   
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
