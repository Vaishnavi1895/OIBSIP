package atm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ATMGUI extends JFrame {

    private Bank bank;
    private Account currentAccount;

    private int attempts = 0;

    private final ArrayList<Transaction> history =
            new ArrayList<>();

    private final Color navy = new Color(15, 32,  fiftySafe());
    private final Color panelColor = new Color(25, 45, 60);
    private final Color teal = new Color(0, 166, 145);
    private final Color white = Color.WHITE;
    private final Color muted = new Color(175, 193, 205);

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField userField;
    private JPasswordField pinField;
    private JButton loginButton;

    private JLabel balanceLabel;
    private JLabel welcomeLabel;
    private JTextArea messageArea;

    private static int fiftySafe() {
        return 50;
    }

    public ATMGUI() {
        bank = new Bank();

        setTitle("National Bank | ATM Banking");
        setSize(950, 680);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(navy);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createDashboardPanel(), "DASHBOARD");

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");

        setVisible(true);
    }

    // ================= LOGIN SCREEN =================

    private JPanel createLoginPanel() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(navy);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(35, 40, 35, 40));
        card.setPreferredSize(new Dimension(410, 490));

        JLabel logo = new JLabel("NATIONAL BANK");
        logo.setFont(new Font("SansSerif", Font.BOLD, 29));
        logo.setForeground(teal);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("ATM BANKING SYSTEM");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Welcome Back");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcome.setForeground(navy);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel("Sign in to access your account");
        description.setForeground(Color.GRAY);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        userField = new JTextField();
        pinField = new JPasswordField();

        styleInput(userField);
        styleInput(pinField);

        loginButton = createButton("LOGIN TO ACCOUNT", teal);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> login());

        JLabel demo = new JLabel(
                "<html><center>DEMO ACCOUNT<br>"
                + "User ID: user1 &nbsp; | &nbsp; PIN: 1234<br>"
                + "Recipient account: 1002</center></html>");

        demo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        demo.setForeground(Color.GRAY);
        demo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(welcome);
        card.add(Box.createVerticalStrut(6));
        card.add(description);
        card.add(Box.createVerticalStrut(28));

        card.add(leftLabel("USER ID"));
        card.add(Box.createVerticalStrut(6));
        card.add(userField);
        card.add(Box.createVerticalStrut(18));

        card.add(leftLabel("PIN"));
        card.add(Box.createVerticalStrut(6));
        card.add(pinField);
        card.add(Box.createVerticalStrut(22));

        card.add(loginButton);
        card.add(Box.createVerticalStrut(22));
        card.add(demo);

        root.add(card);

        return root;
    }

    // ================= DASHBOARD =================

    private JPanel createDashboardPanel() {
        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(new Color(240, 245, 249));
        root.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(navy);
        header.setBorder(new EmptyBorder(22, 25, 22, 25));

        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(
                brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);

        JLabel brand = new JLabel("NATIONAL BANK");
        brand.setFont(new Font("SansSerif", Font.BOLD, 26));
        brand.setForeground(white);

        JLabel tagline = new JLabel("YOUR MONEY. YOUR CONTROL.");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tagline.setForeground(muted);

        brandPanel.add(brand);
        brandPanel.add(Box.createVerticalStrut(5));
        brandPanel.add(tagline);

        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(
                balancePanel, BoxLayout.Y_AXIS));
        balancePanel.setOpaque(false);

        JLabel balanceTitle = new JLabel("AVAILABLE BALANCE");
        balanceTitle.setForeground(muted);
        balanceTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        balanceTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);

        balanceLabel = new JLabel("Rs. 0.00");
        balanceLabel.setForeground(new Color(106, 230, 194));
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        balanceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        balancePanel.add(balanceTitle);
        balancePanel.add(Box.createVerticalStrut(7));
        balancePanel.add(balanceLabel);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(balancePanel, BorderLayout.EAST);

        JPanel center = new JPanel(new GridLayout(2, 2, 18, 18));
        center.setOpaque(false);

        JButton historyButton =
                createButton("01   TRANSACTION HISTORY",
                        new Color(34, 62, 82));

        JButton withdrawButton =
                createButton("02   WITHDRAW MONEY", teal);

        JButton depositButton =
                createButton("03   DEPOSIT MONEY", teal);

        JButton transferButton =
                createButton("04   TRANSFER MONEY",
                        new Color(34, 62, 82));

        historyButton.addActionListener(e -> showHistory());
        withdrawButton.addActionListener(e -> withdraw());
        depositButton.addActionListener(e -> deposit());
        transferButton.addActionListener(e -> transfer());

        center.add(historyButton);
        center.add(withdrawButton);
        center.add(depositButton);
        center.add(transferButton);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setOpaque(false);

        JLabel activityTitle = new JLabel("  TRANSACTION STATUS");
        activityTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        activityTitle.setForeground(navy);

        messageArea = new JTextArea(4, 30);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        messageArea.setForeground(new Color(45, 65, 80));
        messageArea.setBackground(Color.WHITE);
        messageArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        messageArea.setText(
                "Welcome! Select a transaction to get started.");

        JScrollPane scroll = new JScrollPane(messageArea);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(215, 225, 232)));

        JButton logoutButton = createButton(
                "LOG OUT / QUIT",
                new Color(190, 58, 65));

        logoutButton.addActionListener(e -> logout());

        JPanel activity = new JPanel(new BorderLayout(0, 8));
        activity.setOpaque(false);
        activity.add(activityTitle, BorderLayout.NORTH);
        activity.add(scroll, BorderLayout.CENTER);

        bottom.add(activity, BorderLayout.CENTER);
        bottom.add(logoutButton, BorderLayout.SOUTH);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    // ================= LOGIN LOGIC =================

    private void login() {
        if (attempts >= 3) {
            return;
        }

        String user = userField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        currentAccount = bank.login(user, pin);

        if (currentAccount != null) {
            attempts = 0;

            updateBalance();
            welcomeLabel = new JLabel("Welcome, " + user);

            cardLayout.show(mainPanel, "DASHBOARD");

            messageArea.setText(
                    "LOGIN SUCCESSFUL\n"
                    + "Welcome to National Bank.\n"
                    + "Select an option above to continue.");

            pinField.setText("");

        } else {
            attempts++;
            pinField.setText("");

            if (attempts >= 3) {
                userField.setEnabled(false);
                pinField.setEnabled(false);
                loginButton.setEnabled(false);

                JOptionPane.showMessageDialog(
                        this,
                        "ACCESS DENIED\n"
                        + "Three incorrect login attempts.",
                        "Security Alert",
                        JOptionPane.ERROR_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect User ID or PIN.\n"
                        + "Attempts remaining: " + (3 - attempts),
                        "Login Failed",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // ================= WITHDRAW =================

    private void withdraw() {
        Double amount = askAmount();

        if (amount == null) {
            return;
        }

        if (amount > currentAccount.getBalance()) {
            messageArea.setText(
                    "TRANSACTION FAILED\nInsufficient Funds!");
            return;
        }

        if (currentAccount.withdraw(amount)) {
            history.add(new Transaction(
                    "Withdrawal", amount, "Cash withdrawn"));

            updateBalance();

            messageArea.setText(
                    "WITHDRAWAL SUCCESSFUL\n"
                    + "Amount: Rs. " + money(amount) + "\n"
                    + "Please collect your cash.");
        }
    }

    // ================= DEPOSIT =================

    private void deposit() {
        Double amount = askAmount();

        if (amount == null) {
            return;
        }

        currentAccount.deposit(amount);

        history.add(new Transaction(
                "Deposit", amount, "Money deposited"));

        updateBalance();

        messageArea.setText(
                "DEPOSIT SUCCESSFUL\n"
                + "Amount: Rs. " + money(amount));
    }

    // ================= TRANSFER =================

    private void transfer() {
        String recipientId = JOptionPane.showInputDialog(
                this, "Enter recipient account ID:");

        if (recipientId == null) {
            return;
        }

        recipientId = recipientId.trim();

        Account recipient = bank.findAccount(recipientId);

        if (recipient == null) {
            messageArea.setText("Recipient account not found.");
            return;
        }

        if (recipient.getAccountId().equals(
                currentAccount.getAccountId())) {
            messageArea.setText(
                    "Cannot transfer to your own account.");
            return;
        }

        Double amount = askAmount();

        if (amount == null) {
            return;
        }

        if (amount > currentAccount.getBalance()) {
            messageArea.setText(
                    "TRANSFER FAILED\nInsufficient Funds!");
            return;
        }

        if (currentAccount.withdraw(amount)) {
            recipient.deposit(amount);

            history.add(new Transaction(
                    "Transfer", amount,
                    "Sent to account " + recipientId));

            updateBalance();

            messageArea.setText(
                    "TRANSFER SUCCESSFUL\n"
                    + "Amount: Rs. " + money(amount) + "\n"
                    + "Recipient: " + recipientId);
        }
    }

    // ================= AMOUNT INPUT =================

    private Double askAmount() {
        String input = JOptionPane.showInputDialog(
                this, "Enter amount in rupees:");

        if (input == null) {
            return null;
        }

        try {
            double amount = Double.parseDouble(input.trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Enter a valid positive amount.");
                return null;
            }

            return amount;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a valid number.");
            return null;
        }
    }

    // ================= TRANSACTION HISTORY =================

    private void showHistory() {
        StringBuilder result = new StringBuilder();

        result.append("TRANSACTION HISTORY\n");
        result.append("--------------------------------\n");

        if (history.isEmpty()) {
            result.append("No transactions yet.");
        } else {
            for (Transaction transaction : history) {
                result.append(transaction.toString());
                result.append("\n");
            }
        }

        messageArea.setText(result.toString());
        messageArea.setCaretPosition(0);
    }

    // ================= HELPER METHODS =================

    private void updateBalance() {
        balanceLabel.setText(
                "Rs. " + money(currentAccount.getBalance()));
    }

    private String money(double amount) {
        return String.format("%.2f", amount);
    }

    private JLabel leftLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(navy);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setMaximumSize(new Dimension(330, 42));
        field.setPreferredSize(new Dimension(330, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 65));

        return button;
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}