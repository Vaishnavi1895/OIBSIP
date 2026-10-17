
package atm;

import java.util.ArrayList;

public class Bank {
    private ArrayList<Account> accounts = new ArrayList<>();

    public Bank() {
        accounts.add(new Account("user1", "1234",
                                 "1001", 10000.00));

        accounts.add(new Account("user2", "5678",
                                 "1002", 5000.00));
    }

    public Account login(String userId, String pin) {
        for (Account account : accounts) {
            if (account.getUserId().equals(userId)
                    && account.checkPin(pin)) {
                return account;
            }
        }
        return null;
    }

    public Account findAccount(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }
}