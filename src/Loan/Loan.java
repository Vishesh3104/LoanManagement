package src.Loan;

public class Loan {

private int loanId;
private double loan_Amount;
private double interest_rate;
private int months;
private double emi;
private int emi_Paid;

private static int nextLoanId = 1001;
private LoanStatus status = LoanStatus.ACTIVE;
private User user;
private Banker banker;

    public Loan(double loan_Amount, double interest_rate,
            int months, User user, Banker banker) {

    this.loanId = nextLoanId++;
    this.loan_Amount = loan_Amount;
    this.interest_rate = interest_rate;
    this.months = months;
    this.user = user;
    this.banker = banker;
    this.emi_Paid = 0;

    calculateEMI();
}

    public LoanStatus getStatus() {
    return status;
}

public double getTotalRepayment() {
    return emi * months;
}

public double getTotalInterest() {
    return getTotalRepayment() - loan_Amount;
}

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public double getLoan_Amount() {
        return loan_Amount;
    }

    public void setLoan_Amount(double loan_Amount) {
        this.loan_Amount = loan_Amount;
    }

    public double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public double getEmi() {
        return emi;
    }

    public void setEmi(double emi) {
        this.emi = emi;
    }

    public int getEmi_Paid() {
        return emi_Paid;
    }

    public void setEmi_Paid(int emi_Paid) {
        this.emi_Paid = emi_Paid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Banker getBanker() {
        return banker;
    }

    public void setBanker(Banker banker) {
        this.banker = banker;
    }

    public void calculateEMI() {
    double monthlyRate = interest_rate / 1200.0;

    if (months <= 0 || loan_Amount <= 0 || interest_rate < 0) {
        throw new IllegalArgumentException("Invalid loan details.");
    }

    if (monthlyRate == 0) {
        emi = loan_Amount / months;
    } else {
        double factor = Math.pow(1 + monthlyRate, months);
        emi = loan_Amount * monthlyRate * factor / (factor - 1);
    }
}

public boolean payEMI() {
    if (status == LoanStatus.CLOSED) {
        System.out.println("Loan is already closed.");
        return false;
    }

    if (emi_Paid >= months) {
        System.out.println("All EMIs are already paid.");
        status = LoanStatus.CLOSED;
        return false;
    }

    emi_Paid++;

    System.out.println("EMI paid successfully.");
    System.out.println("EMIs remaining: " + getRemainingEMIs());

    if (emi_Paid >= months) {
        status = LoanStatus.CLOSED;
        System.out.println("Loan closed successfully.");
    }

    return true;
}
public boolean closeLoan() {
    if (emi_Paid >= months) {
        status = LoanStatus.CLOSED;
        System.out.println("Loan closed successfully.");
        return true;
    }

    System.out.println("Cannot close loan. EMIs are still pending.");
    return false;
}

public int getRemainingEMIs() {
    return months - emi_Paid;
}
}
