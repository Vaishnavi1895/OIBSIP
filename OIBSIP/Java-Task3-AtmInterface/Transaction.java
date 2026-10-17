
package atm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String type;
    private double amount;
    private String details;
    private String date;

    public Transaction(String type, double amount, String details) {
        this.type = type;
        this.amount = amount;
        this.details = details;

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        this.date = LocalDateTime.now().format(formatter);
    }

    @Override
    public String toString() {
        return date + " | " + type
            + " | Rs. " + String.format("%.2f", amount)
            + " | " + details;
    }
}