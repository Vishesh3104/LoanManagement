package src.Loan;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Loan> loans = new ArrayList<>();
        ArrayList<Payment> payments = new ArrayList<>();
        ArrayList<Banker> bankers = new ArrayList<>();
        Admin admin = new Admin("admin", "admin123");
        boolean running = true;

        while (running) {
            System.out.println("\n===== LOAN MANAGEMENT SYSTEM =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Banker Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid choice. Enter a number.");
                sc.nextLine();
                continue;
            }
            int loginChoice = sc.nextInt();
            sc.nextLine();

            switch (loginChoice) {
                case 1: {
                    boolean authenticated = false;
                    while (!authenticated) {
                        System.out.print("Admin username (or type back): ");
                        String username = sc.nextLine().trim();
                        if (username.equalsIgnoreCase("back")) break;
                        System.out.print("Admin password (or type back): ");
                        String password = sc.nextLine();
                        if (password.equalsIgnoreCase("back")) break;
                        if (admin.login(username, password)) {
                            System.out.println("Admin login successful!");
                            adminDashboard(sc, loans, payments, bankers);
                            authenticated = true;
                        } else {
                            System.out.println("Invalid credentials. Try again or type back.");
                        }
                    }
                    break;
                }
                case 2: {
                    Banker loggedInBanker = bankerLogin(sc, bankers);
                    if (loggedInBanker != null) {
                        System.out.println("Welcome, " + loggedInBanker.getBanker_name() + "!");
                        System.out.println("Banker ID: " + loggedInBanker.getBankerId());
                        bankerMenu(sc, loans, payments, loggedInBanker);
                        System.out.println("Banker logged out.");
                    }
                    break;
                }
                case 3:
                    System.out.println("Exiting Loan Management System.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Choose 1, 2, or 3.");
            }
        }
        sc.close();
    }

    private static Banker bankerLogin(Scanner sc, ArrayList<Banker> bankers) {
        while (true) {
            System.out.print("Banker username (or type back): ");
            String username = sc.nextLine().trim();
            if (username.equalsIgnoreCase("back")) return null;

            Banker matchedBanker = null;
            for (Banker banker : bankers) {
                if (banker.getUsername().equalsIgnoreCase(username)) {
                    matchedBanker = banker;
                    break;
                }
            }
            if (matchedBanker == null) {
                System.out.println("Username not found. Try again or type back.");
                continue;
            }

            System.out.print("Banker password (or type back): ");
            String password = sc.nextLine();
            if (password.equalsIgnoreCase("back")) return null;
            if (matchedBanker.login(username, password)) return matchedBanker;
            System.out.println("Incorrect password. Try again.");
        }
    }

    private static void bankerMenu(Scanner sc, ArrayList<Loan> loans,
                                   ArrayList<Payment> payments, Banker loggedInBanker) {
        int choice = 0;
        do {
            System.out.println("\n===== LOAN MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Loan");
            System.out.println("2. View My Loans");
            System.out.println("3. Pay EMI");
            System.out.println("4. View Payment History");
            System.out.println("5. Close Loan");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number from 1 to 6.");
                sc.nextLine();
                continue;
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: {
                    String name;
                    while (true) {
                        System.out.print("Enter customer name: ");
                        name = sc.nextLine().trim();
                        if (name.matches("[a-zA-Z ]+") && name.matches(".*[a-zA-Z].*")) break;
                        System.out.println("Invalid name! Use letters and spaces only. Try again.");
                    }

                    double amount;
                    while (true) {
                        System.out.print("Enter loan amount: ");
                        if (!sc.hasNextDouble()) {
                            System.out.println("Invalid input! Please enter a numeric amount.");
                            sc.nextLine();
                            continue;
                        }
                        amount = sc.nextDouble();
                        sc.nextLine();
                        if (Double.isFinite(amount) && amount > 0) break;
                        System.out.println("Loan amount must be greater than zero. Try again.");
                    }

                    int months;
                    while (true) {
                        System.out.println("Choose loan tenure:");
                        System.out.println("1. 12 months");
                        System.out.println("2. 24 months");
                        System.out.println("3. 36 months");
                        System.out.println("4. 60 months");
                        System.out.print("Enter choice: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input! Enter a number from 1 to 4.");
                            sc.nextLine();
                            continue;
                        }
                        int tenureChoice = sc.nextInt();
                        sc.nextLine();
                        switch (tenureChoice) {
                            case 1: months = 12; break;
                            case 2: months = 24; break;
                            case 3: months = 36; break;
                            case 4: months = 60; break;
                            default:
                                System.out.println("Invalid tenure choice. Try again.");
                                continue;
                        }
                        break;
                    }

                    User user = new User(name);
                    Loan loan = new Loan(amount, 7.2, months, user, loggedInBanker);
                    loans.add(loan);
                    System.out.println("Loan created successfully!");
                    System.out.println("Loan ID: " + loan.getLoanId());
                    System.out.println("Assigned Banker ID: " + loggedInBanker.getBankerId());
                    break;
                }
                case 2: {
                    boolean found = false;
                    for (Loan loan : loans) {
                        if (!ownsLoan(loan, loggedInBanker)) continue;
                        printLoan(loan);
                        found = true;
                    }
                    if (!found) System.out.println("No loans found for your account.");
                    break;
                }
                case 3: {
                    System.out.print("Enter loan ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid loan ID! Please enter a number.");
                        sc.nextLine();
                        break;
                    }
                    int loanId = sc.nextInt();
                    sc.nextLine();
                    Loan loan = findOwnedLoan(loans, loanId, loggedInBanker);
                    if (loan == null) {
                        System.out.println("Loan not found in your account.");
                    } else if (loan.payEMI()) {
                        payments.add(new Payment(loanId, loan.getEmi()));
                        System.out.println("Payment record created.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("Enter loan ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid loan ID! Please enter a number.");
                        sc.nextLine();
                        break;
                    }
                    int loanId = sc.nextInt();
                    sc.nextLine();
                    if (findOwnedLoan(loans, loanId, loggedInBanker) == null) {
                        System.out.println("Loan not found in your account.");
                        break;
                    }
                    boolean found = false;
                    for (Payment payment : payments) {
                        if (payment.getLoanId() == loanId) {
                            System.out.println("\n--- Payment Details ---");
                            System.out.println("Payment ID: " + payment.getPaymentId());
                            System.out.println("Loan ID: " + payment.getLoanId());
                            System.out.printf("Amount Paid: Rs. %.2f%n", payment.getAmount_paid());
                            found = true;
                        }
                    }
                    if (!found) System.out.println("No payment history found for Loan ID: " + loanId);
                    break;
                }
                case 5: {
                    System.out.print("Enter loan ID to close: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid loan ID! Please enter a number.");
                        sc.nextLine();
                        break;
                    }
                    int loanId = sc.nextInt();
                    sc.nextLine();
                    Loan loan = findOwnedLoan(loans, loanId, loggedInBanker);
                    if (loan == null) System.out.println("Loan not found in your account.");
                    else loan.closeLoan();
                    break;
                }
                case 6:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Choose 1-6.");
            }
        } while (choice != 6);
    }

    private static boolean ownsLoan(Loan loan, Banker banker) {
        return loan.getBanker() != null
                && loan.getBanker().getBankerId() == banker.getBankerId();
    }

    private static Loan findOwnedLoan(ArrayList<Loan> loans, int loanId, Banker banker) {
        for (Loan loan : loans) {
            if (loan.getLoanId() == loanId && ownsLoan(loan, banker)) return loan;
        }
        return null;
    }

    private static void printLoan(Loan loan) {
        System.out.println("\nLoan ID: " + loan.getLoanId());
        System.out.println("Customer: " + loan.getUser().getName());
        System.out.printf("Amount: Rs. %.2f%n", loan.getLoan_Amount());
        System.out.printf("Monthly EMI: Rs. %.2f%n", loan.getEmi());
        System.out.println("EMIs paid: " + loan.getEmi_Paid());
        System.out.println("EMIs remaining: " + loan.getRemainingEMIs());
        System.out.println("Loan Status: " + loan.getStatus());
        System.out.printf("Total Repayment: Rs. %.2f%n", loan.getTotalRepayment());
        System.out.printf("Total Interest: Rs. %.2f%n", loan.getTotalInterest());
    }

    public static void adminDashboard(Scanner sc, ArrayList<Loan> loans,
                                      ArrayList<Payment> payments, ArrayList<Banker> bankers) {
        int choice = 0;
        do {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. Create Banker");
            System.out.println("2. View All Bankers");
            System.out.println("3. View All Loans");
            System.out.println("4. View All Payments");
            System.out.println("5. View System Statistics");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Enter a number.");
                sc.nextLine();
                continue;
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: {
                    String name;
                    while (true) {
                        System.out.print("Enter banker name: ");
                        name = sc.nextLine().trim();
                        if (name.matches("[a-zA-Z ]+") && name.matches(".*[a-zA-Z].*")) break;
                        System.out.println("Invalid banker name! Use letters and spaces only. Try again.");
                    }

                    System.out.print("Create username: ");
                    String username = sc.nextLine().trim();
                    if (username.isEmpty()) {
                        System.out.println("Username cannot be empty.");
                        break;
                    }

                    boolean exists = false;
                    for (Banker banker : bankers) {
                        if (banker.getUsername().equalsIgnoreCase(username)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        System.out.println("Username already exists.");
                        break;
                    }

                    System.out.print("Create password: ");
                    String password = sc.nextLine();
                    if (password.trim().isEmpty()) {
                        System.out.println("Password cannot be empty.");
                        break;
                    }

                    Banker newBanker = new Banker(name, username, password);
                    bankers.add(newBanker);
                    System.out.println("Banker created successfully!");
                    System.out.println("Banker ID: " + newBanker.getBankerId());
                    break;
                }
                case 2:
                    if (bankers.isEmpty()) {
                        System.out.println("No bankers found.");
                    } else {
                        for (Banker banker : bankers) {
                            System.out.println("\nBanker ID: " + banker.getBankerId());
                            System.out.println("Name: " + banker.getBanker_name());
                            System.out.println("Username: " + banker.getUsername());
                        }
                    }
                    break;
                case 3:
                    if (loans.isEmpty()) {
                        System.out.println("No loans found.");
                    } else {
                        for (Loan loan : loans) {
                            System.out.println("\nLoan ID: " + loan.getLoanId());
                            System.out.println("Customer: " + loan.getUser().getName());
                            System.out.printf("Amount: Rs. %.2f%n", loan.getLoan_Amount());
                            System.out.println("Banker: " + (loan.getBanker() == null
                                    ? "Unassigned" : loan.getBanker().getBanker_name()));
                            System.out.println("Status: " + loan.getStatus());
                        }
                    }
                    break;
                case 4:
                    if (payments.isEmpty()) {
                        System.out.println("No payments found.");
                    } else {
                        for (Payment payment : payments) {
                            System.out.println("\nPayment ID: " + payment.getPaymentId());
                            System.out.println("Loan ID: " + payment.getLoanId());
                            System.out.printf("Amount: Rs. %.2f%n", payment.getAmount_paid());
                        }
                    }
                    break;
                case 5: {
                    int active = 0;
                    int closed = 0;
                    double totalPrincipal = 0;
                    for (Loan loan : loans) {
                        totalPrincipal += loan.getLoan_Amount();
                        if (loan.getStatus() == LoanStatus.ACTIVE) active++;
                        else closed++;
                    }
                    System.out.println("Total bankers: " + bankers.size());
                    System.out.println("Total loans: " + loans.size());
                    System.out.println("Active loans: " + active);
                    System.out.println("Closed loans: " + closed);
                    System.out.printf("Total loan principal: Rs. %.2f%n", totalPrincipal);
                    break;
                }
                case 6:
                    System.out.println("Admin logged out.");
                    break;
                default:
                    System.out.println("Invalid choice. Choose 1-6.");
            }
        } while (choice != 6);
    }
}
