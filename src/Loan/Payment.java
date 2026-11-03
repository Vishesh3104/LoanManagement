package src.Loan;

public class Payment {

    private int paymentId;
    private int loanId;
    private double amount_paid;

    private static int nextPaymentId = 1;

    public Payment(int loanId, double amount_paid) {
         this.paymentId = nextPaymentId++;
         this.loanId = loanId;
         this.amount_paid = amount_paid;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public double getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(double amount_paid) {
        this.amount_paid = amount_paid;
    }
}
