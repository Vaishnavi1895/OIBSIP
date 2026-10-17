import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Random;

public class ReservationApp {

    static final String URL = "jdbc:sqlite:reservation.db";
    static final Color PRIMARY = new Color(0x1E5AA8);
    static final Color DANGER = new Color(0xC0392B);
    static final Color BG = new Color(0xF4F6FA);

    // ======================= DATABASE =======================
    static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    static void initDb() throws SQLException {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users(username TEXT PRIMARY KEY, password TEXT NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS trains(train_no INTEGER PRIMARY KEY, train_name TEXT NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS reservations(" +
                    "pnr TEXT PRIMARY KEY, passenger_name TEXT NOT NULL, train_no INTEGER NOT NULL, " +
                    "train_name TEXT NOT NULL, class_type TEXT NOT NULL, journey_date TEXT NOT NULL, " +
                    "source TEXT NOT NULL, destination TEXT NOT NULL)");
            s.execute("INSERT OR IGNORE INTO users VALUES('admin','admin123')");
            s.execute("INSERT OR IGNORE INTO trains VALUES(12123,'Deccan Queen')");
            s.execute("INSERT OR IGNORE INTO trains VALUES(12951,'Mumbai Rajdhani')");
            s.execute("INSERT OR IGNORE INTO trains VALUES(12009,'Shatabdi Express')");
            s.execute("INSERT OR IGNORE INTO trains VALUES(11041,'Mumbai Express')");
            s.execute("INSERT OR IGNORE INTO trains VALUES(12627,'Karnataka Express')");
        }
    }

    static boolean checkLogin(String user, String pass) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username=? AND password=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            return ps.executeQuery().next();
        }
    }

    static String getTrainName(int no) throws SQLException {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement("SELECT train_name FROM trains WHERE train_no=?")) {
            ps.setInt(1, no);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }

    static String saveReservation(String name, int trainNo, String trainName, String cls,
                                  String date, String src, String dst) throws SQLException {
        String sql = "INSERT INTO reservations VALUES(?,?,?,?,?,?,?,?)";
        Random rnd = new Random();
        while (true) {
            String pnr = String.valueOf(1000000000L + (long) (rnd.nextDouble() * 8999999999L));
            try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, pnr);
                ps.setString(2, name);
                ps.setInt(3, trainNo);
                ps.setString(4, trainName);
                ps.setString(5, cls);
                ps.setString(6, date);
                ps.setString(7, src);
                ps.setString(8, dst);
                ps.executeUpdate();
                return pnr;
            } catch (SQLException e) {
                if (!e.getMessage().contains("UNIQUE") && !e.getMessage().contains("PRIMARY")) throw e;
                // PNR collision -> loop and generate another
            }
        }
    }

    static String fetchBooking(String pnr) throws SQLException {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM reservations WHERE pnr=?")) {
            ps.setString(1, pnr);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            return details(rs.getString("pnr"), rs.getString("passenger_name"), rs.getInt("train_no"),
                    rs.getString("train_name"), rs.getString("class_type"), rs.getString("journey_date"),
                    rs.getString("source"), rs.getString("destination"));
        }
    }

    static boolean deleteBooking(String pnr) throws SQLException {
        try (Connection c = connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM reservations WHERE pnr=?")) {
            ps.setString(1, pnr);
            return ps.executeUpdate() > 0;
        }
    }

    static String details(String pnr, String name, int no, String tname, String cls,
                          String date, String src, String dst) {
        return "PNR Number      : " + pnr + "\n" +
               "Passenger       : " + name + "\n" +
               "Train           : " + no + " - " + tname + "\n" +
               "Class           : " + cls + "\n" +
               "Date of Journey : " + date + "\n" +
               "From            : " + src + "\n" +
               "To              : " + dst;
    }

    // ======================= UI HELPERS =======================
    static JButton button(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(170, 38));
        return b;
    }

    static JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    static JTextField field() {
        JTextField f = new JTextField(20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(220, 32));
        return f;
    }

    static void addRow(JPanel p, int row, String text, JComponent comp) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.gridy = row;
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0;
        p.add(label(text), g);
        g.gridx = 1;
        p.add(comp, g);
    }

    static JPanel header(String title) {
        JPanel h = new JPanel();
        h.setBackground(PRIMARY);
        JLabel l = new JLabel(title);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        h.add(l);
        return h;
    }

    static void error(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ======================= LOGIN WINDOW =======================
    static class LoginFrame extends JFrame {
        JTextField userF = field();
        JPasswordField passF = new JPasswordField(20);

        LoginFrame() {
            setTitle("Online Reservation System - Login");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            add(header("Online Reservation System"), BorderLayout.NORTH);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(BG);
            passF.setPreferredSize(new Dimension(220, 32));
            addRow(form, 0, "Username", userF);
            addRow(form, 1, "Password", passF);

            JButton login = button("LOGIN", PRIMARY);
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.insets = new Insets(20, 10, 10, 10);
            form.add(login, g);
            add(form, BorderLayout.CENTER);

            JLabel hint = new JLabel("Demo login: admin / admin123", SwingConstants.CENTER);
            hint.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            add(hint, BorderLayout.SOUTH);

            login.addActionListener(e -> doLogin());
            getRootPane().setDefaultButton(login);

            setSize(430, 340);
            setLocationRelativeTo(null);
        }

        void doLogin() {
            String u = userF.getText().trim();
            String p = new String(passF.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                error(this, "Username and password are required.");
                return;
            }
            try {
                if (checkLogin(u, p)) {
                    dispose();
                    new MainFrame(u).setVisible(true);
                } else {
                    error(this, "Access Denied: invalid username or password.");
                    passF.setText("");
                }
            } catch (SQLException ex) {
                error(this, "Database error: " + ex.getMessage());
            }
        }
    }

    // ======================= MAIN WINDOW =======================
    static class MainFrame extends JFrame {
        MainFrame(String user) {
            setTitle("Online Reservation System");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            add(header("Welcome, " + user), BorderLayout.NORTH);

            JTabbedPane tabs = new JTabbedPane();
            tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
            tabs.addTab("Book Ticket", new BookingPanel());
            tabs.addTab("Cancel Ticket", new CancelPanel());
            add(tabs, BorderLayout.CENTER);

            setSize(620, 620);
            setLocationRelativeTo(null);
        }
    }

    // ======================= BOOKING PANEL =======================
    static class BookingPanel extends JPanel {
        JTextField nameF = field(), trainNoF = field(), trainNameF = field(),
                dateF = field(), srcF = field(), dstF = field();
        JComboBox<String> classCb = new JComboBox<>(
                new String[]{"Sleeper (SL)", "Third AC (3A)", "Second AC (2A)", "First AC (1A)", "Chair Car (CC)"});

        BookingPanel() {
            setLayout(new GridBagLayout());
            setBackground(BG);
            trainNameF.setEditable(false);
            classCb.setPreferredSize(new Dimension(220, 32));
            dateF.setToolTipText("Format: yyyy-MM-dd");

            addRow(this, 0, "Passenger Name", nameF);
            addRow(this, 1, "Train Number", trainNoF);
            addRow(this, 2, "Train Name (auto)", trainNameF);
            addRow(this, 3, "Class Type", classCb);
            addRow(this, 4, "Date (yyyy-MM-dd)", dateF);
            addRow(this, 5, "Source Station", srcF);
            addRow(this, 6, "Destination Station", dstF);

            JButton book = button("BOOK TICKET", PRIMARY);
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0; g.gridy = 7; g.gridwidth = 2; g.insets = new Insets(20, 10, 10, 10);
            add(book, g);

            // auto-populate train name while typing the train number
            trainNoF.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { lookup(); }
                public void removeUpdate(DocumentEvent e) { lookup(); }
                public void changedUpdate(DocumentEvent e) { lookup(); }
            });
            book.addActionListener(e -> book());
        }

        void lookup() {
            String t = trainNoF.getText().trim();
            if (t.isEmpty()) { trainNameF.setText(""); return; }
            try {
                String n = getTrainName(Integer.parseInt(t));
                trainNameF.setText(n == null ? "Train not found" : n);
            } catch (NumberFormatException ex) {
                trainNameF.setText("Invalid number");
            } catch (SQLException ex) {
                trainNameF.setText("DB error");
            }
        }

        void book() {
            String name = nameF.getText().trim();
            String noText = trainNoF.getText().trim();
            String date = dateF.getText().trim();
            String src = srcF.getText().trim();
            String dst = dstF.getText().trim();

            if (name.isEmpty() || noText.isEmpty() || date.isEmpty() || src.isEmpty() || dst.isEmpty()) {
                error(this, "All fields are required.");
                return;
            }
            int trainNo;
            try {
                trainNo = Integer.parseInt(noText);
            } catch (NumberFormatException ex) {
                error(this, "Train number must be numeric.");
                return;
            }
            try {
                LocalDate d = LocalDate.parse(date);
                if (d.isBefore(LocalDate.now())) {
                    error(this, "Journey date cannot be in the past.");
                    return;
                }
            } catch (DateTimeParseException ex) {
                error(this, "Invalid date. Use format yyyy-MM-dd (e.g. 2026-11-20).");
                return;
            }
            if (src.equalsIgnoreCase(dst)) {
                error(this, "Source and destination cannot be the same.");
                return;
            }
            try {
                String trainName = getTrainName(trainNo);
                if (trainName == null) {
                    error(this, "Train number not found in database.");
                    return;
                }
                String cls = (String) classCb.getSelectedItem();
                String pnr = saveReservation(name, trainNo, trainName, cls, date, src, dst);
                JOptionPane.showMessageDialog(this,
                        "Booking Confirmed!\n\n" + details(pnr, name, trainNo, trainName, cls, date, src, dst),
                        "Reservation Successful", JOptionPane.INFORMATION_MESSAGE);
                clear();
            } catch (SQLException ex) {
                error(this, "Database error: " + ex.getMessage());
            }
        }

        void clear() {
            nameF.setText(""); trainNoF.setText(""); trainNameF.setText("");
            dateF.setText(""); srcF.setText(""); dstF.setText("");
            classCb.setSelectedIndex(0);
        }
    }

    // ======================= CANCEL PANEL =======================
    static class CancelPanel extends JPanel {
        JTextField pnrF = field();
        JTextArea area = new JTextArea(9, 34);
        JButton cancelBtn = button("CONFIRM CANCELLATION", DANGER);
        String currentPnr = null;

        CancelPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(BG);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            top.setOpaque(false);
            JButton fetch = button("FETCH", PRIMARY);
            fetch.setPreferredSize(new Dimension(100, 34));
            top.add(label("PNR Number:"));
            top.add(pnrF);
            top.add(fetch);
            add(top, BorderLayout.NORTH);

            area.setEditable(false);
            area.setFont(new Font("Consolas", Font.PLAIN, 14));
            area.setMargin(new Insets(10, 10, 10, 10));
            add(new JScrollPane(area), BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.setOpaque(false);
            cancelBtn.setPreferredSize(new Dimension(240, 40));
            cancelBtn.setEnabled(false);
            bottom.add(cancelBtn);
            add(bottom, BorderLayout.SOUTH);

            fetch.addActionListener(e -> fetch());
            cancelBtn.addActionListener(e -> cancel());
        }

        void fetch() {
            String pnr = pnrF.getText().trim();
            if (pnr.isEmpty()) { error(this, "Please enter a PNR number."); return; }
            if (!pnr.matches("\\d+")) { error(this, "PNR must be numeric."); return; }
            try {
                String d = fetchBooking(pnr);
                if (d == null) {
                    area.setText("");
                    cancelBtn.setEnabled(false);
                    currentPnr = null;
                    error(this, "No booking found for PNR " + pnr);
                } else {
                    area.setText(d);
                    currentPnr = pnr;
                    cancelBtn.setEnabled(true);
                }
            } catch (SQLException ex) {
                error(this, "Database error: " + ex.getMessage());
            }
        }

        void cancel() {
            if (currentPnr == null) return;
            int ans = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel this booking?\nPNR: " + currentPnr,
                    "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ans != JOptionPane.YES_OPTION) return;
            try {
                if (deleteBooking(currentPnr)) {
                    JOptionPane.showMessageDialog(this, "Booking cancelled successfully.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    area.setText("");
                    pnrF.setText("");
                    currentPnr = null;
                    cancelBtn.setEnabled(false);
                }
            } catch (SQLException ex) {
                error(this, "Database error: " + ex.getMessage());
            }
        }
    }

    // ======================= MAIN =======================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        try {
            initDb();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Cannot start database:\n" + e.getMessage());
            return;
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}