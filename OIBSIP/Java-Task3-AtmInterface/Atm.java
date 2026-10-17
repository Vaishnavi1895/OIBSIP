
package atm;

import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    private Bank bank;
    private Scanner sc;
    private Account currentAccount;

    private ArrayList<Transaction> history = new ArrayList<>();

    public ATM(Bank bank, Scanner sc) {
        this.bank = bank;
        this.sc = sc;
    }

    public void start() {
        System.out.println("\n================================");
        System.out.println("       NATIONAL BANK ATM");
        System.out.println("================================");

        int attempts = 0;

        while (attempts < 3) {
            System.out.print("Enter User ID: ");
            String userId = sc.nextLine().trim();

            System.out.print("Enter PIN: ");
            String pin = sc.nextLine().trim();

            currentAccount = bank.login(userId, pin);

            if (currentAccount != null) {
                System.out.println("\nLogin Successful!");
                System.out.println("Welcome, " + userId);
                showMenu();
                return;
            }

            attempts++;
            System.out.println("Invalid User ID or PIN.");
            System.out.println("Attempts remaining: " + (3 - attempts));
        }

        System.out.println("\nAccount access denied.");
        System.out.println("Too many incorrect attempts.");
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n================================");
            System.out.println("          ATM MAIN MENU");
            System.out.println("================================");
            System.out.printf("Balance: Rs. %.2f%n",
                              currentAccount.getBalance());
            System.out.println("--------------------------------");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("--------------------------------");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    showHistory();
                    break;
                case "2":
                    withdraw();
                    break;
                case "3":
                    deposit();
                    break;
                case "4":
                    transfer();
                    break;
                case "5":
                    System.out.println("\nThank you for using");
                    System.out.println("NATIONAL BANK ATM.");
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private double readAmount() {
        try {
            System.out.print("Enter amount (Rs.): ");
            double amount = Double.parseDouble(sc.nextLine().trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Enter a valid positive amount.");
                return -1;
            }

            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Enter numbers only.");
            return -1;
        }
    }

    private void withdraw() {
        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        if (amount > currentAccount.getBalance()) {
            System.out.println("Insufficient Funds");
            return;
        }

        if (currentAccount.withdraw(amount)) {
            history.add(new Transaction(
                "Withdrawal", amount, "Cash withdrawn"));

            System.out.printf("Withdrawal successful! Balance: Rs. %.2f%n",
                              currentAccount.getBalance());
        }
    }

    private void deposit() {
        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        currentAccount.deposit(amount);

        history.add(new Transaction(
            "Deposit", amount, "Money deposited"));

        System.out.printf("Deposit successful! Balance: Rs. %.2f%n",
                          currentAccount.getBalance());
    }

    private void transfer() {
        System.out.print("Enter recipient account ID: ");
        String recipientId = sc.nextLine().trim();

        Account recipient = bank.findAccount(recipientId);

        if (recipient == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        if (recipient.getAccountId().equals(
                currentAccount.getAccountId())) {
            System.out.println("Cannot transfer to your own account.");
            return;
        }

        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        if (amount > currentAccount.getBalance()) {
            System.out.println("Insufficient Funds");
            return;
        }

        if (currentAccount.withdraw(amount)) {
            recipient.deposit(amount);

            history.add(new Transaction(
                "Transfer", amount,
                "Sent to account " + recipientId));

            System.out.println("Transfer successful!");
            System.out.printf("Your balance: Rs. %.2f%n",
                              currentAccount.getBalance());
        }
    }

    private void showHistory() {
        System.out.println("\n================================");
        System.out.println("       TRANSACTION HISTORY");
        System.out.println("================================");

        if (history.isEmpty()) {
            System.out.println("No transactions in this session.");
            return;
        }

        for (Transaction transaction : history) {
            System.out.println(transaction);
        }

        System.out.println("================================");
    }
}