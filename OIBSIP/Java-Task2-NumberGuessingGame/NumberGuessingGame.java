import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NumberGuessingGame extends JFrame {

    private int number;
    private int attempts;
    private int maxAttempts;
    private int score = 0;
    private int round = 1;

    private final Random random = new Random();

    private JLabel feedback;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JLabel roundLabel;
    private JTextField guessField;
    private JComboBox<String> difficulty;
    private JButton guessButton;
    private JButton playAgainButton;

    public NumberGuessingGame() {
        setTitle("Number Guessing Challenge");
        setSize(560, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel();
        main.setBackground(new Color(20, 24, 45));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(
                30, 45, 30, 45));

        JLabel title = new JLabel("NUMBER QUEST");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(155, 120, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Guess the secret number!");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        roundLabel = createLabel("ROUND 1");
        scoreLabel = createLabel("SCORE: 0");

        JPanel stats = new JPanel(new GridLayout(1, 2, 12, 0));
        stats.setOpaque(false);
        stats.add(roundLabel);
        stats.add(scoreLabel);

        JLabel levelLabel = createLabel("SELECT DIFFICULTY");
        difficulty = new JComboBox<>(
                new String[]{"Easy", "Medium", "Hard"});
        difficulty.setMaximumSize(new Dimension(400, 38));
        difficulty.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel rangeLabel = createLabel("ENTER A NUMBER");
        guessField = new JTextField();
        guessField.setMaximumSize(new Dimension(400, 48));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setFont(new Font("Arial", Font.BOLD, 24));

        guessButton = new JButton("GUESS NOW");
        styleButton(guessButton, new Color(115, 75, 235));

        feedback = createLabel("Choose a level and start guessing!");
        feedback.setForeground(new Color(100, 220, 190));

        attemptsLabel = createLabel("Attempts: 0 / 10");

        playAgainButton = new JButton("PLAY AGAIN");
        styleButton(playAgainButton, new Color(35, 155, 130));
        playAgainButton.setVisible(false);

        main.add(title);
        main.add(Box.createVerticalStrut(8));
        main.add(subtitle);
        main.add(Box.createVerticalStrut(25));
        main.add(stats);
        main.add(Box.createVerticalStrut(25));
        main.add(levelLabel);
        main.add(Box.createVerticalStrut(8));
        main.add(difficulty);
        main.add(Box.createVerticalStrut(20));
        main.add(rangeLabel);
        main.add(Box.createVerticalStrut(8));
        main.add(guessField);
        main.add(Box.createVerticalStrut(18));
        main.add(guessButton);
        main.add(Box.createVerticalStrut(20));
        main.add(feedback);
        main.add(Box.createVerticalStrut(8));
        main.add(attemptsLabel);
        main.add(Box.createVerticalStrut(20));
        main.add(playAgainButton);

        add(main);

        guessButton.addActionListener(e -> checkGuess());
        guessField.addActionListener(e -> checkGuess());

        playAgainButton.addActionListener(e -> {
            round++;
            startRound();
        });

        difficulty.addActionListener(e -> {
            if (guessButton.isEnabled()) {
                startRound();
            }
        });

        startRound();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 45));
    }

    private void startRound() {
        String level = (String) difficulty.getSelectedItem();

        int upperLimit;
        if ("Easy".equals(level)) {
            upperLimit = 50;
            maxAttempts = 10;
        } else if ("Hard".equals(level)) {
            upperLimit = 200;
            maxAttempts = 5;
        } else {
            upperLimit = 100;
            maxAttempts = 7;
        }

        number = random.nextInt(upperLimit) + 1;
        attempts = 0;

        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        difficulty.setEnabled(true);
        playAgainButton.setVisible(false);

        feedback.setText("Guess a number from 1 to " + upperLimit);
        attemptsLabel.setText("Attempts: 0 / " + maxAttempts);
        roundLabel.setText("ROUND " + round);
        scoreLabel.setText("SCORE: " + score);

        revalidate();
        repaint();
    }

    private void checkGuess() {
        if (!guessButton.isEnabled()) {
            return;
        }

        String text = guessField.getText().trim();

        int guess;
        try {
            guess = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            feedback.setText("Please enter a whole number!");
            return;
        }

        int upperLimit;
        String level = (String) difficulty.getSelectedItem();

        if ("Easy".equals(level)) {
            upperLimit = 50;
        } else if ("Hard".equals(level)) {
            upperLimit = 200;
        } else {
            upperLimit = 100;
        }

        if (guess < 1 || guess > upperLimit) {
            feedback.setText("Enter a number from 1 to " + upperLimit);
            return;
        }

        attempts++;
        attemptsLabel.setText(
                "Attempts: " + attempts + " / " + maxAttempts);

        if (guess == number) {
            score += (maxAttempts - attempts + 1) * 10;
            scoreLabel.setText("SCORE: " + score);
            feedback.setText("CORRECT! YOU WIN!");
            feedback.setForeground(new Color(80, 240, 160));
            finishRound(true);
        } else if (attempts >= maxAttempts) {
            feedback.setText("YOU LOST! Number was " + number);
            feedback.setForeground(new Color(255, 100, 120));
            finishRound(false);
        } else if (guess > number) {
            feedback.setText("Too High! Try a smaller number.");
            feedback.setForeground(new Color(255, 185, 90));
        } else {
            feedback.setText("Too Low! Try a bigger number.");
            feedback.setForeground(new Color(100, 200, 255));
        }

        guessField.setText("");
        guessField.requestFocusInWindow();
    }

    private void finishRound(boolean won) {
        guessButton.setEnabled(false);
        guessField.setEnabled(false);
        difficulty.setEnabled(false);
        playAgainButton.setVisible(true);

        JOptionPane.showMessageDialog(
                this,
                won ? "Congratulations! You guessed it!"
                    : "Better luck next time!",
                won ? "Winner!" : "Game Over",
                won ? JOptionPane.INFORMATION_MESSAGE
                    : JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new NumberGuessingGame().setVisible(true));
    }
}